package com.antkorwin.springtestmongo.junit5;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.antkorwin.springtestmongo.MongoPopulator;
import com.antkorwin.springtestmongo.annotation.ExportMongoDataSet;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 03.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtension.class)
@ExtendWith(MongoDbExportDataSetIT.ExportTestExtension.class)
@EnableMongoDbTestContainers
class MongoDbExportDataSetIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String OUTPUT_FILE_NAME = "target/dataset/export.json";

    @Test
    @ExportMongoDataSet(outputFile = OUTPUT_FILE_NAME)
    void exportDataSet() {
        MongoPopulator.populate(mongoTemplate, "/dataset/simple_collections_dataset.json");
    }


    static class ExportTestExtension implements Extension, AfterEachCallback {

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            InputStream inputStream = MongoPopulator.class.getClass().getResourceAsStream(OUTPUT_FILE_NAME);
            assertThat(inputStream).isNotNull();

            String stringDataSet = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertThat(stringDataSet).isNotNull();

            System.out.println(stringDataSet);
        }
    }
}
