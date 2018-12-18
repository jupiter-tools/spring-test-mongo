package com.antkorwin.springtestmongo.internal.expect.match;

import com.antkorwin.springtestmongo.internal.expect.matcher.MatcherFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.List;
import java.util.Map;

/**
 * Created on 18.12.2018.
 * <p>
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchAny implements DataMatch {

    @Override
    public boolean match(Object original, Object expected) {

        JsonNode originalNode = new ObjectMapper().valueToTree(original);
        JsonNode expectedNode = new ObjectMapper().valueToTree(expected);

        if (original == null) {
            return expected == null;
        }

        if (expected == null) {
            return true;
        }

        if (originalNode.getNodeType() != expectedNode.getNodeType()) {
            return false;
        }

        if (originalNode.getNodeType() == JsonNodeType.OBJECT) {
            return new MatchMap().match(original, expected);
        }

        if (originalNode.getNodeType() == JsonNodeType.ARRAY) {
            return new MatchList().match(original, expected);
        }

        return new MatcherFactory().getMatcher(expected).match(original, expected);
    }
}
