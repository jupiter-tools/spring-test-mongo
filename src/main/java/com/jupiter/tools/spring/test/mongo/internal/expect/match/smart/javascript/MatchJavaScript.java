package com.jupiter.tools.spring.test.mongo.internal.expect.match.smart.javascript;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.jupiter.tools.spring.test.mongo.internal.expect.match.smart.MatchDataSmart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchJavaScript implements MatchDataSmart {

    private static final String PREFIX = "js-match:";
    private final ScriptEngine engine;
    private final Logger log = LoggerFactory.getLogger(MatchJavaScript.class);

    public MatchJavaScript() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("js");
    }

    @Override
    public boolean match(Object original, Object expected) {

        Object result = evaluate((String) expected, original);

        if (!(result instanceof Boolean)) {
            throw new InternalException(PREFIX + " must return a boolean value instead of {" + result + "}", 111);
        }

        return (boolean) result;
    }

    private Object evaluate(String script, Object value){
        try {
            String expectedValue = script.replaceFirst(PREFIX, "");
            engine.put("value", value);
            return engine.eval(expectedValue);
        } catch (Throwable e) {
            throw new InternalException("JS engine evaluate error", 112, e);
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
