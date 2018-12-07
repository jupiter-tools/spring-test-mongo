package com.antkorwin.springtestmongo.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class ImportFileTest {

    @Test
    void read() {
        // Arrange
        Text fileText = new ImportFile("/dataset/internal/test_file.txt");
        // Act
        String text = fileText.read();
        // Asserts
        assertThat(text).isEqualTo("secret-123");
    }
}