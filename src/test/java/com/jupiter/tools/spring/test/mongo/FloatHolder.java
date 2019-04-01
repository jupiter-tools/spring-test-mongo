package com.jupiter.tools.spring.test.mongo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created on 01.04.2019.
 *
 * @author Korovin Anatoliy
 */
@Document
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatHolder {
    private String name;
    private List<Float> values;
}
