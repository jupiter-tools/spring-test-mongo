package com.antkorwin.springtestmongo.internal;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class ExportFileTest {

    private static final String OUTPUT_FILE_NAME = "./target/test.txt";

    @BeforeEach
    void setUp() {
        boolean exists = new File(OUTPUT_FILE_NAME).exists();
        assertThat(exists).describedAs("Export file already exists, " +
                                       "please clean target directory before execute the test.")
                          .isFalse();
    }

    @AfterEach
    void tearDown() {
        new File(OUTPUT_FILE_NAME).delete();
    }

    @Test
    void write() throws IOException {
        // Arrange
        Text text = getStubText();
        ExportFile exportFile = new ExportFile(text);
        // Act
        exportFile.write(OUTPUT_FILE_NAME);
        // Asserts
        assertThat(getResultFromFile()).isEqualTo("test test test");
    }

    private Text getStubText() {
        return () -> "test test test";
    }

    private String getResultFromFile() throws IOException {
        InputStream inputStream = new FileInputStream(OUTPUT_FILE_NAME);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}