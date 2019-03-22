package com.jupiter.tools.spring.test.mongo.junit5.meta.annotation;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * Run a test with the configuration by {@link MongoDbDataTest}.
 * <p>
 * Using this annotation will disable full auto-configuration and instead apply only
 * configuration relevant to MongoDB tests.
 * <p>
 * @author Korovin Anatoliy
 */
@MongoDbDataTest
class MongoDbDataTestAnnotationIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Testing populate mongo database from dataset defined in MongoDataSet annotation
     */
    @Test
    @MongoDataSet(value = "/dataset/multidocument_dataset.json", cleanBefore = true, cleanAfter = true)
    void testPopulatingByMongoDataSet() throws Exception {
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
