package com.jupiter.tools.spring.test.mongo.junit4;

import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.collections.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

/**
 * Created by Korovin A. on 23.01.2018.
 *
 * Testing the clean-after logic of the {@link MongoDbRule}
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRuleCleanAfterTest {

    private static final MongoTemplate mongoTemplate = mock(MongoTemplate.class);

    @Rule
    public final MongoDbRule rule = new MongoDbRule(() -> mongoTemplate);


    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {

        @Override
        protected void after() {
            // Asserts
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(mongoTemplate, times(2)).dropCollection(captor.capture());
            Assertions.assertThat(captor.getAllValues())
                      .hasSize(2)
                      .containsOnly("Foo", "Bar");
        }
    };

    @Test
    @MongoDataSet(cleanAfter = true)
    public void testCleanAfter() throws Exception {
        // Arrange
        when(mongoTemplate.getCollectionNames()).thenReturn(Sets.newSet("Foo", "Bar"));
    }
}