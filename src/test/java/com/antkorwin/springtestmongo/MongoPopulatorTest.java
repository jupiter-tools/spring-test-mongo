package com.antkorwin.springtestmongo;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.GuardCheck;
import com.antkorwin.springtestmongo.errorinfo.RiderErrorInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created on 01.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MongoPopulatorTest {

    /**
     * check the populating of one record with a simple entity
     */
    @Test
    void testPopulateDocumentWithOneRecordDataSet() throws Exception {
        // Arrange
        ArgumentCaptor<Bar> captor = ArgumentCaptor.forClass(Bar.class);
        MongoTemplate template = mock(MongoTemplate.class);

        // Act
        MongoPopulator.populate(template, "/dataset/simple_dataset.json");

        // Assert
        verify(template).save(captor.capture());
        Assertions.assertThat(captor.getValue())
                  .isNotNull()
                  .extracting(Bar::getId, Bar::getData)
                  .containsOnly("55f3ed00b1375a48e61830bf", "TEST");
    }

    /**
     * check the populating of the collection of documents with simple entities
     */
    @Test
    void testPopulateCollectionOfDocuments() throws Exception {
        // Arrange
        ArgumentCaptor<Bar> captor = ArgumentCaptor.forClass(Bar.class);
        MongoTemplate template = mock(MongoTemplate.class);

        // Act
        MongoPopulator.populate(template, "/dataset/simple_collections_dataset.json");

        // Assert
        verify(template, times(3)).save(captor.capture());
        Assertions.assertThat(captor.getAllValues())
                  .isNotNull()
                  .extracting(Bar::getId, Bar::getData)
                  .containsOnly(Tuple.tuple("55f3ed00b1375a48e618300a", "A"),
                                Tuple.tuple("55f3ed00b1375a48e618300b", "BB"),
                                Tuple.tuple("55f3ed00b1375a48e618300c", "CCC"));
    }


    /**
     * Check the populating of the collection with multiple document types
     */
    @Test
    void testMultipleDocumentPopulating() throws Exception {
        // Arrange
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        MongoTemplate template = mock(MongoTemplate.class);
        // Act
        MongoPopulator.populate(template, "/dataset/multidocument_dataset.json");
        // Assert
        verify(template, times(3)).save(captor.capture());

        Assertions.assertThat(captor.getAllValues().get(0))
                  .extracting("id", "data", "class")
                  .containsOnly("55f3ed00b1375a48e618300a", "A", Bar.class);

        Assertions.assertThat(captor.getAllValues().get(1))
                  .extracting("id", "data", "class")
                  .containsOnly("55f3ed00b1375a48e618300b", "BB", Bar.class);

        Assertions.assertThat(captor.getAllValues().get(2))
                  .extracting("id", "time", "counter", "class")
                  .containsOnly("77f3ed00b1375a48e618300a", new Date(1516527720000L), 1, Foo.class);
    }

    /**
     * The trying to populate with a not exist data file
     * will throw an internal exception (FILE_NOT_FOUND)
     */
    @Test
    void testWithWrongResourceFile() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "unexist"),
                         InternalException.class,
                         RiderErrorInfo.FILE_NOT_FOUND);
    }

    /**
     * Check the trying to populate with the empty data file.
     */
    @Test
    void testWithEmptyFile() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "/dataset/emptyfile.json"),
                         InternalException.class,
                         RiderErrorInfo.DATASET_PARSING_ERROR);
    }

    /**
     * Check the trying to populate a wrong data set (not supported json format).
     */
    @Test
    void testWithWrongDataSetFormat() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "/dataset/wrong_format.json"),
                         InternalException.class,
                         RiderErrorInfo.DATASET_PARSING_ERROR);
    }

    /**
     * Check the trying to populate an undefined document class type.
     */
    @Test
    void testWithWrongDocumentCollectionType() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "/dataset/wrong_collection_type.json"),
                         InternalException.class,
                         RiderErrorInfo.UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE);
    }

    /**
     * Check the trying to populate with unsupported format of entity.
     * Not found document collection for one type.
     */
    @Test
    void testWithWrongDataFormat() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "/dataset/wrong_dataformat.json"),
                         InternalException.class,
                         RiderErrorInfo.DATASET_FORMAT_ERROR);
    }

    /**
     * Check the trying to populate two records
     * with wrong format of the second record.
     */
    @Test
    void testWithWrongDataInSecondRecord() throws Exception {
        // Arrange
        MongoTemplate template = mock(MongoTemplate.class);
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(template, "/dataset/wrong_dataformat_in_record.json"),
                         InternalException.class,
                         RiderErrorInfo.DOCUMENT_RECORD_PARSING_ERROR);
    }

    /**
     * Check the trying to populate a dataset without MongoTemplate.
     */
    @Test
    void testWithoutMongoTemplate() throws Exception {
        // Act & Assert
        GuardCheck.check(() -> MongoPopulator.populate(null, "123"),
                         InternalException.class,
                         RiderErrorInfo.MONGO_TEMPLATE_IS_MANDATORY);
    }
}