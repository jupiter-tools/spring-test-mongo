package com.jupiter.tools.spring.test.mongo.junit5;

import com.jupiter.tools.spring.test.mongo.annotation.ExportMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.documents.StarShip;
import com.jupiter.tools.spring.test.mongo.junit5.meta.annotation.MongoDbIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Korovin Anatoliy
 */
@MongoDbIntegrationTest
class MongoDbExtensionGeoJsonIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @ExportMongoDataSet(outputFile = "target/dataset/export_point.json")
    void exportPoint() {

        StarShip dreadnought = StarShip.builder()
                                       .armor(100)
                                       .damage(10)
                                       .location(new GeoJsonPoint(10, 35))
                                       .build();

        mongoTemplate.save(dreadnought);
    }

    @Test
    @MongoDataSet(value = "dataset/geo/geo_point.json",
                  cleanBefore = true,
                  cleanAfter = true)
    void readPoint() {
        // Act
        StarShip ship = mongoTemplate.findById("5cbaa745921376602705886f", StarShip.class);
        // Assert
        assertThat(ship).isNotNull();
        assertThat(ship.getLocation()).extracting(GeoJsonPoint::getType,
                                                  GeoJsonPoint::getCoordinates,
                                                  Point::getX,
                                                  Point::getY)
                                      .contains("Point",
                                                Arrays.asList(10.0, 35.0),
                                                10.0,
                                                35.0);
    }

    @Test
    @ExportMongoDataSet(outputFile = "target/dataset/export_polygon.json")
    void exportPolygon() {

        Point p1 = new Point(20, 40);
        Point p2 = new Point(22, 42);
        Point p3 = new Point(22, 40);
        Point p4 = new Point(20, 42);

        StarShip dreadnought = StarShip.builder()
                                       .armor(5)
                                       .damage(1)
                                       .location(new GeoJsonPoint(20, 40))
                                       .shape(new GeoJsonPolygon(p1, p2, p3, p4))
                                       .build();

        mongoTemplate.save(dreadnought);
    }

    @Test
    @MongoDataSet(value = "dataset/geo/geo_poly.json",
                  cleanBefore = true,
                  cleanAfter = true)
    void readPolygon() {
        // Arrange
        Point p1 = new Point(20, 40);
        Point p2 = new Point(22, 42);
        Point p3 = new Point(22, 40);
        Point p4 = new Point(20, 42);
        // Act
        StarShip ship = mongoTemplate.findById("5cbba77d92137661ee1919e2", StarShip.class);
        // Assert
        assertThat(ship).isNotNull();
        assertThat(ship.getShape().getPoints()).contains(p1, p2, p3, p4);
        assertThat(ship.getShape().getType()).isEqualTo("Polygon");
    }


    @Test
    @MongoDataSet(value = "dataset/geo/geo_within.json",
                  cleanBefore = true,
                  cleanAfter = true)
    void findWithinRectangle() {
        // Act
        Query query = new Query();
        GeoJsonPolygon boundary = new GeoJsonPolygon(new Point(0, 0),
                                                     new Point(10, 0),
                                                     new Point(10, 10),
                                                     new Point(0, 10),
                                                     new Point(0, 0));

        query.addCriteria(Criteria.where("location")
                                  .within(boundary));
        // Act
        List<StarShip> ships = mongoTemplate.find(query, StarShip.class);

        // Assert
        assertThat(ships).hasSize(1);
        assertThat(ships.get(0).getName()).isEqualTo("x-wing");
        assertThat(ships.get(0).getLocation().getCoordinates()).containsExactly(1.0, 5.0);
    }
}
