package io.graphenee.core.model.bean;

import java.util.Map;

import io.graphenee.util.GxExpressionEvaluator;

public class GxSecurityPolicyStatement {
    private String statementType;
    private String action;
    private String resource;
    private String condition;

    public GxSecurityPolicyStatement(String statementType, String action, String resource, String condition) {
        this.statementType = statementType;
        this.action = resource == null ? "all" : action;
        this.resource = resource == null ? action : resource;
        this.condition = condition;
    }

    public boolean isGrant() {
        return "grant".equalsIgnoreCase(statementType);
    }

    public boolean isRevoke() {
        return "revoke".equalsIgnoreCase(statementType);
    }

    public String getResource() {
        return resource != null ? resource : "all";
    }

    public String[] getActions() {
        return (action != null ? "all" : action).splitWithDelimiters("\\s*,\\s*", 0);
    }

    public boolean evaluate(Map<String, Object> keyValueMap) {
        return GxExpressionEvaluator.evaluate(condition, keyValueMap);
    }

}