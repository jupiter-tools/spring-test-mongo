package com.jupiter.tools.spring.test.mongo.internal.geo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.io.IOException;

/**
 * Created on 2019-04-21
 *
 * Jackson module to serialization of GeoJson objects.
 *
 * @author Korovin Anatoliy
 */
public class GeoJsonSerializationModule extends SimpleModule {

    public GeoJsonSerializationModule() {
        addSerializer(new GeoJsonPointSerializer());
        addSerializer(new GeoJsonMultiPointSerializer());
        addSerializer(new GeoJsonLineStringSerializer());
        addSerializer(new GeoJsonMultiLineStringSerializer());
        addSerializer(new GeoJsonPolygonSerializer());
        addSerializer(new GeoJsonMultiPolygonSerializer());
    }


    /**
     * Convert {@link GeoJsonPoint} to String
     * <p>
     * Example of the output:
     *
     * <pre>
     * <code>
     * { "type": "Point", "coordinates": [10.0, 20.0] }
     * </pre>
     * </code>
     */
    private class GeoJsonPointSerializer extends JsonSerializer<GeoJsonPoint> {

        @Override
        public Class<GeoJsonPoint> handledType() {
            return GeoJsonPoint.class;
        }

        @Override
        public void serialize(GeoJsonPoint value,
                              JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {

            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            gen.writeObject(value.getCoordinates().get(0));
            gen.writeObject(value.getCoordinates().get(1));
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    /**
     * Convert {@link GeoJsonMultiPoint} to String
     * <p>
     * Example of the output:
     *
     * <pre>
     * <code>
     * {
     *   "type": "MultiPoint",
     *   "coordinates": [
     *     [10.0, 20.0], [30.0, 40.0], [50.0, 60.0]
     *   ]
     * }
     * </pre>
     * </code>
     */
    private class GeoJsonMultiPointSerializer extends JsonSerializer<GeoJsonMultiPoint> {

        @Override
        public Class<GeoJsonMultiPoint> handledType() {
            return GeoJsonMultiPoint.class;
        }

        @Override
        public void serialize(GeoJsonMultiPoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            for (Point p : value.getCoordinates()) {
                gen.writeObject(new double[]{p.getX(), p.getY()});
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    /**
     * Convert {@link GeoJsonLineString} to String
     * <p>
     * Example of the output:
     *
     * <pre>
     * <code>
     * {
     *   "type": "LineString",
     *   "coordinates": [
     *     [10.0, 20.0], [30.0, 40.0], [50.0, 60.0]
     *   ]
     * }
     * </pre>
     * </code>
     */
    private class GeoJsonLineStringSerializer extends JsonSerializer<GeoJsonLineString> {

        @Override
        public Class<GeoJsonLineString> handledType() {
            return GeoJsonLineString.class;
        }

        @Override
        public void serialize(GeoJsonLineString value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            for (Point p : value.getCoordinates()) {
                gen.writeObject(new double[]{p.getX(), p.getY()});
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    /**
     * Convert {@link GeoJsonMultiLineString} to String
     * <p>
     * Example of the output:
     *
     * <pre>
     * <code>
     * {
     *   "type": "MultiLineString",
     *   "coordinates": [
     *     [ [10.0, 20.0], [30.0, 40.0] ],
     *     [ [50.0, 60.0] , [70.0, 80.0] ]
     *   ]
     * }
     * </pre>
     * </code>
     */
    private class GeoJsonMultiLineStringSerializer extends JsonSerializer<GeoJsonMultiLineString> {

        @Override
        public Class<GeoJsonMultiLineString> handledType() {
            return GeoJsonMultiLineString.class;
        }

        @Override
        public void serialize(GeoJsonMultiLineString value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            for (GeoJsonLineString line : value.getCoordinates()) {
                gen.writeStartArray();
                for (Point p : line.getCoordinates()) {
                    gen.writeObject(new double[]{p.getX(), p.getY()});
                }
                gen.writeEndArray();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    /**
     * Convert {@link GeoJsonPolygon} to String
     * <p>
     * Example of the output:
     *
     * <pre>
     * <code>
     * {
     *   "type": "Polygon",
     *   "coordinates": [
     *     [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]
     *   ]
     * }
     * </pre>
     * </code>
     */
    private class GeoJsonPolygonSerializer extends JsonSerializer<GeoJsonPolygon> {

        @Override
        public Class<GeoJsonPolygon> handledType() {
            return GeoJsonPolygon.class;
        }

        @Override
        public void serialize(GeoJsonPolygon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            for (GeoJsonLineString ls : value.getCoordinates()) {
                gen.writeStartArray();
                for (Point p : ls.getCoordinates()) {
                    gen.writeObject(new double[]{p.getX(), p.getY()});
                }
                gen.writeEndArray();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    /**
     * Convert {@link GeoJsonMultiPolygon} to String
     * <p>
     * Example of the output:
     * <pre>
     * <code>
     * {
     *      "type":"MultiPolygon",
     *      "coordinates" : [
     *          [[ [1.0,1.0], [1.0,2.0], [1.0,3.0], [1.0,4.0] ]],
     *          [[ [2.0,1.0], [2.0,2.0], [2.0,3.0], [2.0,4.0] ]]
     *      ]
     * }
     * </pre>
     * </code>
     */
    private class GeoJsonMultiPolygonSerializer extends JsonSerializer<GeoJsonMultiPolygon> {

        @Override
        public Class<GeoJsonMultiPolygon> handledType() {
            return GeoJsonMultiPolygon.class;
        }

        @Override
        public void serialize(GeoJsonMultiPolygon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("type", value.getType());
            gen.writeArrayFieldStart("coordinates");
            for (GeoJsonPolygon polygon : value.getCoordinates()) {
                gen.writeStartArray();
                for (GeoJsonLineString lines : polygon.getCoordinates()) {
                    gen.writeStartArray();
                    for (Point p : lines.getCoordinates()) {
                        gen.writeObject(new double[]{p.getX(), p.getY()});
                    }
                    gen.writeEndArray();
                }
                gen.writeEndArray();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }
}
