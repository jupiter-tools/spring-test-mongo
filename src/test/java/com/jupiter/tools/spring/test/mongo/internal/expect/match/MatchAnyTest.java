package com.jupiter.tools.spring.test.mongo.internal.expect.match;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.FooBar;
import com.jupiter.tools.spring.test.mongo.internal.TestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 18.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchAnyTest {

    @Nested
    class SimpleTests {

        @Test
        void matchWithTheSame() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "1",
                                                       "data", "data-101");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isTrue();
        }

        @Test
        void matchWithThePartialSame() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("data", "data-101");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isTrue();
        }

        @Test
        void matchWithTheDifferent() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("data", "not same");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isFalse();
        }

        @Test
        void matchWithTheDifferentByOneField() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "1",
                                                       "data", "not same");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isFalse();
        }

        @Test
        void matchWithAnotherStructuredObject() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "1",
                                                       "data", "data-101",
                                                       "field", "not_exists_in_origin");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isFalse();
        }

        @Test
        void matchWithDifferentTypesOfField() {
            // Arrange
            Bar original = new Bar("1", "1100");
            Map<String, Object> notSame = ImmutableMap.of("data", 1100);
            // Act & Asserts
            assertThat(new MatchAny().match(original, notSame)).isFalse();
        }
    }

    @Nested
    class NestedObjectTests {

        @Test
        void matchWithNested() {
            Bar bar = new Bar("1B", "Bar-404");
            FooBar fooBar = new FooBar("1A", "FooBar", bar);

            Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                       "data", "FooBar",
                                                       "bar", ImmutableMap.of("id", "1B",
                                                                              "data", "Bar-404"));
            // Act
            MatchAny matcher = new MatchAny();
            // Asserts
            assertThat(matcher.match(fooBar, same)).isTrue();
        }

        @Test
        void matchWithNestedEntityWithoutOneField() {
            Bar bar = new Bar("1B", "Bar-404");
            FooBar fooBar = new FooBar("1A", "FooBar", bar);

            Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                       "data", "FooBar",
                                                       "bar", ImmutableMap.of("data", "Bar-404"));
            // Act & Asserts
            assertThat(new MatchAny().match(fooBar, same)).isTrue();
        }

        @Test
        void matchNotEqualsWithNestedEntityWithoutOneField() {
            Bar bar = new Bar("1B", "Bar-404");
            FooBar fooBar = new FooBar("1A", "FooBar", bar);

            Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                       "data", "FooBar",
                                                       "bar", ImmutableMap.of("data", "Bar-401"));
            // Act & Asserts
            assertThat(new MatchAny().match(fooBar, same)).isFalse();
        }

        @Test
        void matchEqualsWithDoubleLevelOfNestedEntity() {

            Map<String, Object> secondNested1 = ImmutableMap.of("second", "222",
                                                                "unexpected", "wow");
            Map<String, Object> firstNested1 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested1);
            Map<String, Object> bar1 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested1);

            Map<String, Object> secondNested2 = ImmutableMap.of("second", "222");
            Map<String, Object> firstNested2 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested2);
            Map<String, Object> bar2 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested2);
            // Act & Asserts
            assertThat(new MatchAny().match(bar1, bar2)).isTrue();
        }

        @Test
        void matchNotEqualsWithDoubleLevelOfNestedEntity() {

            Map<String, Object> secondNested1 = ImmutableMap.of("second", "222");
            Map<String, Object> firstNested1 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested1);
            Map<String, Object> bar1 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested1);

            Map<String, Object> secondNested2 = ImmutableMap.of("second", "BBB");
            Map<String, Object> firstNested2 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested2);
            Map<String, Object> bar2 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested2);
            // Act & Asserts
            assertThat(new MatchAny().match(bar1, bar2)).isFalse();
        }

        @Test
        void matchNotEqualsWithDoubleLevelOfNestedEntityAndDifferentType() {

            Map<String, Object> secondNested1 = ImmutableMap.of("second", 222);
            Map<String, Object> firstNested1 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested1);
            Map<String, Object> bar1 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested1);

            Map<String, Object> secondNested2 = ImmutableMap.of("second", 223);
            Map<String, Object> firstNested2 = ImmutableMap.of("first", "111",
                                                               "secondNested", secondNested2);
            Map<String, Object> bar2 = ImmutableMap.of("id", "1B",
                                                       "data", "Bar",
                                                       "firstNested", firstNested2);
            // Act & Asserts
            assertThat(new MatchAny().match(bar1, bar2)).isFalse();
        }
    }

    @Nested
    class RegexTests {

        @Test
        void simpleRegularExpression() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "1",
                                                       "data", "regex: ^data-...$");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isTrue();
        }

        @Test
        void uuidRegularExpression() {
            // Arrange
            String UUID_REGEXP = "regex: [a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";

            Bar original = new Bar(UUID.randomUUID().toString(), "data-101");
            Map<String, Object> same = ImmutableMap.of("id", UUID_REGEXP,
                                                       "data", "regex: ^data-...$");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isTrue();
        }

        @Test
        void uuidRegularExpressionNotMatch() {
            // Arrange
            String UUID_REGEXP = "regex: [a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";

            Bar original = new Bar("123", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", UUID_REGEXP,
                                                       "data", "regex: ^data-...$");
            // Act & Asserts
            assertThat(new MatchAny().match(original, same)).isFalse();
        }
    }

    @Nested
    class ComplexTests {

        @Test
        void matchWithNullable() {

            Map<String, Object> first =
                    new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(0);

            Map<String, Object> second =
                    new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(1);

            assertThat(new MatchAny().match(first, second)).isTrue();
        }
    }

    @Nested
    class NestedMapTests {

        @Test
        void matchMap() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/match_objects.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(1);
            // Act & Asserts
            assertThat(new MatchAny().match(first, second)).isTrue();
        }
    }

    @Nested
    class NestedArraysTests {

        @Test
        void matchNestedArray() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(1);
            // Act
            assertThat(new MatchAny().match(first, second)).isTrue();
        }

        @Test
        void matchNestedArrayNotEquals() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(2);
            // Act
            assertThat(new MatchAny().match(first, second)).isFalse();
        }

        @Test
        void matchNestedArrayNotSameLength() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_array.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(3);
            // Act
            assertThat(new MatchAny().match(first, second)).isFalse();
        }

        @Test
        void matchArraysOfFloat() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_arrays_of_float.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(1);
            // Act
            assertThat(new MatchAny().match(first, second)).isTrue();
        }

        @Test
        void matchNotSameArraysOfFloat() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_arrays_of_float.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(0);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(2);
            // Act
            assertThat(new MatchAny().match(first, second)).isFalse();
        }

        @Test
        void matchNotSameArraysOfFloatWithSingleValue() {
            // Arrange
            String dataSetFilePath = "/dataset/internal/expect/expect_with_nested_arrays_of_float.json";
            Map<String, Object> first = new TestData().read(dataSetFilePath).get("test").get(4);
            Map<String, Object> second = new TestData().read(dataSetFilePath).get("test").get(5);
            // Act
            assertThat(new MatchAny().match(first, second)).isTrue();
        }
    }

    @Nested
    class DifferentTypes {

        @Test
        void intVsLong() {
            ImmutableMap<String, Integer> intVal = ImmutableMap.of("value", 123);
            ImmutableMap<String, Long> longVal = ImmutableMap.of("value", 123L);
            // Act & Asserts
            assertThat(new MatchAny().match(intVal, longVal)).isTrue();
        }
    }

    @Nested
    class DateMatchTests {

        @Test
        void now() {
            // Arrange
            Map<String, Object> actual = ImmutableMap.of("time", new Date());
            Map<String, Object> expected = ImmutableMap.of("time", "date-match:[NOW]");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isTrue();
        }

        @Test
        void plusOneDay() {
            // Arrange
            Date tomorrow = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(1));
            Map<String, Object> actual = ImmutableMap.of("time", tomorrow);
            Map<String, Object> expected = ImmutableMap.of("time", "date-match:[NOW]+1(DAYS)");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isTrue();
        }

        @Test
        void notSame() {
            // Arrange
            Date tomorrow = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(1));
            Map<String, Object> actual = ImmutableMap.of("time", tomorrow);
            Map<String, Object> expected = ImmutableMap.of("time", "date-match:[NOW]");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isFalse();
        }

        @Test
        void minusOneDay() {
            // Arrange
            Date yesterday = new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1));
            Map<String, Object> actual = ImmutableMap.of("time", yesterday);
            Map<String, Object> expected = ImmutableMap.of("time", "date-match:[NOW]-1(DAYS)");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isTrue();
        }
    }

    @Nested
    class GroovyMatchTests {

        @Test
        void minusOneDay() {
            // Arrange
            Map<String, Object> actual = ImmutableMap.of("sum", 55);
            Map<String, Object> expected = ImmutableMap.of("sum", "groovy-match: value == (1..10).sum()");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isTrue();
        }
    }

    @Nested
    class JavaScriptMatchTests {

        @Test
        void evenNumber() {
            // Arrange
            Map<String, Object> actual = ImmutableMap.of("sum", 32);
            Map<String, Object> expected = ImmutableMap.of("sum", "js-match: value % 2 == 0");
            // Act & Asserts
            assertThat(new MatchAny().match(actual, expected)).isTrue();
        }
    }
}