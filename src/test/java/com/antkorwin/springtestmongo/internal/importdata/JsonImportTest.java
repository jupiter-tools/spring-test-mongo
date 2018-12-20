package com.antkorwin.springtestmongo.internal.importdata;

import com.antkorwin.springtestmongo.internal.importdata.JsonImport;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class JsonImportTest {

    @Test
    void testImport() {
        // Act
        Map<String, List<Map<String, Object>>> map = new JsonImport(this::getJson).read();
        // Asserts
        assertThat(map).isNotNull()
                       .containsKeys("com.antkorwin.springtestmongo.Bar");

        Map<String, Object> bar = map.get("com.antkorwin.springtestmongo.Bar").get(0);

        assertThat(bar.keySet()).containsOnly("id", "data");
        assertThat(bar.values()).containsOnly("55f3ed00b1375a48e61830bf", "TEST");
    }

    private String getJson() {
        return "{\n" +
               "  \"com.antkorwin.springtestmongo.Bar\": [\n" +
               "    {\n" +
               "      \"id\": \"55f3ed00b1375a48e61830bf\",\n" +
               "      \"data\": \"TEST\"\n" +
               "    }\n" +
               "  ]\n" +
               "}";
    }
}