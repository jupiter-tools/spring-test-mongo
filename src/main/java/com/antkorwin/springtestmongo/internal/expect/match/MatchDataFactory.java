package com.antkorwin.springtestmongo.internal.expect.match;

import com.fasterxml.jackson.databind.node.JsonNodeType;

/**
 * Created on 19.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchDataFactory {

    public MatchData get(JsonNodeType type) {
        switch (type) {
            case OBJECT:
                return new MatchMap();
            case ARRAY:
                return new MatchList();
            case NUMBER:
                return new MatchNumber();
            case STRING:
                return new MatchString();
            default:
                return new MatchObjects();
        }
    }
}
