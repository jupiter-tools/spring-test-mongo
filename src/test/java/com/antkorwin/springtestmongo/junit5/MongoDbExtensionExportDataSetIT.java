package com.antkorwin.springtestmongo.junit5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.antkorwin.springtestmongo.annotation.ExportMongoDataSet;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import com.antkorwin.springtestmongo.internal.MongoDbTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
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
 * Created on 03.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtensionExportDataSetIT.ExportTestExtension.class)
@ExtendWith(MongoDbExtension.class)
@EnableMongoDbTestContainers
class MongoDbExtensionExportDataSetIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String INPUT_DATA_SET_FILE = "/dataset/exported_dataset.json";
    private static final String OUTPUT_FILE_NAME = "target/dataset/export.json";

    @Test
    @MongoDataSet(cleanBefore = true)
    @ExportMongoDataSet(outputFile = OUTPUT_FILE_NAME)
    void exportDataSet() {
        // TODO: test it without MongoDbTest, must use external tools to be sure in stability of this solution
        new MongoDbTest(mongoTemplate).importFrom(INPUT_DATA_SET_FILE);
    }


    static class ExportTestExtension implements Extension, AfterEachCallback, BeforeEachCallback {

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            FileInputStream inputStream = FileUtils.openInputStream(new File(OUTPUT_FILE_NAME));
            assertThat(inputStream).isNotNull();

            String stringDataSet = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(stringDataSet);
            assertThat(stringDataSet).isNotNull()
                                     .isEqualTo(getExpectedJson());
        }

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            File file = new File(OUTPUT_FILE_NAME);
            Files.deleteIfExists(file.toPath());
        }

        private String getExpectedJson() throws IOException {
            final InputStream inputStream =
                    ExportTestExtension.class.getClass().getResourceAsStream(INPUT_DATA_SET_FILE);

            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
