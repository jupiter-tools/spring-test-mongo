package com.antkorwin.springtestmongo.junit5;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/**
 * Created on 01.12.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtension.class)
@EnableMongoDbTestContainers
class MongoDbExtensionImportIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Testing import to the current mongo database from a dataset defined in the MongoDataSet annotation
     */
    @Test
    @MongoDataSet(value = "/dataset/multidocument_dataset.json", cleanBefore = true, cleanAfter = true)
    void testImportByMongoDataSetAnnotation() throws Exception {
        // Act
        Foo fooDoc = mongoTemplate.findById("77f3ed00b1375a48e618300a", Foo.class);
        Bar simpleDoc = mongoTemplate.findById("55f3ed00b1375a48e618300b", Bar.class);

        // Assert
        Assertions.assertThat(fooDoc)
                  .isNotNull()
                  .extracting(Foo::getId, Foo::getCounter, Foo::getTime)
                  .containsOnly("77f3ed00b1375a48e618300a", 1, new Date(1516527720000L));

        Assertions.assertThat(simpleDoc)
                  .isNotNull()
                  .extracting(Bar::getId, Bar::getData)
                  .containsOnly("55f3ed00b1375a48e618300b", "BB");
    }
}
