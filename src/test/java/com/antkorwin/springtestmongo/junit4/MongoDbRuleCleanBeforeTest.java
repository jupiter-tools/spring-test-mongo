package com.antkorwin.springtestmongo.junit4;

import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.collections.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Korovin A. on 23.01.2018.
 *
 * Testing the clean-before logic of the {@link MongoDbRule}
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRuleCleanBeforeTest {

    private static final MongoTemplate mongoTemplate = mock(MongoTemplate.class);

    @Rule
    public final MongoDbRule rule = new MongoDbRule(() -> mongoTemplate);

    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            // Arrange
            when(mongoTemplate.getCollectionNames()).thenReturn(Sets.newSet("Foo", "Bar"));
        }
    };


    /**
     * check the dropping all exist collections before run test
     */
    @Test
    @MongoDataSet(cleanBefore = true)
    public void testCleanBefore() throws Exception {
        // Asserts
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mongoTemplate, times(2)).dropCollection(captor.capture());
        Assertions.assertThat(captor.getAllValues())
                  .hasSize(2)
                  .containsOnly("Foo", "Bar");
    }
}