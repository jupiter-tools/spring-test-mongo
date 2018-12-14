package com.antkorwin.springtestmongo.junit4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.antkorwin.springtestmongo.annotation.ExportMongoDataSet;
import com.antkorwin.springtestmongo.internal.MongoDbTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by Korovin A. on 23.01.2018.
 * <p>
 * Testing the clean-after logic of the {@link MongoDbRule}
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRuleExportDataSetIT extends BaseMongoIT {

    private static final String INPUT_DATA_SET_FILE = "/dataset/exported_dataset.json";
    private static final String OUTPUT_FILE_NAME = "target/dataset/export.json";

    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            File file = new File(OUTPUT_FILE_NAME);
            Files.deleteIfExists(file.toPath());
        }

        @Override
        protected void after() {
            try (FileInputStream inputStream = FileUtils.openInputStream(new File(OUTPUT_FILE_NAME))) {
                String stringDataSet = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                System.out.println(stringDataSet);
                assertThat(stringDataSet).isNotNull()
                                         .isEqualTo(getExpectedJson());
            }
            catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        private String getExpectedJson() throws IOException {
            try (InputStream inputStream = getClass().getResourceAsStream(INPUT_DATA_SET_FILE)) {
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        }
    };

    @Test
    @ExportMongoDataSet(outputFile = OUTPUT_FILE_NAME)
    public void testExportDataSet() throws Exception {
        new MongoDbTest(mongoTemplate).importFrom(INPUT_DATA_SET_FILE);
    }
}