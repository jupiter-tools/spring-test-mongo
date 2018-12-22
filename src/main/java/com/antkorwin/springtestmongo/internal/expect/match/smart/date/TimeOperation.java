package com.antkorwin.springtestmongo.internal.expect.match.smart.date;


import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TimeOperation {

    private final TimeDirection direction;
    private final TimeUnit unit;
    private final int count;

    public TimeOperation(TimeDirection direction, TimeUnit unit, int count) {
        this.direction = direction;
        this.count = count;
        this.unit = unit;
    }

    /**
     * Apply time operation to the initialTime
     *
     * @param initialTime start value to evaluate operation
     * @return the result of applying this operation
     */
    public Date evaluate(Date initialTime) {
        return new Date(evaluateInMillis(initialTime));
    }

    private long evaluateInMillis(Date initialDate) {
        switch (direction) {
            case PLUS:
                return initialDate.getTime() + unit.toMillis(count);
            case MINUS:
                return initialDate.getTime() - unit.toMillis(count);
            default:
                return initialDate.getTime();
        }
    }
}