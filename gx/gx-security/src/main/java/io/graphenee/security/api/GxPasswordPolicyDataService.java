package io.graphenee.security.api;

import java.util.List;

import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxPasswordPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;

public interface GxPasswordPolicyDataService {

	public List<GxPasswordPolicyBean> findPasswordPolicyByNamespace(GxNamespaceBean gxNamespaceBean);

	public GxPasswordPolicyBean findOnePasswordPolicyByNamespace(GxNamespaceBean gxNamespaceBean);

	public GxPasswordPolicyBean createOrUpdate(GxPasswordPolicyBean bean);

	public void delete(GxPasswordPolicyBean bean);

	Boolean findPasswordIsValid(String namespace, String username, String password);

	void assertPasswordPolicy(GxPasswordPolicyBean policyBean, String username, String password) throws AssertionError;

	void changePasswordPolicyApply(String username, String oldPassword, String newPassword, String confirmPassword) throws ChangePasswordFailedException;

	Boolean isPasswordExpired(GxUserAccountBean usAccountBean);
}
