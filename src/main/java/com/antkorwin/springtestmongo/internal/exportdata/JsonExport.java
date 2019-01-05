package com.antkorwin.springtestmongo.internal.exportdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.DataSet;
import com.antkorwin.springtestmongo.internal.Text;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.LoggerFactory;

/**
 * Convert object to {@link Text} in JSON format.
 *
 * @author Korovin Anatoliy
 */
public class JsonExport implements Text {

    private final DataSet dataSet;
    private final ObjectMapper objectMapper;

    public JsonExport(DataSet dataSet) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataSet = dataSet;
    }

    @Override
    public String read() {
        try {
            return objectMapper.writeValueAsString(dataSet.read());
        } catch (Exception e) {
            LoggerFactory.getLogger(JsonExport.class)
                         .error("Error while converting dataset to JSON string: ", e);
            throw new InternalException(e);
        }
    }
}
