package com.antkorwin.springtestmongo.internal.expect;

import com.antkorwin.springtestmongo.Bar;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

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
        Map<String, Object> same = ImmutableMap.of("id","1",
                                                   "data","data-101");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchWithThePartialSame() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("data","data-101");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isTrue();
    }

    @Test
    void matchWithTheDifferent() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("data","not same");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
    }

    @Test
    void matchWithTheDifferentByOneField() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("id","1",
                                                   "data","not same");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
    }

    @Test
    void matchWithAnotherStructuredObject() {
        // Arrange
        Bar original = new Bar("1", "data-101");
        Map<String, Object> same = ImmutableMap.of("id","1",
                                                   "data","data-101",
                                                   "field","not_exists_in_origin");
        // Act
        ObjectMatcher matcher = new ObjectMatcher(original);
        // Asserts
        assertThat(matcher.match(same)).isFalse();
    }
}