package com.antkorwin.springtestmongo.internal.expect.match.smart.groovy;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.expect.match.smart.MatchDataSmart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchGroovy implements MatchDataSmart {

    private static final String PREFIX = "groovy-match:";
    private final ScriptEngine engine;
    private final Logger log = LoggerFactory.getLogger(MatchGroovy.class);

    public MatchGroovy() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("groovy");
    }

    @Override
    public boolean match(Object original, Object expected) {
        try {
            String expectedValue = ((String) expected).replaceFirst(PREFIX, "");
            engine.put("value", original);
            Object result = engine.eval(expectedValue);
            if (!(result instanceof Boolean)) {
                throw new InternalException(PREFIX + " must return a boolean value instead of {" + result + "}", 106);
            }
            return (boolean) result;
        } catch (ScriptException e) {
            log.error("Groovy engine evaluate error: ", e);
            throw new InternalException("Groovy engine evaluate error", 104, e);
        }
    }

    @Override
    public boolean isNecessary(Object expected) {
        if (!(expected instanceof String)) {
            return false;
        }
        return ((String) expected).startsWith(PREFIX);
    }
}
