package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

import com.antkorwin.springtestmongo.internal.DataSet;
import com.antkorwin.springtestmongo.internal.TestData;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 16.12.2018.
 *
 * @author Korovin Anatoliy
 */
class DynamicDataSetTest {

    @Test
    void replaceSimple() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic.json");
        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new SimpleDynamicValue()));
        Map<String, List<Map<String, Object>>> map = dynamicDataSet.read();
        // Asserts
        assertThat(map.get("test").get(0).get("first")).isEqualTo("{fixed}");
        assertThat(map.get("test").get(0).get("second")).isEqualTo("DATA");
    }

    @Test
    void replaceInNested() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic_nested.json");
        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new SimpleDynamicValue()));
        Map<String, List<Map<String, Object>>> map = dynamicDataSet.read();
        // Asserts
        assertThat(map.get("test").get(0).get("first")).isEqualTo("{fixed}");
        assertThat(((Map) (map.get("test").get(0).get("nested"))).get("second")).isEqualTo("{fixed}");
        assertThat(((Map) (((Map) (map.get("test").get(0).get("nested"))).get("nested"))).get("third"))
                .isEqualTo("{fixed}");
    }

    @Test
    void replaceGroovy() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic_groovy.json");
        Date now = new Date();
        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new GroovyDynamicValue()));
        Map<String, List<Map<String, Object>>> map = dynamicDataSet.read();
        // Asserts
        long date = (long) map.get("test").get(0).get("date");
        assertThat(date - now.getTime() < 10000).isTrue();

        int sum = (int) map.get("test").get(0).get("sum");
        assertThat(sum).isEqualTo(55);

        int eight = (int) map.get("test").get(0).get("2by4");
        assertThat(eight).isEqualTo(8);
    }

    @Test
    void multipleDynamicValueEvaluators() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/groovy_and_simple_values.json");
        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new GroovyDynamicValue(),
                                                                                    new SimpleDynamicValue()));
        Map<String, List<Map<String, Object>>> map = dynamicDataSet.read();
        // Asserts
        int sum = (int) map.get("test").get(0).get("groovy");
        assertThat(sum).isEqualTo(55);
        String fix = (String) map.get("test").get(0).get("simple");
        assertThat(fix).isEqualTo("{fixed}");
    }

    @Test
    void multipleDynamicValueInList() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/groovy_and_simple_in_list.json");

        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new GroovyDynamicValue(),
                                                                                    new SimpleDynamicValue()));
        Map<String, List<Map<String, Object>>> map = dynamicDataSet.read();

        // Asserts
        int sum = (int) ((List) map.get("test").get(0).get("array")).get(0);
        assertThat(sum).isEqualTo(55);

        int twoByFore = (int) ((List) map.get("test").get(0).get("array")).get(1);
        assertThat(twoByFore).isEqualTo(8);

        String fix = (String) map.get("test").get(0).get("simple");
        assertThat(fix).isEqualTo("{fixed}");
    }
}