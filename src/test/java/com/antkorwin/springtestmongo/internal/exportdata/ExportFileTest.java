package com.antkorwin.springtestmongo.internal.exportdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.Text;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    private static final String OUTPUT_FILE_NAME = "./target/test-folder/test.txt";

    @BeforeEach
    void setUp() {
        boolean exists = new File(OUTPUT_FILE_NAME).exists();
        assertThat(exists).describedAs("Export file already exists, " +
                                       "please clean target directory before execute the test.")
                          .isFalse();
    }

    @AfterEach
    void tearDown() {
        System.out.println("DELETE: " + new File(OUTPUT_FILE_NAME).delete());
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

    @Test
    void OverrideAlreadyExistedFile() throws IOException {
        // Arrange
        new ExportFile(() -> "first").write(OUTPUT_FILE_NAME);
        // Act
        new ExportFile(() -> "second").write(OUTPUT_FILE_NAME);
        // Asserts
        assertThat(getResultFromFile()).isEqualTo("second");
    }

    @Test
    void withoutFileName() throws IOException {
        // Act
        Assertions.assertThrows(InternalException.class, () -> {
            new ExportFile(() -> "wrong").write(null);
        });
    }

    private Text getStubText() {
        return () -> "test test test";
    }

    private String getResultFromFile() throws IOException {
        try (InputStream inputStream = new FileInputStream(OUTPUT_FILE_NAME);) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}