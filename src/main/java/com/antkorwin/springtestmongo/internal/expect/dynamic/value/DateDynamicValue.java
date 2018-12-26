package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

import java.util.Date;

import com.antkorwin.springtestmongo.internal.expect.match.smart.date.TimeDescription;
import com.antkorwin.springtestmongo.internal.expect.match.smart.date.TimeDescriptionType;

/**
 * Created on 26.12.2018.
 *
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
        TimeDescription timeDescription = new TimeDescription((String) value);
        if (!timeDescription.matches()) {
            return false;
        }
        return TimeDescriptionType.DYNAMIC_VALUE.equals(timeDescription.getType());
    }

    @Override
    public Object evaluate(Object value) {
        return new TimeDescription((String) value).getTimeOperation()
                                                  .evaluate(new Date());
    }
}
