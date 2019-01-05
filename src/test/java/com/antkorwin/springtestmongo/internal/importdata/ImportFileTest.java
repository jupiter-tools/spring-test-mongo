package com.antkorwin.springtestmongo.internal.importdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.Text;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void tryToReadNotExistsFile() {
        // Arrange
        Text fileText = new ImportFile("dataset/not_exist_file_name.txt");
        // Act
        Assertions.assertThrows(InternalException.class,
                                fileText::read);
    }
}