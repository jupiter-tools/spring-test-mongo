package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.jupiter.tools.spring.test.mongo.internal.DataSet;
import com.jupiter.tools.spring.test.mongo.internal.TestData;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.JsonExport;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

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

        System.out.println(new JsonExport(dynamicDataSet).read());
    }

    @Test
    void dynamicWithListOfMap() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic_with_list_of_map.json");

        List array = Arrays.asList(ImmutableMap.of("value", 55),
                                   ImmutableMap.of("value", 8));

        DataSet expect = () ->
                ImmutableMap.of("test", Arrays.asList(ImmutableMap.of("array", array,
                                                                      "simple", "{fixed}")));
        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new GroovyDynamicValue(),
                                                                                    new SimpleDynamicValue()));
        // Asserts
        String dynamic = new JsonExport(dynamicDataSet).read();
        String expected = new JsonExport(expect).read();
        System.out.println(dynamic);
        assertThat(dynamic).isEqualTo(expected);
    }

    @Test
    void dynamicWithListOfList() {
        // Arrange
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic_with_list_of_list.json");

        List array = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2, 3, 5));

        DataSet expect = () ->
                ImmutableMap.of("test", Arrays.asList(ImmutableMap.of("array", array,
                                                                      "simple", "{fixed}")));

        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new GroovyDynamicValue(),
                                                                                    new SimpleDynamicValue()));
        // Asserts
        String dynamic = new JsonExport(dynamicDataSet).read();
        String expected = new JsonExport(expect).read();
        System.out.println(dynamic);
        assertThat(dynamic).isEqualTo(expected);
    }

    @Test
    void dateTime() {
        // Arrange
        Date before = new Date();
        DataSet dataSet = new TestData().jsonDataSet("/dataset/internal/dynamic/dynamic_with_dates.json");

        // Act
        DynamicDataSet dynamicDataSet = new DynamicDataSet(dataSet, Sets.newHashSet(new DateDynamicValue()));
        // Asserts
        Date now = (Date) dynamicDataSet.read().get("test").get(0).get("now");
        Date tomorrow = (Date) dynamicDataSet.read().get("test").get(0).get("tomorrow");

        assertThat(now).isAfterOrEqualsTo(before);
        assertThat(tomorrow).isAfterOrEqualsTo(new Date(before.getTime() + TimeUnit.MINUTES.toMillis(3)));

        String dynamic = new JsonExport(dynamicDataSet).read();
        System.out.println(dynamic);
    }
}