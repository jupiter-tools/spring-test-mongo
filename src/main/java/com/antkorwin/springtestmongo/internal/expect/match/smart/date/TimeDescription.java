package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antkorwin.commonutils.exceptions.InternalException;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TimeDescription {

    private static final String TIME_DESCRIPTION_PATTERN =
            "^((date-match:|date:)\\[NOW\\])" +
            "((\\+|\\-)([0-9]{1,7})(\\((DAYS|HOURS|MINUTES|SECONDS)\\))){0,1}" +
            "(\\{THR=([0-9]{1,10})\\}){0,1}$";

    private final static int DEFAULT_THRESHOLD = 10_000;
    private final String description;
    private Integer threshold = null;
    private TimeDescriptionType type = null;

    public TimeDescription(String description) {
        this.description = description;
    }

    //@formatter:off
    /**
     * Parse time operation from the String description.
     *
     * direction (group 3)
     *    |
     *    |  TimeUnit (group 6)
     *    |    |
     *    V    V
     * NOW+17(DAYS)
     *      ^
     *      |
     *    amount of time (group 4)
     *
     * @return time operation
     */
    //@formatter:on
    public TimeOperation getTimeOperation() {

        Matcher matcher = matchTimeDescriptionPattern(description);

        if (parseTimeOperation(matcher) == null) {
            return new TimeOperation(TimeDirection.UNDEFINED, null, 0);
        }

        TimeDirection direction = parseDirection(matcher);
        int count = parseCount(matcher);
        TimeUnit unit = parseTimeUnit(matcher);
        threshold = parseThreshold(matcher);

        return new TimeOperation(direction, unit, count);
    }

    private Matcher matchTimeDescriptionPattern(String value) {
        Pattern pattern = Pattern.compile(TIME_DESCRIPTION_PATTERN);
        Matcher matcher = pattern.matcher(value);
        matcher.find();
        return matcher;
    }

    private String parseTimeOperation(Matcher matcher) {
        return matcher.group(3);
    }

    private TimeUnit parseTimeUnit(Matcher matcher) {
        return TimeUnit.valueOf(matcher.group(7));
    }

    private int parseCount(Matcher matcher) {
        return Integer.valueOf(matcher.group(5));
    }

    private TimeDirection parseDirection(Matcher matcher) {
        switch (matcher.group(4)) {
            case "+":
                return TimeDirection.PLUS;
            case "-":
                return TimeDirection.MINUS;
            default:
                throw new InternalException("unsupported operation", 106);
        }
    }

    /**
     * Match a description with the pattern of expected time
     *
     * @return true if description match to pattern and false if not
     */
    public boolean matches() {
        Matcher matcher = matchTimeDescriptionPattern(description);
        return matcher.matches();
    }

    /**
     * @return amount of the possible threshold
     */
    public long getThreshold() {
        if (threshold == null) {
            Matcher matcher = matchTimeDescriptionPattern(description);
            threshold = parseThreshold(matcher);
        }
        return threshold;
    }

    private Integer parseThreshold(Matcher matcher) {
        return threshold = (matcher.group(9) != null)
                           ? Integer.valueOf(matcher.group(9))
                           : DEFAULT_THRESHOLD;
    }

    /**
     * @return Type of the current description
     */
    public TimeDescriptionType getType() {
        if (type != null) {
            return type;
        }
        type = parseType();
        return type;
    }

    private TimeDescriptionType parseType() {
        Matcher matcher = matchTimeDescriptionPattern(description);
        if (!matcher.matches()) {
            throw new InternalException("Unsupported type of Date Matcher", 107);
        }
        String prefix = matcher.group(2);
        switch (prefix) {
            case "date-match:":
                return TimeDescriptionType.MATCH;
            case "date:":
                return TimeDescriptionType.DYNAMIC_VALUE;
            default:
                throw new InternalException("Unsupported type of Date Matcher", 107);
        }
    }
}
