package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

import com.jupiter.tools.spring.test.mongo.internal.DataSet;
import com.jupiter.tools.spring.test.mongo.internal.expect.ComplexityDataTypes;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16.12.2018.
 * <p>
 * Evaluate dynamic-values in the DataSet
 *
 * @author Korovin Anatoliy
 */
public class DynamicDataSet implements DataSet {

    private final DataSet dataSet;
    private final Set<DynamicValue> dynamicValueEvaluators;
    private final ComplexityDataTypes complexityDataTypes;

    public DynamicDataSet(DataSet dataSet,
                          Set<DynamicValue> dynamicValueEvaluators) {
        this.dataSet = dataSet;
        this.dynamicValueEvaluators = dynamicValueEvaluators;
        this.complexityDataTypes = new ComplexityDataTypes();
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
            if(value == null){
                continue;
            }
            if (!this.complexityDataTypes.isComplexType(value)) {
                for (DynamicValue replacer : this.dynamicValueEvaluators) {
                    if (replacer.isNecessary(value)) {
                        maps.put(key, replacer.evaluate(value));
                    }
                }
            } else {
                if (value instanceof Map) {
                    applyReplacerToMap((Map<String, Object>) value);
                } else if (value instanceof List) {
                    applyReplacerToList((List<Object>) value);
                }
            }
        }
    }

    private void applyReplacerToList(List<Object> listValues) {
        for (int i = 0; i < listValues.size(); i++) {
            Object value = listValues.get(i);
            if (value instanceof Map) {
                applyReplacerToMap((Map<String, Object>) value);
                continue;
            }
            if (value instanceof List) {
                applyReplacerToList((List<Object>) value);
                continue;
            }
            for (DynamicValue replacer : this.dynamicValueEvaluators) {
                if (replacer.isNecessary(value)) {
                    listValues.set(i, replacer.evaluate(value));
                }
            }
        }
    }
}
