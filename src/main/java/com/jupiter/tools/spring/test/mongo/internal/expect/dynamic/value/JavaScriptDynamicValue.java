package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 16.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class JavaScriptDynamicValue implements DynamicValue {

    private final ScriptEngine engine;
    private final Logger log = LoggerFactory.getLogger(JavaScriptDynamicValue.class);

    public JavaScriptDynamicValue() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("js");
    }

    @Override
    public boolean isNecessary(Object value) {
        return value instanceof String &&
               ((String) value).startsWith("js:");
    }

    @Override
    public Object evaluate(Object value) {
        try {
            return engine.eval((String) value);
        } catch (ScriptException e) {
            log.error("JavaScript engine evaluate error: ", e);
            throw new InternalException("JavaScript engine evaluate error", 110, e);
        }
    }
}
