package io.graphenee.security;

import java.util.Map;

import io.graphenee.core.model.bean.GxSecurityPolicyStatement;

public interface GxSecurityPolicyParser {

    GxSecurityPolicyStatement parse(String statement);
    boolean evaluate(GxSecurityPolicyStatement statement, Map<String, Object> keyValueMap);

}