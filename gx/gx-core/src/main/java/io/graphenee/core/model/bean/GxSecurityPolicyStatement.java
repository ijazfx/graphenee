package io.graphenee.core.model.bean;

import lombok.Getter;

@Getter
public class GxSecurityPolicyStatement {
    private String statementType;
    private String action;
    private String resource;
    private String condition;

    public GxSecurityPolicyStatement(String statementType, String action, String resource, String condition) {
        this.statementType = statementType;
        this.action = action;
        this.resource = resource;
        this.condition = condition;
    }

    public boolean isGrant() {
        return "grant".equalsIgnoreCase(statementType);
    }

    public boolean isRevoke() {
        return "revoke".equalsIgnoreCase(statementType);
    }

    public String getAction() {
        return action != null ? action : "all";
    }

}