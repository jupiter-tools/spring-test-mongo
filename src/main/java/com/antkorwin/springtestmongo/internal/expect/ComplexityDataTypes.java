package com.antkorwin.springtestmongo.internal.expect;


import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Check complexity of the object
 */
class ComplexityDataTypes {

    private final Set<Class> simpleTypes =
            Sets.newHashSet(Boolean.class,
                            Byte.class,
                            Short.class,
                            Character.class,
                            Integer.class,
                            Long.class,
                            Float.class,
                            Double.class,
                            String.class);

    /**
     * Check the object is a simple wrapper type or not.
     *
     * @param object checked object
     *
     * @return true if the object type is a simple and
     * false when this object will convert to a map before comparing.
     */
    boolean isComplexType(Object object) {
        Class<?> type = object.getClass();
        return !type.isPrimitive() && !type.isEnum() && !simpleTypes.contains(type);
    }
}