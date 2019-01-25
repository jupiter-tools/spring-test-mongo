package com.antkorwin.springtestmongo.internal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import com.antkorwin.springtestmongo.FooBar;
import com.antkorwin.springtestmongo.junit5.EnableMongoDbTestContainers;
import org.apache.commons.io.IOUtils;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnableMongoDbTestContainers
class MongoDbTestIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoDbTest mongoDbTest;

    @BeforeEach
    void setUp() {
        mongoDbTest = new MongoDbTest(mongoTemplate);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.getCollectionNames()
                     .forEach(mongoTemplate::dropCollection);
    }

    @Test
    void importTest() {
        // Act
        mongoDbTest.importFrom("/dataset/simple_collections_dataset.json");
        // Asserts
        assertThat(mongoTemplate.getCollectionNames()).contains("bar");
        assertThat(mongoTemplate.findAll(Bar.class))
                .extracting(Bar::getId, Bar::getData)
                .containsOnly(Tuple.tuple("55f3ed00b1375a48e618300a", "A"),
                              Tuple.tuple("55f3ed00b1375a48e618300b", "BB"),
                              Tuple.tuple("55f3ed00b1375a48e618300c", "CCC"));
    }

    @Test
    void importWithDateValues() {
        // Arrange
        Date before = new Date();
        Date plus3Days = new Date(before.getTime() + TimeUnit.DAYS.toMillis(3));
        // Act
        mongoDbTest.importFrom("/dataset/internal/dynamic/dynamic_with_dates_it.json");
        // Asserts
        List<Foo> foos = mongoTemplate.findAll(Foo.class);
        assertThat(foos).hasSize(2);

        assertThat(foos.get(0).getId()).isEqualTo("1");
        assertThat(foos.get(0).getTime()).isAfterOrEqualsTo(before);

        assertThat(foos.get(1).getId()).isEqualTo("2");
        assertThat(foos.get(1).getTime()).isAfterOrEqualsTo(plus3Days);
    }

    @Test
    void importWithGroovyDataValues() {
        // Arrange
        Date before = new Date();
        // Act
        mongoDbTest.importFrom("/dataset/internal/dynamic/dynamic_groovy_it.json");
        // Asserts
        List<Foo> foos = mongoTemplate.findAll(Foo.class);
        assertThat(foos).hasSize(1);
        assertThat(foos.get(0).getId()).isEqualTo("8");
        assertThat(foos.get(0).getTime()).isAfterOrEqualsTo(before);
        assertThat(foos.get(0).getCounter()).isEqualTo(55);
    }

    @Test
    void exportTest() throws IOException {
        // Arrange
        mongoTemplate.save(new Bar("111100001", "data-1"));
        mongoTemplate.save(new Bar("111100002", "data-2"));
        // Act
        mongoDbTest.exportTo("./target/export.json");
        // Asserts
        String result = getResultFromFile("./target/export.json");
        assertThat(result).isEqualTo(getExpectedResult());
    }

    @Test
    void expectedTest() {
        // Arrange
        mongoTemplate.save(new FooBar("1FB", "DATA", new Bar("1B", "B")));
        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            mongoDbTest.expect("/dataset/internal/expect_nested.json");
        });
    }

    private String getExpectedResult() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/dataset/internal/json_expected.json")) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    private String getResultFromFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

}