package com.jupiter.tools.spring.test.mongo.junit5;

import java.util.Calendar;
import java.util.Date;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.annotation.ExpectedMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.ExportMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 01.12.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MongoDbExtension.class)
@EnableMongoDbTestContainers
class MongoDbExtensionImportIT {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * Testing import to the current mongo database from a dataset defined in the MongoDataSet annotation
	 */
	@Test
	@MongoDataSet(value = "/dataset/multidocument_dataset.json", cleanBefore = true, cleanAfter = true)
	void testImportByMongoDataSetAnnotation() throws Exception {
		// Act
		Foo fooDoc = mongoTemplate.findById("77f3ed00b1375a48e618300a", Foo.class);
		Bar simpleDoc = mongoTemplate.findById("55f3ed00b1375a48e618300b", Bar.class);

		// Assert
		assertThat(fooDoc)
				.isNotNull()
				.extracting(Foo::getId, Foo::getCounter, Foo::getTime)
				.containsOnly("77f3ed00b1375a48e618300a", 1, new Date(1516527720000L));

		assertThat(simpleDoc)
				.isNotNull()
				.extracting(Bar::getId, Bar::getData)
				.containsOnly("55f3ed00b1375a48e618300b", "BB");
	}

	@Test
	@MongoDataSet(value = "/dataset/multidocument_dataset.json",
	              readOnly = true,
	              cleanBefore = true,
	              cleanAfter = true)
	void readOnly() {
		Foo fooDoc = mongoTemplate.findById("77f3ed00b1375a48e618300a", Foo.class);
		assertThat(fooDoc).isNotNull();
	}

	@Test
	void withoutAnnotation() {
		//nothing
	}

	@Test
	@MongoDataSet(value = "/dataset/dynamic_dataset.json",
	              cleanBefore = true, cleanAfter = true)
	void dynamicDataSets() {
		Foo foo = mongoTemplate.findById("77f3ed00b1375a48e618300a", Foo.class);
		assertThat(foo).isNotNull();
		assertThat(foo.getCounter()).isEqualTo(3);
		assertThat(foo.getTime()).isEqualTo(new Date(87, Calendar.NOVEMBER, 5));
	}

	@Test
	@ExpectedMongoDataSet("/dataset/expected_floating_point.json")
	void floatingPointTest() {
		FloatingPoint floatingPoint = FloatingPoint.builder()
		                                           .value(4.7f)
		                                           .build();
		mongoTemplate.save(floatingPoint);
	}

	@Document
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class FloatingPoint {
		@Id
		private String id;
		private float value;
	}
}
