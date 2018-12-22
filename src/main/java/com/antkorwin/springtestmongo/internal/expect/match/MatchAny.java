package com.antkorwin.springtestmongo.internal.expect.match;

import com.antkorwin.springtestmongo.internal.expect.match.smart.MatchDataSmartFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 18.12.2018.
 * <p>
 * Top-level matcher, to match objects of any types
 * (List, Map, Number e.t.c.)
 *
 * @author Korovin Anatoliy
 */
public class MatchAny implements MatchData {

    private final MatchDataFactory matchDataFactory;
    private final MatchDataSmartFactory matchDataSmartFactory;

    public MatchAny() {
        this.matchDataFactory = new MatchDataFactory();
        this.matchDataSmartFactory = new MatchDataSmartFactory();
    }

    @Override
    public boolean match(Object original, Object expected) {

        if (matchDataSmartFactory.isNecessary(expected)) {
            return matchDataSmartFactory.get(expected)
                                        .match(original, expected);
        }

        return simpleMatch(original, expected);
    }

    private boolean simpleMatch(Object original, Object expected) {
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
