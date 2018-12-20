package com.antkorwin.springtestmongo.internal.expect.match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 19.12.2018.
 *
 * Match original string to expected string, when in the expected string
 * use a regular expression.
 *
 * @author Korovin Anatoliy
 */
public class MatchRegExp implements MatchData {

    @Override
    public boolean match(Object originValue, Object comparableValue) {
        String cmpValue = (String) comparableValue;
        cmpValue = cmpValue.replaceFirst("regex:", "").trim();
        Pattern pattern = Pattern.compile(cmpValue);
        Matcher matcher = pattern.matcher((String) originValue);
        return matcher.matches();
    }

    public boolean isNecessary(Object valueWithRegExp) {
        if (!(valueWithRegExp instanceof String)) {
            return false;
        }
        String strValue = (String) valueWithRegExp;
        return (strValue.startsWith("regex:"));
    }
}
