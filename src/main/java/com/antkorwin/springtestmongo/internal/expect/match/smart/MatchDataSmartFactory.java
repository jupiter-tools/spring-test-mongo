package com.antkorwin.springtestmongo.internal.expect.match.smart;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.expect.match.smart.date.MatchDate;
import com.antkorwin.springtestmongo.internal.expect.match.smart.groovy.MatchGroovy;
import com.antkorwin.springtestmongo.internal.expect.match.smart.regexp.MatchRegExp;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchDataSmartFactory {

    private List<MatchDataSmart> matchDataList = Arrays.asList(new MatchGroovy(),
                                                               new MatchDate(),
                                                               new MatchRegExp());

    public boolean isNecessary(Object expected) {
        for (MatchDataSmart matchData : matchDataList) {
            if (matchData.isNecessary(expected)) {
                return true;
            }
        }
        return false;
    }

    public MatchDataSmart get(Object expected) {
        for (MatchDataSmart matchData : matchDataList) {
            if (matchData.isNecessary(expected)) {
                return matchData;
            }
        }
        throw new InternalException("Not found MatchDataSmart for {" + expected.toString() + "} object.", 105);
    }
}
