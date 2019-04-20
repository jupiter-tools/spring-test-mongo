package com.jupiter.tools.spring.test.mongo.documents;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

/**
 * JSON-deserializer for the class GeoJsonPoint
 *
 * @author Korovin Anatoliy
 */
public class GeoJsonPointDeserializer extends JsonDeserializer<GeoJsonPoint> {

    @Override
    public GeoJsonPoint deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException,
                                                                                          JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();

        if (node == null || !node.has("type")) {
            return null;
        }

        if (node.get("type").asText().equals("Point")) {
            return new GeoJsonPoint(node.get("x").asDouble(),
                                    node.get("y").asDouble());
        }

        return null;
    }
}