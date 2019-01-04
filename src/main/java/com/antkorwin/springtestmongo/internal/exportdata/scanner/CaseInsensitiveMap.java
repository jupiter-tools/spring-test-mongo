package com.antkorwin.springtestmongo.internal.exportdata.scanner;


import java.util.HashMap;


/**
 * Created on 04.01.2019.
 *
 * @author Korovin Anatoliy
 */
public class CaseInsensitiveMap extends HashMap<String, Class<?>> {

    @Override
    public Class<?> put(String key, Class<?> value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public Class<?> get(Object key) {
        return super.get(((String) key).toLowerCase());
    }
}