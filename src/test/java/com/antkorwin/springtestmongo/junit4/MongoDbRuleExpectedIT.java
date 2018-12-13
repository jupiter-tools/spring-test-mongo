package com.antkorwin.springtestmongo.junit4;

import java.util.Date;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import com.antkorwin.springtestmongo.annotation.ExpectedMongoDataSet;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.junit.Ignore;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created on 12.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
@Ignore("TODO: find a way to test JUnit4 rules which throws an exception.")
public class MongoDbRuleExpectedIT extends BaseMongoIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
    public void testExpectAfterCommitInTest() {
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
    public void testExpectWithNotSame() {
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-3");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
    public void testWrongDataExpect() {
        // nope
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset_multiple.json")
    public void testExpectWithMultipleCollections() {
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
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset_multiple.json")
    public void testExpectWithMultipleCollectionsCombination() {
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
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset_double_matching.json")
    public void testExpectWithMultipleCollectionsDoubleMatching() {
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
    }

    @Test
    @MongoDataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedMongoDataSet("/dataset/internal/expected_dataset_multiple.json")
    public void testExpectNotExistsCollection() {
        Bar bar1 = new Bar("111100001", "data-1");
        Bar bar2 = new Bar("111100002", "data-2");
        mongoTemplate.save(bar1);
        mongoTemplate.save(bar2);
    }
}
