package com.antkorwin.springtestmongo.internal;

import java.util.Date;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import com.antkorwin.springtestmongo.internal.MongoDbTest;
import com.antkorwin.springtestmongo.junit5.EnableMongoDbTestContainers;
import com.antkorwin.springtestmongo.junit5.MongoDbExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    void testWrongDataExpect() {
        Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset.json");
        });
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
        Assertions.assertThrows(Error.class, () -> {
            new MongoDbTest(mongoTemplate).expect("/dataset/internal/expected_dataset_multiple.json");
        });
    }
}
