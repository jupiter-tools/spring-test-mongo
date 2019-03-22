package com.jupiter.tools.spring.test.mongo.junit4;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

/**
 * Test the behavior of the {@link MongoDbRule} with the use of {@link MongoDataSet} annotation
 *
 * @author Korovin Anatoliy
 */
public class MongoDbRuleTest {

    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);

    @Rule
    public final MongoDbRule rule = new MongoDbRule(() -> mongoTemplate);


    /**
     * Verification the sequence of calls in the mongoTemplate,
     * with the using a MongoDataSet annotation
     */
    @Test
    @MongoDataSet(value = "/dataset/simple_collections_dataset.json")
    public void testPopulateByMongoDataSetAnnotation() throws Exception {

        // Asserts
        ArgumentCaptor<Bar> captor = ArgumentCaptor.forClass(Bar.class);
        verify(mongoTemplate, times(3)).save(captor.capture());
        Assertions.assertThat(captor.getAllValues())
                  .isNotNull()
                  .extracting(Bar::getId, Bar::getData)
                  .containsOnly(Tuple.tuple("55f3ed00b1375a48e618300a", "A"),
                                Tuple.tuple("55f3ed00b1375a48e618300b", "BB"),
                                Tuple.tuple("55f3ed00b1375a48e618300c", "CCC"));
    }
}