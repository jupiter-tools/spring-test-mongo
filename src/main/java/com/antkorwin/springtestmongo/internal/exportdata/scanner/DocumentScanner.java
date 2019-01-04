package com.antkorwin.springtestmongo.internal.exportdata.scanner;

import java.util.Map;

/**
 * Created on 04.01.2019.
 *
 * Find all MongoDb Document collections with their classes.
 *
 * @author Korovin Anatoliy
 */
public interface DocumentScanner {

    Map<String, Class<?>> scan();
}
