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
        Text fileText = new ImportFile("/dataset/internal/json_expected.json");
        // Act
        String text = fileText.read();
        // Asserts
        assertThat(text).isEqualTo(getExpectedText());
    }

    private String getExpectedText() {
        return "{\n" +
               "  \"com.antkorwin.springtestmongo.Bar\" : [ {\n" +
               "    \"id\" : \"111100001\",\n" +
               "    \"data\" : \"data-1\"\n" +
               "  }, {\n" +
               "    \"id\" : \"111100002\",\n" +
               "    \"data\" : \"data-2\"\n" +
               "  } ]\n" +
               "}";
    }
}