package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

/**
 * Created on 16.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class SimpleDynamicValue implements DynamicValue {

    @Override
    public boolean isNecessary(Object value) {
        return value instanceof String &&
               ((String) value).startsWith("fixme:");
    }

    @Override
    public Object evaluate(Object value) {
        return "{fixed}";
    }
}
