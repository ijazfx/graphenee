package io.graphenee.security.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import io.graphenee.core.model.bean.GxSecurityPolicyStatement;
import io.graphenee.security.GxSecurityPolicyParser;

@Service
public class GxSecurityPolicyParserImpl implements GxSecurityPolicyParser {

    private static final Pattern PATTERN = Pattern.compile(
            "^<statementType>(?:\\s+(?<action>[a-zA-Z]+)\\s+on)?\\s+(?<resource>[a-zA-Z0-9]+)(?:\\s+when\\s+(?<condition>.+))?$");

    public GxSecurityPolicyStatement parse(String statement) {
        Matcher matcher = PATTERN.matcher(statement.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid policy statement");
        }

        GxSecurityPolicyStatement spstmt = new GxSecurityPolicyStatement(matcher.group("statementType"),
                matcher.group("action"), matcher.group("resource"), matcher.group("condition"));
        return spstmt;
    }

    public boolean evaluate(GxSecurityPolicyStatement statement, Map<String, Object> keyValueMap) {
        if (statement == null || Strings.isNullOrEmpty(statement.getCondition()))
            return true;

        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        keyValueMap.entrySet().forEach(entry -> {
            context.setVariable(entry.getKey(), entry.getValue());
        });

        Expression expression = parser.parseExpression(statement.getCondition());
        return expression.getValue(context, Boolean.class);
    }

}