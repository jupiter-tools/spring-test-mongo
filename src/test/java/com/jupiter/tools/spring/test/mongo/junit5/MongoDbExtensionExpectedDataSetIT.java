package com.jupiter.tools.spring.test.mongo.junit5;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.annotation.ExpectedMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.idea.skip.test.IdeaSkipTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;
import static org.junit.platform.testkit.engine.EventConditions.*;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

/**
 * Created on 12.12.2018.
 *
 * @author Korovin Anatoliy
 */
@IdeaSkipTest
class MongoDbExtensionExpectedDataSetIT {

    @Test
    void testExpected() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpected");
        assertThat(summary.getTestsFailedCount()).isEqualTo(0);
    }


    @Test
    void testExpectWithNotSame() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWithNotSame");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);

        Throwable exception = summary.getFailures().get(0).getException();
        assertThat(exception).isInstanceOf(Error.class);
        assertThat(exception.getMessage()).contains("ExpectedDataSet of " + Bar.class.getCanonicalName())
                                          .contains("Not expected: \n{\"id\":\"111100002\",\"data\":\"data-3\"}")
                                          .contains("Expected but not found: \n{\"data\":\"data-2\"}");
    }

    @Test
    void testExpectWhenDbIsEmpty() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWhenDbIsEmpty");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);
    }

    @Test
    void testExpectWithMultipleCollections() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWithMultipleCollections");
        assertThat(summary.getTestsFailedCount()).isEqualTo(0);
    }

    @Test
    void testExpectWithMultipleCollectionsCombination() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWithMultipleCollectionsCombination");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);

        Throwable error = summary.getFailures().get(0).getException();
        assertThat(error).isInstanceOf(Error.class);
        assertThat(error.getMessage()).contains("ExpectedDataSet of " + Foo.class.getCanonicalName())
                                      .contains("Not expected: \n{\"id\":\"F2\",\"time\":12345002,\"counter\":3}")
                                      .contains("Expected but not found: \n{\"id\":\"F2\",\"counter\":2}");
    }

    @Test
    void testExpectWithMultipleCollectionsDoubleMatching() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWithMultipleCollectionsDoubleMatching");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);

        Throwable error = summary.getFailures().get(0).getException();
        assertThat(error).isInstanceOf(Error.class);
        assertThat(error.getMessage()).contains("ExpectedDataSet of " + Foo.class.getCanonicalName())
                                      .contains("Expected but not found: \n{\"counter\":2}");
    }

    @Test
    void testExpectNotExistsCollection() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "testExpectWithMultipleCollectionsDoubleMatching");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);
    }

    @Test
    void testReadOnlyFail() {
        TestExecutionSummary summary = runTestMethod(RealTests.class, "readOnlyFail");
        assertThat(summary.getTestsFailedCount()).isEqualTo(1);

        Throwable error = summary.getFailures().get(0).getException();
        assertThat(error.getMessage()).contains("Expected ReadOnly dataset, but found some modifications:");
        assertThat(error.getCause()).isInstanceOf(Error.class);
        assertThat(error.getCause().getMessage())
                .contains("Not expected: \n" +
                          "{\"id\":\"77f3ed00b1375a48e618300a\",\"time\":1516527720000,\"counter\":51187}")
                .contains("Expected but not found: \n" +
                          "{\"id\":\"77f3ed00b1375a48e618300a\",\"time\":1516527720000,\"counter\":1}");
    }

    private TestExecutionSummary runTestMethod(Class<?> testClass, String methodName) {

        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = request().selectors(selectMethod(testClass, methodName)).build();
        LauncherFactory.create().execute(request, listener);

        return listener.getSummary();
    }

    @Test
    void readOnlyFailTestInTheTestKitStyle() {
        String expectedErrorMessage = "Expected ReadOnly dataset, but found some modifications";
        String testMethod = "readOnlyFail";
        //Arrange
        EngineTestKit.engine("junit-jupiter")
                     .selectors(selectMethod(RealTests.class, testMethod))
                     // Act
                     .execute()
                     .testEvents()
                     // Assert
                     .assertStatistics(stats -> stats.started(1)
                                                     .succeeded(0)
                                                     .failed(1))
                     // Check error
                     .assertThatEvents()
                     .haveExactly(1,
                                  event(test(testMethod),
                                        finishedWithFailure(instanceOf(RuntimeException.class),
                                                            message(m -> m.contains(expectedErrorMessage)))));
    }


    @SpringBootTest
    @ExtendWith(SpringExtension.class)
    @ExtendWith(MongoDbExtension.class)
    @EnableMongoDbTestContainers
    @IdeaSkipTest
    static class RealTests {

        @Autowired
        private MongoTemplate mongoTemplate;

        @Test
        @MongoDataSet(cleanAfter = true, cleanBefore = true)
        @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
        void testExpected() {
            Bar bar1 = new Bar("111100001", "data-1");
            Bar bar2 = new Bar("111100002", "data-2");
            mongoTemplate.save(bar1);
            mongoTemplate.save(bar2);
        }

        @Test
        @MongoDataSet(cleanAfter = true, cleanBefore = true)
        @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
        void testExpectWithNotSame() {
            Bar bar1 = new Bar("111100001", "data-1");
            Bar bar2 = new Bar("111100002", "data-3");
            mongoTemplate.save(bar1);
            mongoTemplate.save(bar2);
        }

        @Test
        @MongoDataSet(cleanAfter = true, cleanBefore = true)
        @ExpectedMongoDataSet("/dataset/internal/expected_dataset.json")
        void testExpectWhenDbIsEmpty() {
            // nope
        }


        @Test
        @MongoDataSet(cleanAfter = true, cleanBefore = true)
        @ExpectedMongoDataSet("/dataset/internal/expected_dataset_multiple.json")
        void testExpectWithMultipleCollections() {
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
        }

        @Test
        @MongoDataSet(cleanAfter = true, cleanBefore = true)
        @ExpectedMongoDataSet("/dataset/internal/expected_dataset_double_matching.json")
        void testExpectWithMultipleCollectionsDoubleMatching() {
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
        void testExpectNotExistsCollection() {
            Bar bar1 = new Bar("111100001", "data-1");
            Bar bar2 = new Bar("111100002", "data-2");
            mongoTemplate.save(bar1);
            mongoTemplate.save(bar2);
        }

        @Test
        @MongoDataSet(value = "/dataset/multidocument_dataset.json",
                      readOnly = true,  // ASSERT THIS
                      cleanBefore = true,
                      cleanAfter = true)
        void readOnlyFail() {
            Foo fooDoc = mongoTemplate.findById("77f3ed00b1375a48e618300a", Foo.class);
            fooDoc.setCounter(51187);
            mongoTemplate.save(fooDoc);
        }
    }
}
