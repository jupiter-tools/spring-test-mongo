package com.antkorwin.springtestmongo.internal.expect;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.FooBar;
import com.antkorwin.springtestmongo.internal.TestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 08.12.2018.
 *
 * @author Korovin Anatoliy
 */
class ObjectMatcherTest {


    @Test
    void matchWithTheSame() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("id", "1",
                                                   "data", "data-101");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchWithThePartialSame() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("data", "data-101");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchWithTheDifferent() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("data", "not same");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
    }

    @Test
    void matchWithTheDifferentByOneField() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("id", "1",
                                                   "data", "not same");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
    }

    @Test
    void matchWithAnotherStructuredObject() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("id", "1",
                                                   "data", "data-101",
                                                   "field", "not_exists_in_origin");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
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
        ObjectMatcher matcher = new ObjectMatcher(fooBar);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchWithNestedEntityWithoutOneField() {
        Bar bar = new Bar("1B", "Bar-404");
        FooBar fooBar = new FooBar("1A", "FooBar", bar);

        Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                   "data", "FooBar",
                                                   "bar", ImmutableMap.of("data", "Bar-404"));
        // Act
        ObjectMatcher matcher = new ObjectMatcher(fooBar);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchNotEqualsWithNestedEntityWithoutOneField() {
        Bar bar = new Bar("1B", "Bar-404");
        FooBar fooBar = new FooBar("1A", "FooBar", bar);

        Map<String, Object> same = ImmutableMap.of("id", "1A",
                                                   "data", "FooBar",
                                                   "bar", ImmutableMap.of("data", "Bar-401"));
        // Act
        ObjectMatcher matcher = new ObjectMatcher(fooBar);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
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
        // Act
        ObjectMatcher matcher = new ObjectMatcher(bar1);
        // Asserts
        assertThat(matcher.match(bar2)).isTrue();
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
        // Act
        ObjectMatcher matcher = new ObjectMatcher(bar1);
        // Asserts
        assertThat(matcher.match(bar2)).isFalse();
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
        // Act
        ObjectMatcher matcher = new ObjectMatcher(bar1);
        // Asserts
        assertThat(matcher.match(bar2)).isFalse();
    }

    @Test
    void matchWithNullable() {

        Map<String, Object> first =
                new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(0);

        Map<String, Object> second =
                new TestData().read("/dataset/internal/expect/match_objects.json").get("test").get(1);

        assertThat(new ObjectMatcher(first).match(second)).isTrue();
    }

    @Nested
    class RegexTests {

        @Test
        void simpleRegularExpression() {
            // Arrange
            Bar original = new Bar("1", "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "1",
                                                       "data", "regex: ^data-...$");
            // Act
            ObjectMatcher matcher = new ObjectMatcher(original);
            // Asserts
            assertThat(matcher.match(same)).isTrue();
        }

        @Test
        void uuidRegularExpression() {
            // Arrange
            Bar original = new Bar(UUID.randomUUID().toString(), "data-101");
            Map<String, Object> same = ImmutableMap.of("id", "regex: [a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}",
                                                       "data", "regex: ^data-...$");
            // Act
            ObjectMatcher matcher = new ObjectMatcher(original);
            // Asserts
            assertThat(matcher.match(same)).isTrue();
        }
    }
}