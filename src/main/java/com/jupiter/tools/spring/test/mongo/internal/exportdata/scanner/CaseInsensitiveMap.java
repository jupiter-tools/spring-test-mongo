package com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner;


import java.util.HashMap;


/**
 * Created on 04.01.2019.
 *
 * This Map cast keys to lower case when you put something
 * and find by lowercase of the key when you get something from this map.
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

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(((String) key).toLowerCase());
    }
}