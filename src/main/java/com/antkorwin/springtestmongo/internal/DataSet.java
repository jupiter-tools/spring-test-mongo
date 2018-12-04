package com.antkorwin.springtestmongo.internal;

import java.util.List;
import java.util.Map;

/**
 * Created on 05.12.2018.
 *
 * @author Korovin Anatoliy
 */
public interface DataSet {

    Map<String, List<?>> read();
}
