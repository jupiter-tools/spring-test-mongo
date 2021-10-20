package com.jupiter.tools.spring.test.mongo.internal;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.FloatHolder;
import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.junit5.EnableMongoDbTestContainers;
import com.jupiter.tools.spring.test.mongo.junit5.MongoDbExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 08.12.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtension.class)
@EnableMongoDbTestContainers
class MongoDbTestExpectedIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectAfterCommitInTest() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        // Act
        Assertions.assertDoesNotThrow(() -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset.json");
        });
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectWithDifferentCountOfEntities() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        mongoTemplate.save(bar1);
        // Act
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset.json");
        });
        assertThat(error.getMessage())
                .contains("expected 2 but found 1 - com.jupiter.tools.spring.test.mongo.Bar entities");
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectWithNotSame() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-3");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        // Act
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset.json");
        });
        assertThat(error.getMessage()).contains("ExpectedDataSet of " + Bar.class.getCanonicalName())
                                      .contains("Not expected: \n{\"id\":\"111100002\",\"data\":\"data-3\"}")
                                      .contains("Expected but not found: \n{\"data\":\"data-2\"}");
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testWithEmptyDataBaseAndNotEmptyDataSet() {
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset.json");
        });
        assertThat(error.getMessage()).contains("expected 2 but found 0 - com.jupiter.tools.spring.test.mongo.Bar entities");
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectWithMultipleCollections() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        Foo foo1 = new Foo("F1", new Date(), 1);
        Foo foo2 = new Foo("F2", new Date(), 2);
        Foo foo3 = new Foo("F3", new Date(), 3);
        mongoTemplate.save(foo1);
        mongoTemplate.save(foo2);
        mongoTemplate.save(foo3);
        // Act
        Assertions.assertDoesNotThrow(() -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset_multiple.json");
        });
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectWithMultipleCollectionsCombination() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        Foo foo1 = new Foo("F1", new Date(12345001), 1);
        Foo foo2 = new Foo("F2", new Date(12345002), 3);
        Foo foo3 = new Foo("F3", new Date(12345003), 3);
        mongoTemplate.save(foo1);
        mongoTemplate.save(foo2);
        mongoTemplate.save(foo3);
        // Act
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset_multiple.json");
        });

        assertThat(error.getMessage()).contains("ExpectedDataSet of " + Foo.class.getCanonicalName())
                                      .contains("Not expected: \n{\"id\":\"F2\",\"time\":12345002,\"counter\":3}")
                                      .contains("Expected but not found: \n{\"id\":\"F2\",\"counter\":2}");
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectWithMultipleCollectionsDoubleMatching() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        Bar bar3 = new Bar("111100003", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        mongoTemplate.save(bar3);
        Foo foo1 = new Foo("F1", new Date(), 1);
        Foo foo2 = new Foo("F2", new Date(), 3);
        Foo foo3 = new Foo("F3", new Date(), 3);
        Foo foo4 = new Foo("F4", new Date(), 4);
        Foo foo5 = new Foo("F5", new Date(), 3);
        mongoTemplate.save(foo1);
        mongoTemplate.save(foo2);
        mongoTemplate.save(foo3);
        mongoTemplate.save(foo4);
        mongoTemplate.save(foo5);
        // Act
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset_double_matching.json");
        });
        System.out.println(error.getMessage());
        assertThat(error.getMessage()).contains("ExpectedDataSet of " + Foo.class.getCanonicalName())
                                      .contains("Expected but not found: \n{\"counter\":2}");
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    void testExpectNotExistsCollection() {
        // Arrange
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
        // Act
        Error error = Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset_not_exists_collection.json");
        });

        assertThat(error.getMessage()).contains("Not equal document collections")
                                      .contains("expected but not found:\n[com.jupiter.tools.spring.test.mongo.Boo]");
    }

    @Nested
    @SpringBootTest
    @ExtendWith(SpringExtension.class)
    @ExtendWith(MongoDbExtension.class)
    @EnableMongoDbTestContainers
    class RegexTests {

        @Autowired
        private MongoTemplate mongoTemplate;

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void foundMatch() {
            // Arrange
            Bar bar1 = new Bar(UUID.randomUUID().toString(), "data-1");
            mongoTemplate.save(bar1);
            // Act
            Assertions.assertDoesNotThrow(() -> {
                new MongoDbTest(mongoTemplate).expect("/dataset/internal/expect/regex_test.json");
            });
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void notFoundMatch() {
            // Arrange
            Bar bar1 = new Bar("12345", "data-1");
            mongoTemplate.save(bar1);
            // Act
            Error error = Assertions.assertThrows(Error.class, () -> {
                new MongoDbTest(mongoTemplate).expect("/dataset/internal/expect/regex_test.json");
            });

            assertThat(error.getMessage()).contains("ExpectedDataSet of com.jupiter.tools.spring.test.mongo.Bar")
                                          .contains("Not expected: \n" +
                                                    "{\"id\":\"12345\",\"data\":\"data-1\"}")
                                          .contains("Expected but not found: \n" +
                                                    "{\"id\":\"regex: [a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\",\"data\":\"data-1\"}");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void multipleRegexp() {
            // Arrange
            Bar bar1 = new Bar("1", "data-1");
            Bar bar2 = new Bar("2", "data-2");
            Bar bar3 = new Bar("3", "data-3");
            Bar bar4 = new Bar("4", "data-4");
            mongoTemplate.save(bar1);
            mongoTemplate.save(bar2);
            mongoTemplate.save(bar3);
            mongoTemplate.save(bar4);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expect/multiple_regex.json");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void numberFieldRegexp() {
            // Arrange
            Foo foo = new Foo("1", new Date(), 1);
            mongoTemplate.save(foo);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expect/number_field_regex.json");
        }
    }

    @Nested
    @SpringBootTest
    @ExtendWith(SpringExtension.class)
    @ExtendWith(MongoDbExtension.class)
    @EnableMongoDbTestContainers
    class DynamicDataGroovyTest {

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void expectGroovy() {
            // Arrange
            Bar bar1 = new Bar("1", "3");
            Bar bar2 = new Bar("2", "55");
            mongoTemplate.save(bar1);
            mongoTemplate.save(bar2);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/dynamic/expect_with_groovy.json");
        }
    }

    @Nested
    @SpringBootTest
    @ExtendWith(SpringExtension.class)
    @ExtendWith(MongoDbExtension.class)
    @EnableMongoDbTestContainers
    class SmartDataMatchingTests {

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void matchGroovy() {
            // Arrange
            Bar bar1 = new Bar("1", "55");
            mongoTemplate.save(bar1);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/dynamic/groovy_match.json");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void matchJavaScript() {
            // Arrange
            Bar bar = new Bar("1", "baNaNa");
            mongoTemplate.save(bar);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/dynamic/js_match.json");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void matchDate() {
            Foo foo = new Foo("1", new Date(), 123);
            mongoTemplate.save(foo);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/dynamic/time_match.json");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void dateTimeNow() {
            // Arrange
            Foo foo1 = new Foo("1", new Date(), 1);
            Foo foo2 = new Foo("2", new Date(new Date().getTime() + TimeUnit.MINUTES.toMillis(3)), 2);
            mongoTemplate.save(foo1);
            mongoTemplate.save(foo2);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/dynamic/expect_with_dates.json");
        }

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void wrongDateTime() {
            // Arrange
            Foo foo1 = new Foo("1", new Date(), 1);
            Foo foo2 = new Foo("2", new Date(), 2);
            mongoTemplate.save(foo1);
            mongoTemplate.save(foo2);
            MongoDbTest mongoDbTest = new MongoDbTest(mongoTemplate);
            // Act
            Assertions.assertThrows(Error.class,
                                    () -> mongoDbTest.expect("/dataset/internal/dynamic/expect_with_dates.json"));
        }
    }

    @Nested
    @SpringBootTest
    @ExtendWith(SpringExtension.class)
    @ExtendWith(MongoDbExtension.class)
    @EnableMongoDbTestContainers
    class NestedArraysOfFloatTests {

        @Test
        @MongoDataSet(cleanBefore = true, cleanAfter = true)
        void equalArraysOfFloat() {
            // Arrange
            FloatHolder firstHolder = new FloatHolder("first", Arrays.asList(1.3E+2F));
            FloatHolder secondHolder = new FloatHolder("second", Arrays.asList(1.3E+5F, 0.3E-7F));

            mongoTemplate.save(firstHolder);
            mongoTemplate.save(secondHolder);
            // Act
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expect/expect_with_float_array_holder.json");
        }
    }
}
