package integration;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static nl.tudelft.cse1110.andy.utils.ResourceUtils.resourceFolder;

public abstract class IntegrationTestBase {
    @TempDir
    protected Path reportDir;

    @TempDir
    protected Path workDir;

    public String run(Action action,
                      String libraryFile,
                      String solutionFile,
                      String configurationFile) {
        if (configurationFile != null) {
            copyConfigurationFile(configurationFile);
        }

        copyFiles(libraryFile, solutionFile);

        Context ctx = new Context(action);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                reportDir.toString()
        );

        ctx.setDirectoryConfiguration(dirCfg);

        ResultBuilder resultBuilder = new ResultBuilder(ctx, new GradeCalculator());

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            WebLabResultWriter writer = new WebLabResultWriter(ctx, getAsciiArtGenerator());
            ExecutionFlow flow = ExecutionFlow.build(ctx, resultBuilder, writer);

            addSteps(flow);

            flow.run();
        } catch(Exception e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }

        return readStdOut();
    }

    protected void addSteps(ExecutionFlow flow) {
    }

    protected RandomAsciiArtGenerator getAsciiArtGenerator() {
        return new RandomAsciiArtGenerator();
    }

    public String run(String libraryFile, String solutionFile) {
        return this.run(Action.FULL_WITH_HINTS, libraryFile, solutionFile, null);
    }

    public String run(Action action, String libraryFile, String solutionFile) {
        return this.run(action, libraryFile, solutionFile, null);
    }

    public String run(String libraryFile, String solutionFile, String configurationFile) {
        return this.run(Action.FULL_WITH_HINTS, libraryFile, solutionFile, configurationFile);
    }

    private String readStdOut() {
        return FilesUtils.readFile(new File(FilesUtils.concatenateDirectories(reportDir.toString(), "stdout.txt")));
    }


    protected void copyFiles(String libraryFile, String solutionFile) {
        String dirWithLibrary = resourceFolder("/grader/fixtures/Library/");
        String dirWithSolution = resourceFolder("/grader/fixtures/Solution/");
        File library = new File(dirWithLibrary +  libraryFile + ".java");
        File solution = new File(dirWithSolution + solutionFile + ".java");

        String dirToCopy = workDir.toString();

        File copiedLibrary = FilesUtils.copyFile(library.getAbsolutePath(), dirToCopy).toFile();
        File copiedSolution = FilesUtils.copyFile(solution.getAbsolutePath(), dirToCopy).toFile();

        copiedLibrary.renameTo(new File(copiedLibrary.getParentFile() + "/Library.java"));
        copiedSolution.renameTo(new File(copiedSolution.getParentFile() + "/Solution.java"));
    }

    protected void copyConfigurationFile(String configurationFile) {
        String dirWithConfiguration = resourceFolder("/grader/fixtures/Config/");

        File config = new File(dirWithConfiguration + configurationFile + ".java");
        File copied = FilesUtils.copyFile(config.getAbsolutePath(), workDir.toString()).toFile();

        copied.renameTo(new File(copied.getParentFile() + "/Configuration.java"));
    }

}
