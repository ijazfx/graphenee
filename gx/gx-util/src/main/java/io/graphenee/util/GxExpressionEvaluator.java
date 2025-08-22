package io.graphenee.util;

import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Strings;

public class GxExpressionEvaluator {

    public static boolean evaluate(String condition, Map<String, Object> keyValueMap) {
        try {
            if (Strings.isNullOrEmpty(condition) || keyValueMap == null || keyValueMap.isEmpty())
                return true;
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext context = new StandardEvaluationContext();
            keyValueMap.entrySet().forEach(entry -> {
                context.setVariable(entry.getKey(), entry.getValue());
            });

            Expression expression = parser.parseExpression(condition);
            return expression.getValue(context, Boolean.class);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
