package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;

import java.util.List;

public class Andy {

    private final Action action;
    private final String workDir;
    private final String outputDir;
    private final List<String> librariesToBeIncludedInCompilation;
    private final ResultWriter writer;

    public Andy(Action action, String workDir, String outputDir, List<String> librariesToBeIncludedInCompilation, ResultWriter writer) {
        this.librariesToBeIncludedInCompilation = librariesToBeIncludedInCompilation;
        this.writer = writer;
        assert action!=null;
        assert workDir!=null;
        assert outputDir!=null;

        this.action = action;
        this.workDir = workDir;
        this.outputDir = outputDir;
    }

    public Andy(Action action, String workDir, String outputDir, ResultWriter writer) {
        this(action, workDir, outputDir, null, writer);
    }



    public void run() {
        Context ctx = buildContext();

        ResultBuilder result = new ResultBuilder(ctx, new GradeCalculator());
        ExecutionFlow flow = ExecutionFlow.build(ctx, result, writer);

        flow.run();
    }

    private Context buildContext() {
        Context ctx = new Context(action);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir,
                outputDir);

        ctx.setDirectoryConfiguration(dirCfg);
        ctx.setLibrariesToBeIncludedInCompilation(librariesToBeIncludedInCompilation);

        return ctx;
    }

}
