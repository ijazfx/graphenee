package io.graphenee.security.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import io.graphenee.core.model.bean.GxSecurityPolicyStatement;
import io.graphenee.security.GxSecurityPolicyParser;

@Service
public class GxSecurityPolicyParserImpl implements GxSecurityPolicyParser {

    private static final Pattern PATTERN = Pattern.compile(
            "^(?<statementType>\\w+)\\s+(?<action>[\\w,\\s-]+?)(?:\\s+on\\s+(?<resource>[\\w-]+))?(?:\\s+when\\s+(?<condition>.+))?$");

    public GxSecurityPolicyStatement parse(String statement) {
        Matcher matcher = PATTERN.matcher(statement.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid policy statement");
        }

        GxSecurityPolicyStatement spstmt = new GxSecurityPolicyStatement(matcher.group("statementType"),
                matcher.group("action"), matcher.group("resource"), matcher.group("condition"));
        return spstmt;
    }

}