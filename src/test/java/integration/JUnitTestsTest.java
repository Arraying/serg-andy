package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static testutils.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JUnitTestsTest {

    @Nested
    class TraditionalJUnitTests extends IntegrationTestBase {

        @Test
        void allTestsPassing() {

            String result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(4))
                    .has(totalNumberOfJUnitTests(4))
                    .doesNotHave(failingTests())
                    .doesNotHave(consoleOutputExists()); // ensure that if there's no console output, we do not show it
        }

        // In test 1 and 2,  30*3 should be 30+50
        // In test 3, 30+50 should be 30*3
        // example: student misinterpreted the source code.
        // 1/4 normal @Tests passing
        @Test
        void someTestsFailing() {

            String result = run(Action.TESTS, "PlayerPointsLibrary", "PlayerPointsSomeTestsFail");
            System.out.println(result);

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(numberOfJUnitTestsPassing(1))
                    .has(totalNumberOfJUnitTests(4))
                    .has(failingTestName("lessPoints"))
                    .has(failingTestName("manyPointsAndManyLives"))
                    .has(failingTestName("manyPointsButLittleLives"))
                    .has(allTestsNeedToPassMessage())
                    .has(errorType("AssertionFailedError"));

        }

        // example: student forgets @Test
        // 0/0 normal @Tests passing
        @Test
        void noTests() {
            String result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsNoTests");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }

        //Check to see if the System.out.printlns are caught from the console
        @Test
        void showConsoleOutput(){

            String result = run(Action.TESTS, "ZagZigLibrary", "ZagZigRandomSysouts");

            assertThat(result)
                    .has(consoleOutputExists())
                    .has(consoleOutput("RandomString1"))
                    .has(consoleOutput("HelloWorld2"))
                    .has(consoleOutput("I love SQT3"));
        }

    }

    @Nested
    class Mockito extends IntegrationTestBase {

        @Test
        void mocksWork() {
            String result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereTests");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(3))
                    .has(totalNumberOfJUnitTests(3))
                    .doesNotHave(failingTests());
        }

    }


    @Nested
    class ParameterizedTests extends IntegrationTestBase {

        @Test
        void parameterizedTestsPass() {
            String result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(31))
                    .has(totalNumberOfJUnitTests(31))
                    .doesNotHave(failingTests());
        }

        // in test cases 1 and 2 of "validTest", 22 and 44 should be numbers in (20, 200) divisible by 20.
        // in test case 1 of "invalidTest", 40 should not be divisible by 20.
        @Test
        void someParameterizedTestsFail() {

            String result = run(Action.TESTS, "ATMLibrary", "ATMMoreParameterizedTestsFail");  // 11/14 parameterized test cases

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(numberOfJUnitTestsPassing(11))
                    .has(totalNumberOfJUnitTests(14))
                    .has(parameterizedTestFailing("validTest", 1))
                    .has(parameterizedTestFailing("validTest", 2))
                    .has(parameterizedTestFailing("invalidTest", 1))
                    .has(errorType("AssertionFailedError"));
        }

        @Test
        void helperMethodsCanBeUsed() {

            String result = run(Action.TESTS, "PiecewiseLibrary", "PiecewiseHelperInTest");  // 26/26 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(26))
                    .has(totalNumberOfJUnitTests(26));
        }



    }

    @Nested
    class JQWik extends IntegrationTestBase {

        @Test
        void testSimplePropertyTest() {
            String result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfSimpleJqwikError");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(propertyTestFailing("testNoElementInWholeArray"));
        }


        @Test
        void testMultiplePropertyTestsFailing() {
            String result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfMultipleJqwikErrors");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(allTestsNeedToPassMessage())
                    .has(propertyTestFailing("testNoElementInWholeArray"))
                    .has(propertyTestFailing("testValueInArrayUniqueElements"));
        }


        @Test
        void testMultiplePropertyWithParameterizedTests() {
            String result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJqwikWithParameterized");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(propertyTestFailing("testNoElementInWholeArray"))
                    .has(propertyTestFailing("testValueInArrayUniqueElements"))
                    .has(parameterizedTestFailing("test", 6));
        }


        @Test
        void testMessageOtherThanAssertionError() {
            String result = run(Action.TESTS, "NumberUtilsAddPositiveLibrary", "NumberUtilsAddPositiveJqwikException");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(propertyTestFailing("testAddition"));
        }

    }


    @Nested
    class MixedTypesOfTests extends IntegrationTestBase {

        // test class contains normal @Tests, parameterized tests and pbt.
        @Test
        void threeDifferentTestTypesUsed() {

            String result = run(Action.TESTS, "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfDifferentTestTypes");  // 5/5 @Tests passing

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(5))
                    .has(totalNumberOfJUnitTests(5))
                    .doesNotHave(failingTests());
        }
    }


    @Nested
    class GeneralMistakes extends IntegrationTestBase {

        // @BeforeAll methods should be static -> no tests detected
        @Test
        void jUnitCantRunIfBeforeAllIsNonStatic(){

            String result = run(Action.TESTS, "PiecewiseLibrary", "PiecewiseNonStaticBeforeAll");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }


        // @BeforeEach methods should NOT be static -> no tests detected
        @Test
        void junitCantRunIfBeforeEachIsStatic(){

            String result = run(Action.TESTS, "PiecewiseLibrary", "PiecewiseStaticBeforeEach");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }

        // error in @Test1: instead of completeTodo(), addTodo() should be invoked.
        @Test
        void methodVerifiedButNotInvoked() {

            String result = run(Action.TESTS, "TodoApplicationLibrary", "TodoApplicationMockitoMethodNotInvoked");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(numberOfJUnitTestsPassing(2))
                    .has(totalNumberOfJUnitTests(3))
                    .has(failingTestName("addTodoTest"))
                    .has(uninvokedMethod("todoService.completeTodo"))
                    .has(hintAtInteractionFound("todoService.addTodo"));
        }

        // error in @Test 3: student is misusing Mockito stubs in line 48: TheQueue q is not a mock, thus its methods cannot be stubbed!
        @Test
        void stubbingNonMockClass() {

            String result = run(Action.TESTS, "TheQueueLibrary", "TheQueueMisusingMockitoStub");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(numberOfJUnitTestsPassing(2))
                    .has(totalNumberOfJUnitTests(3))
                    .has(failingTestName("getNextReturnsFirst"))
                    .has(errorType("mockito.exceptions.misusing"));
        }


        // student reversed the 2 method names "invalidInputs" and "validInputs",
        //  raising exceptions since the number of arguments passed is incorrect, and thus making all tests fail.
        // in "invalidInputs", an exception is expected, whereas this is not being thrown by the method.
        // in "validInputs", exceptions are raised, whereas this is not expected.
        @Test
        void exceptionThrownByTest() {

            String result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionParameterizedTestThrowsException");  // 0/5 parameterized test cases

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(numberOfJUnitTestsPassing(0))
                    .has(totalNumberOfJUnitTests(5))
                    .has(parameterizedTestFailing("validInputs", 1))
                    .has(parameterizedTestFailing("validInputs", 2))
                    .has(parameterizedTestFailing("invalidInputs", 1))
                    .has(parameterizedTestFailing("invalidInputs", 2))
                    .has(parameterizedTestFailing("invalidInputs", 3))
                    .has(errorType("ParameterResolutionException"))
                    .has(errorType("AssertionError"))
                    .has(errorMessage("Expecting code to raise a throwable."));
        }

        // @MethodSource method should be static -> test failure -> 2/2 pass (but in GitHub this is 0/2?)
        @Test
        void nonStaticMethodSourceSomeFail() {

            String result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionNonStaticMethodSourceSomeFail");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(2))
                    .has(totalNumberOfJUnitTests(2))
                    .has(hintAtNonStaticMethodSource("tudelft.domain.MScAdmissionTest.validGenerator"));
        }


        // @MethodSource method should be static -> test failure
        @Test
        void nonStaticMethodSourceAllFail() {

            String result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionNonStaticMethodSourceAllFail");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }

        // Student forgot @ParameterizedTest -> no tests detected
        @Test
        void forgotParameterizedTestAnnotation() {

            String result = run(Action.TESTS, "PassingGradeLibrary", "PassingGradeForgotParameterizedTestAnnotation");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }

        //         Student forgot @MethodSource -> no tests detected
        @Test
        void forgotMethodSourceAnnotationAllFail() {

            String result = run(Action.TESTS, "PassingGradeLibrary", "PassingGradeForgotMethodSourceAnnotationAllFail");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .has(weDoNotSeeTestsMessage());
        }

        //         Student forgot @MethodSource -> 3/3 tests pass
        @Test
        void forgotMethodSourceAnnotationSomeFail() {

            String result = run(Action.TESTS, "MScAdmissionLibrary", "MScAdmissionForgotMethodSourceSomeFail");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(3))
                    .has(totalNumberOfJUnitTests(3))
                    .has(noMethodSourceProvidedMessage());
        }

        // Student forgot @Property -> no tests detected
        @Test
        void forgotPropertyAnnotation() {

            String result = run(Action.TESTS, "MathArraysLibrary", "MathArraysForgotProperty");

            assertThat(result)
                    .has(finalGradeInXml(workDir.toString(), 0))
                    .doesNotHave(allTestsNeedToPassMessage())
                    .has(weDoNotSeeTestsMessage());
        }

    }

}
