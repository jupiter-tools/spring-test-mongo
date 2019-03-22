package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

/**
 * Created on 16.12.2018.
 *
 * This class provides an algorithm of the
 * evaluating script-based values in the data set.
 *
 * @author Korovin Anatoliy
 */
public interface DynamicValue {

    /**
     * Check the necessary of apply this value-evaluator.
     *
     * @param value data which we can replace on the evaluated
     *
     * @return true if this {@link DynamicValue} is possible to apply
     * and false if not.
     */
    boolean isNecessary(Object value);

    /**
     * evaluate dynamic value
     *
     * @param value content of the value with some scripts or instructions
     *
     * @return evaluated value
     */
    Object evaluate(Object value);
}
