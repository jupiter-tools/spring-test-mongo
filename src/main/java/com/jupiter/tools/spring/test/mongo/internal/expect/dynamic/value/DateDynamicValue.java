package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

import com.jupiter.tools.spring.test.mongo.internal.expect.match.smart.date.TimeDescription;

import java.util.Date;

/**
 * Created on 26.12.2018.
 * <p>
 * Replaces date-time data values in dynamic data sets
 *
 * @author Korovin Anatoliy
 */
public class DateDynamicValue implements DynamicValue {

    @Override
    public boolean isNecessary(Object value) {

        if (!(value instanceof String)) {
            return false;
        }

        String val = (String) value;

        if (!val.startsWith("date:")) {
            return false;
        }

        return new TimeDescription(trimPrefix(val)).matches();
    }

    @Override
    public Object evaluate(Object value) {

        return new TimeDescription(trimPrefix((String) value)).getTimeOperation()
                                                              .evaluate(new Date());
    }

    private String trimPrefix(String valueWithPrefix) {
        return valueWithPrefix.replaceFirst("date:", "")
                              .replaceAll(" ", "");
    }
}
