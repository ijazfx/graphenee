package io.graphenee.security;

import io.graphenee.core.model.bean.GxSecurityPolicyStatement;

public interface GxSecurityPolicyParser {

    GxSecurityPolicyStatement parse(String statement);

}