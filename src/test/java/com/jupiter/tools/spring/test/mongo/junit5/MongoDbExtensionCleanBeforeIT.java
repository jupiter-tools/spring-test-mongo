package com.jupiter.tools.spring.test.mongo.junit5;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.internal.MongoDbTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 01.12.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith({MongoDbExtensionCleanBeforeIT.TestExtension.class, MongoDbExtension.class})
@EnableMongoDbTestContainers
class MongoDbExtensionCleanBeforeIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @MongoDataSet(cleanAfter = true)
    void name() {
        Bar simpleDoc = mongoTemplate.findById("55f3ed00b1375a48e61830bf", Bar.class);
        assertThat(simpleDoc).isNotNull();
    }

    @Test
    @MongoDataSet(cleanBefore = true)
    void cleanBefore() {

        Bar simpleDoc = mongoTemplate.findById("55f3ed00b1375a48e61830bf", Bar.class);
        assertThat(simpleDoc).isNull();

        assertThat(mongoTemplate.getCollectionNames().size()).isEqualTo(0);
    }

    public static class TestExtension implements Extension, BeforeEachCallback {

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            MongoTemplate mongoTemplate = SpringExtension.getApplicationContext(context)
                                                         .getBean(MongoTemplate.class);
            new MongoDbTest(mongoTemplate).importFrom("/dataset/simple_dataset.json");
        }
    }
}
