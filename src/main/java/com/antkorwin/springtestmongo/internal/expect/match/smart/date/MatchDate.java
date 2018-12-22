package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import com.antkorwin.springtestmongo.internal.expect.match.smart.MatchDataSmart;

import java.util.Date;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchDate implements MatchDataSmart {

    @Override
    public boolean match(Object original, Object expected) {

        TimeDescription description = new TimeDescription((String) expected);

        TimeMatch timeMatch = new TimeMatch(new Date(),
                                            description.getTimeOperation(),
                                            description.getThreshold());

        return timeMatch.match((Date) original);
    }

    @Override
    public boolean isNecessary(Object expected) {
        if (!(expected instanceof String)) {
            return false;
        }
        TimeDescription description = new TimeDescription((String) expected);
        return description.matches();
    }
}
