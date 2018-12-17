package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

import com.antkorwin.springtestmongo.internal.DataSet;
import com.antkorwin.springtestmongo.internal.expect.ComplexityDataTypes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16.12.2018.
 *
 * Evaluate dynamic-values in the DataSet
 *
 * @author Korovin Anatoliy
 */
public class DynamicDataSet implements DataSet {

    private final DataSet dataSet;
    private final Set<DynamicValue> dynamicValueEvaluators;
    private final ComplexityDataTypes complexityDataTypes;
    private final ObjectMapper objectMapper;

    public DynamicDataSet(DataSet dataSet,
                          Set<DynamicValue> dynamicValueEvaluators) {
        this.dataSet = dataSet;
        this.dynamicValueEvaluators = dynamicValueEvaluators;
        this.complexityDataTypes = new ComplexityDataTypes();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, List<Map<String, Object>>> read() {

        Map<String, List<Map<String, Object>>> evaluatedDataSet = this.dataSet.read();

        for (List<Map<String, Object>> data : evaluatedDataSet.values()) {
            for (Map<String, Object> maps : data) {
                applyReplacerToMap(maps);
            }
        }

        return evaluatedDataSet;
    }

    private void applyReplacerToMap(Map<String, Object> maps) {
        for (String key : maps.keySet()) {
            Object value = maps.get(key);
            if (!this.complexityDataTypes.isComplexType(value)) {
                for (DynamicValue replacer : this.dynamicValueEvaluators) {
                    if (replacer.isNecessary(value)) {
                        maps.put(key, replacer.evaluate(value));
                    }
                }
            } else {
                Map<String, Object> map = this.objectMapper.convertValue(value, Map.class);
                applyReplacerToMap(map);
            }
        }
    }
}
