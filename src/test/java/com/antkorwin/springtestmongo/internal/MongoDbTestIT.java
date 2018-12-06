package com.antkorwin.springtestmongo.internal;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.junit5.EnableMongoDbTestContainers;
import org.apache.commons.io.IOUtils;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
    void exportTest() throws IOException {
        // Arrange
        mongoTemplate.save(new Bar("101","data-1"));
        mongoTemplate.save(new Bar("102","data-2"));
        mongoTemplate.save(new Bar("103","data-3"));
        // Act
        mongoDbTest.exportTo("./target/export.json");
        // Asserts
        String result = getResultFromFile("./target/export.json");
        assertThat(result).isEqualTo("{\n" +
                                     "  \"com.antkorwin.springtestmongo.Bar\" : [ {\n" +
                                     "    \"id\" : \"101\",\n" +
                                     "    \"data\" : \"data-1\"\n" +
                                     "  }, {\n" +
                                     "    \"id\" : \"102\",\n" +
                                     "    \"data\" : \"data-2\"\n" +
                                     "  }, {\n" +
                                     "    \"id\" : \"103\",\n" +
                                     "    \"data\" : \"data-3\"\n" +
                                     "  } ]\n" +
                                     "}");
    }

    private String getResultFromFile(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}