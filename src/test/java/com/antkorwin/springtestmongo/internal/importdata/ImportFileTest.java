package com.antkorwin.springtestmongo.internal.importdata;

import com.antkorwin.springtestmongo.internal.Text;
import com.antkorwin.springtestmongo.internal.importdata.ImportFile;
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

    @Test
    void readWithoutSlashInPath() {
        // Arrange
        Text fileText = new ImportFile("dataset/internal/test_file.txt");
        // Act
        String text = fileText.read();
        // Asserts
        assertThat(text).isEqualTo("secret-123");
    }

    @Test
    void readWithoutDataSetInPath() {
        // Arrange
        Text fileText = new ImportFile("internal/test_file.txt");
        // Act
        String text = fileText.read();
        // Asserts
        assertThat(text).isEqualTo("secret-123");
    }
}