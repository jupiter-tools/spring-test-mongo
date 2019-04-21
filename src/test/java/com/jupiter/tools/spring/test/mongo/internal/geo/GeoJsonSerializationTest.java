package com.jupiter.tools.spring.test.mongo.internal.geo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 2019-04-21
 *
 * @author Korovin Anatoliy
 */
class GeoJsonSerializationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new GeoJsonSerializationModule());
        mapper.registerModule(new GeoJsonModule());
    }

    @Test
    void pointTest() throws JsonProcessingException {
        // Arrange
        GeoJsonPoint point = new GeoJsonPoint(5.11, 19.87);
        // Act
        String stringValue = mapper.writeValueAsString(point);
        // Assert
        assertThat(stringValue).isEqualTo("{\"type\":\"Point\",\"coordinates\":[5.11,19.87]}");
    }

    @Test
    void pointDeserializeAndSerialize() throws IOException {
        // Arrange
        String stringPoint = "{\"type\":\"Point\",\"coordinates\":[5.11,19.87]}";
        // Act
        GeoJsonPoint jsonPoint = mapper.readValue(stringPoint, GeoJsonPoint.class);
        String result = mapper.writeValueAsString(jsonPoint);
        // Assert
        assertThat(result).isEqualTo(stringPoint);
    }

    @Test
    void multiPointTest() throws JsonProcessingException {
        // Arrange
        Point p1 = new Point(0, 0);
        Point p2 = new Point(10, 5);
        Point p3 = new Point(15, 7);

        GeoJsonMultiPoint multiPoint = new GeoJsonMultiPoint(p1, p2, p3);

        // Act
        String stringValue = mapper.writeValueAsString(multiPoint);

        // Assert
        assertThat(stringValue).isEqualTo("{\"type\":\"MultiPoint\",\"coordinates\":[[0.0,0.0],[10.0,5.0],[15.0,7.0]]}");
    }

    @Test
    void multiPointDeserializeAndSerialize() throws IOException {
        // Arrange
        String source = "{\"type\":\"MultiPoint\"," +
                        "\"coordinates\":[" +
                        "[0.0,0.0]," +
                        "[10.0,5.0]," +
                        "[15.0,7.0]" +
                        "]" +
                        "}";
        // Act
        GeoJsonMultiPoint jsonPoints = mapper.readValue(source, GeoJsonMultiPoint.class);
        String result = mapper.writeValueAsString(jsonPoints);
        // Assert
        assertThat(result).isEqualTo(source);
    }

    @Test
    void lineStringTest() throws JsonProcessingException {
        // Arrange
        Point p1 = new Point(0, 0);
        Point p2 = new Point(10, 10);
        GeoJsonLineString line = new GeoJsonLineString(p1, p2);

        // Act
        String stringValue = mapper.writeValueAsString(line);

        // Assert
        assertThat(stringValue).isEqualTo("{\"type\":\"LineString\",\"coordinates\":[[0.0,0.0],[10.0,10.0]]}");
    }

    @Test
    void lineStringDeserializeAndSerialize() throws IOException {
        // Arrange
        String source = "{" +
                        "\"type\":\"LineString\"," +
                        "\"coordinates\":" +
                        "[" +
                        "[0.0,0.0]," +
                        "[10.0,10.0]" +
                        "]" +
                        "}";
        // Act
        GeoJsonLineString json = mapper.readValue(source, GeoJsonLineString.class);
        String result = mapper.writeValueAsString(json);
        // Assert
        assertThat(result).isEqualTo(source);
    }

    @Test
    void multiLineStringTest() throws JsonProcessingException {
        // Arrange
        String expectedResult = ("{\"type\":\"MultiLineString\"," +
                                 "\"coordinates\":[" +
                                 "[[10.0, 20.0],[30.0, 40.0]]," +
                                 "[[50.0, 60.0],[70.0, 80.0]]" +
                                 "]}").replaceAll("[\\n\\t ]", "");

        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 40);
        Point p3 = new Point(50, 60);
        Point p4 = new Point(70, 80);
        GeoJsonLineString line1 = new GeoJsonLineString(p1, p2);
        GeoJsonLineString line2 = new GeoJsonLineString(p3, p4);
        GeoJsonMultiLineString multiLineString = new GeoJsonMultiLineString(Arrays.asList(line1, line2));

        // Act
        String stringValue = mapper.writeValueAsString(multiLineString);

        // Assert
        assertThat(stringValue).isEqualTo(expectedResult);
    }

    @Test
    void multiLineStringDeserializeAndSerialize() throws IOException {
        // Arrange
        String source = ("{\"type\":\"MultiLineString\"," +
                        "\"coordinates\":[" +
                        "[[10.0, 20.0],[30.0, 40.0]]," +
                        "[[50.0, 60.0],[70.0, 80.0]]" +
                        "]}").replaceAll("[\\n\\t ]","");
        // Act
        GeoJsonMultiLineString json = mapper.readValue(source, GeoJsonMultiLineString.class);
        String result = mapper.writeValueAsString(json);
        // Assert
        assertThat(result).isEqualTo(source);
    }

    @Test
    void polygonTest() throws JsonProcessingException {
        // Arrange
        Point p1 = new Point(0, 3);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(2, 1);
        Point p4 = new Point(3, 0);
        GeoJsonPolygon polygon = new GeoJsonPolygon(p1, p2, p3, p4);

        // Act
        String stringValue = mapper.writeValueAsString(polygon);

        // Assert
        assertThat(stringValue).isEqualTo("{\"type\":\"Polygon\",\"coordinates\":[[[0.0,3.0],[1.0,2.0],[2.0,1.0],[3.0,0.0]]]}");
    }

    @Test
    void polygonDeserializeAndSerialize() throws IOException {
        // Arrange
        String source = "{" +
                        "\"type\":\"Polygon\"," +
                        "\"coordinates\":[" +
                        "[[0.0,3.0],[1.0,2.0],[2.0,1.0],[3.0,0.0]]" +
                        "]}";
        // Act
        GeoJsonPolygon json = mapper.readValue(source, GeoJsonPolygon.class);
        String result = mapper.writeValueAsString(json);
        // Assert
        assertThat(result).isEqualTo(source);
    }

    @Test
    void multiPolygon() throws IOException {
        // Arrange
        GeoJsonPolygon polygon1 = new GeoJsonPolygon(new Point(1, 1),
                                                     new Point(1, 2),
                                                     new Point(1, 3),
                                                     new Point(1, 4));

        GeoJsonPolygon polygon2 = new GeoJsonPolygon(new Point(2, 1),
                                                     new Point(2, 2),
                                                     new Point(2, 3),
                                                     new Point(2, 4));

        GeoJsonMultiPolygon multiPolygon = new GeoJsonMultiPolygon(Arrays.asList(polygon1, polygon2));

        // Act
        String result = mapper.writeValueAsString(multiPolygon);

        // Assert
        assertThat(result).isEqualTo("{" +
                                     "\"type\":\"MultiPolygon\"," +
                                     "\"coordinates\":[" +
                                     "[[[1.0,1.0],[1.0,2.0],[1.0,3.0],[1.0,4.0]]]," +
                                     "[[[2.0,1.0],[2.0,2.0],[2.0,3.0],[2.0,4.0]]]" +
                                     "]" +
                                     "}");
    }

    @Test
    void multiPolygonDeserializeAndSerialize() throws IOException {
        // Arrange
        String source = "{" +
                        "\"type\":\"MultiPolygon\"," +
                        "\"coordinates\":[" +
                        "[[[1.0,1.0],[1.0,2.0],[1.0,3.0],[1.0,4.0]]]," +
                        "[[[2.0,1.0],[2.0,2.0],[2.0,3.0],[2.0,4.0]]]" +
                        "]" +
                        "}";
        // Act
        GeoJsonMultiPolygon json = mapper.readValue(source, GeoJsonMultiPolygon.class);
        String result = mapper.writeValueAsString(json);
        // Assert
        assertThat(result).isEqualTo(source);
    }
}
