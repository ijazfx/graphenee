package io.graphenee.security.api;

import java.util.List;

import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxPasswordPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;

public interface GxPasswordPolicyDataService {

	public List<GxPasswordPolicyBean> findAllPasswordPolicyByNamespace(GxNamespaceBean gxNamespaceBean);

	public GxPasswordPolicyBean findPasswordPolicyByNamespace(GxNamespaceBean gxNamespaceBean);

	public GxPasswordPolicyBean findPasswordPolicyByNamespace(String namespace);

	public GxPasswordPolicyBean createOrUpdate(GxPasswordPolicyBean bean);

	public void delete(GxPasswordPolicyBean bean);

	Boolean findPasswordIsValid(String namespace, String username, String password);

	void assertPasswordPolicy(String namespace, String username, String password) throws AssertionError;

	void assertPasswordPolicy(GxPasswordPolicyBean entity, String username, String password) throws AssertionError;

	void changePassword(String namespace, String username, String oldPassword, String newPassword) throws ChangePasswordFailedException;

	Boolean isPasswordExpired(String namespace, GxUserAccountBean usAccountBean);
}
