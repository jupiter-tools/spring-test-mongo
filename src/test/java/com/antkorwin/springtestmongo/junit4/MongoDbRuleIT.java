package com.antkorwin.springtestmongo.junit4;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Date;

/**
 * Created on 01.12.2018.
 *
 * Test a behavior of the {@link MongoDbRule} and the base abstract class {@link BaseMongoIT}
 * to write integration tests in old (junit4) style.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbRuleIT extends BaseMongoIT {

    /**
     * Testing populate mongo database from dataset defined in MongoDataSet annotation
     * Also check cleanAfter in the next test
     */
    @Test
    @MongoDataSet(value = "/dataset/multidocument_dataset.json", cleanBefore = true, cleanAfter = true)
    public void testPopulatingByMongoDataSet() throws Exception {
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
