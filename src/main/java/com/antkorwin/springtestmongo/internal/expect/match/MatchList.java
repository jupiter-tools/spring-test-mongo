package com.antkorwin.springtestmongo.internal.expect.match;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created on 18.12.2018.
 * <p>
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchList implements DataMatch {

    private final ObjectMapper objectMapper;
    private final Logger log;

    public MatchList() {
        this.objectMapper = new ObjectMapper();
        this.log = LoggerFactory.getLogger(MatchList.class);
    }

    @Override
    public boolean match(Object original, Object expected) {

        List<Object> originalList = convertToList(original);
        List<Object> expectedList = convertToList(expected);

        if (originalList.size() != expectedList.size()) {
            log.error("different array sizes: \n  actual: {}\n  expected: {}",
                      writeObject(original),
                      writeObject(expected));
            return false;
        }

        for (int i = 0; i < originalList.size(); i++) {
            if (!new AnyDataMatch().match(originalList.get(i),
                                          expectedList.get(i))) {
                return false;
            }
        }

        return true;
    }

    private List<Object> convertToList(Object object) {
        return objectMapper.convertValue(object, List.class);
    }

    private String writeObject(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("jackson parsing error", e);
            throw new InternalException("jackson error", 104, e);
        }
    }
}
