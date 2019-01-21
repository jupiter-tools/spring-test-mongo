package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import com.antkorwin.springtestmongo.internal.expect.match.smart.MatchDataSmart;

import java.util.Date;

/**
 * Created on 22.12.2018.
 * <p>
 * Match Date to expected pattern.
 *
 * @author Korovin Anatoliy
 */
public class MatchDate implements MatchDataSmart {

    @Override
    public boolean match(Object original, Object expected) {

        TimeDescription description = new TimeDescription(trimPrefix((String) expected));

        TimeMatch timeMatch = new TimeMatch(new Date(),
                                            description.getTimeOperation(),
                                            description.getThreshold());

        Date originalDate = (original instanceof Long)
                            ? new Date((long) original)
                            : (Date) original;

        return timeMatch.match(originalDate);
    }

    @Override
    public boolean isNecessary(Object expected) {

        if (!(expected instanceof String)) {
            return false;
        }

        String value = (String) expected;
        if (!value.startsWith("date-match:")) {
            return false;
        }

        return new TimeDescription(trimPrefix(value)).matches();
    }

    private String trimPrefix(String value) {
        return value.replaceFirst("date-match:", "")
                    .replaceAll(" ", "");
    }
}
