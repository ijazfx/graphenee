package io.graphenee.security;

import io.graphenee.security.impl.GxSecurityPolicyParserImpl;

public class GxSecurityPolicyParserFactory {

    private static GxSecurityPolicyParser DEFAULT_PARSER;

    public static GxSecurityPolicyParser defaultParser() {
        if(DEFAULT_PARSER == null) {
            synchronized(GxSecurityPolicyParser.class) {
                if(DEFAULT_PARSER == null) {
                    DEFAULT_PARSER = new GxSecurityPolicyParserImpl();
                }
            }
        }
        return DEFAULT_PARSER;
    }

    public static GxSecurityPolicyParser createParser() {
        return new GxSecurityPolicyParserImpl();
    }

}
