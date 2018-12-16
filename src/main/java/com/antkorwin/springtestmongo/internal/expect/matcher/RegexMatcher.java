package com.antkorwin.springtestmongo.internal.expect.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Match two objects with using a regular expression in the expected value.
 *
 * @author Korovin Anatoliy
 */
public class RegexMatcher implements ValueMatcher {

    @Override
    public boolean match(Object originValue, Object comparableValue) {
        String cmpValue = (String) comparableValue;
        cmpValue = cmpValue.replaceFirst("regex:", "").trim();
        Pattern pattern = Pattern.compile(cmpValue);
        Matcher matcher = pattern.matcher((String) originValue);
        return matcher.matches();
    }

    @Override
    public boolean isNecessary(Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        String strValue = (String) value;
        return (strValue.startsWith("regex:"));
    }
}