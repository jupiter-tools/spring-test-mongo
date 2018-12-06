package com.antkorwin.springtestmongo.internal;

import java.util.List;
import java.util.Map;

interface DataSet {

    Map<String, List<?>> read();

}