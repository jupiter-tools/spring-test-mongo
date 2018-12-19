package com.antkorwin.springtestmongo.internal.expect.match;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 18.12.2018.
 * <p>
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchAny implements MatchData {

    private final MatchDataFactory matchDataFactory;

    public MatchAny() {
        this.matchDataFactory = new MatchDataFactory();
    }

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

        return matchDataFactory.get(originalNode.getNodeType())
                               .match(original, expected);
    }
}
