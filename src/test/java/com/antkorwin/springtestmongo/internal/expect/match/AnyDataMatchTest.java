package com.antkorwin.springtestmongo.internal.expect.match;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.FooBar;
import com.antkorwin.springtestmongo.internal.TestData;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 18.12.2018.
 * <p>
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
class AnyDataMatchTest {

    @Test
    void matchMap() {
        Map<String, Object> first =
                new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(0);

        Map<String, Object> second =
                new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(1);

        assertThat(new AnyDataMatch().match(first, second)).isTrue();
    }

    @Test
    void matchWithNested() {
        Bar bar = new Bar("1B", "Bar-404");
        FooBar fooBar = new FooBar("1A", "FooBar", bar);

        Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                   "data", "FooBar",
                                                   "bar", ImmutableMap.of("id", "1B",
                                                                          "data", "Bar-404"));
        // Act
        AnyDataMatch matcher = new AnyDataMatch();
        // Asserts
        assertThat(matcher.match(fooBar, same)).isTrue();
    }

    @Test
    void matchNestedArray() {
        // Arrange
        String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
        Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
        Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(1);
        // Act
        assertThat(new AnyDataMatch().match(first, second)).isTrue();
    }

    @Test
    void matchNestedArrayNotEquals() {
        // Arrange
        String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
        Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
        Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(2);
        // Act
        assertThat(new AnyDataMatch().match(first, second)).isFalse();
    }

    @Test
    void matchNestedArrayNotSameLength() {
        // Arrange
        String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
        Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
        Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(3);
        // Act
        assertThat(new AnyDataMatch().match(first, second)).isFalse();
    }

}