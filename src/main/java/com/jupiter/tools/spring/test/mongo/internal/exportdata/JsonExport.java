package com.jupiter.tools.spring.test.mongo.internal.exportdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.jupiter.tools.spring.test.mongo.internal.DataSet;
import com.jupiter.tools.spring.test.mongo.internal.Text;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert object to {@link Text} in JSON format.
 *
 * @author Korovin Anatoliy
 */
public class JsonExport implements Text {

    private final DataSet dataSet;
    private final ObjectMapper objectMapper;
    private final Logger log;

    public JsonExport(DataSet dataSet) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataSet = dataSet;
        this.log = LoggerFactory.getLogger(JsonExport.class);
    }

    @Override
    public String read() {
        try {
            return objectMapper.writeValueAsString(dataSet.read());
        } catch (Exception e) {
            log.error("Error while converting dataset to JSON string: ", e);
            throw new InternalException(e);
        }
    }
}
