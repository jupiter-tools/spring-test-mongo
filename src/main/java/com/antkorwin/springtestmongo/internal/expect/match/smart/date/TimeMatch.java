package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import java.util.Date;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TimeMatch {

    private final TimeOperation timeOperation;
    private final long threshold;
    private final Date initialTime;

    public TimeMatch(Date initialTime,
                     TimeOperation timeOperation,
                     long threshold) {

        this.timeOperation = timeOperation;
        this.threshold = threshold;
        this.initialTime = initialTime;
    }

    public boolean match(Date actualDate) {
        Date expectedDate = timeOperation.evaluate(initialTime);
        return Math.abs(expectedDate.getTime() - actualDate.getTime()) <= threshold;
    }
}
