package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.MetaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class RunConfiguration {

    protected static final List<String> OLD_DEFAULTS = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "RETURN_VALS", "VOID_METHOD_CALLS");

    protected static final List<String> DEFAULTS = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS");

    protected static final List<String> STRONGER = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS", "REMOVE_CONDITIONALS_EQ_ELSE", "EXPERIMENTAL_SWITCH");

    protected static final List<String> ALL = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS", "REMOVE_CONDITIONALS", "EXPERIMENTAL_SWITCH",
            "INLINE_CONSTS", "NON_VOID_METHOD_CALLS", "REMOVE_CONDITIONALS", "REMOVE_INCREMENTS", "EXPERIMENTAL_ARGUMENT_PROPAGATION",
            "EXPERIMENTAL_BIG_INTEGER", "EXPERIMENTAL_NAKED_RECEIVER", "EXPERIMENTAL_MEMBER_VARIABLE", "ABS",
            "AOR", "AOD", "CRCR", "OBBN", "ROR", "UOI");

    public abstract List<String> classesUnderTest();

    public abstract Map<String, Float> weights();

    public CheckScript checkScript() {
        return new CheckScript(Arrays.asList());
    }

    public List<MetaTest> metaTests() {
        return Collections.emptyList();
    }

    public boolean failureGivesZero() {
        return true;
    }

    public List<String> listOfMutants() {
        return DEFAULTS;
    }

}
