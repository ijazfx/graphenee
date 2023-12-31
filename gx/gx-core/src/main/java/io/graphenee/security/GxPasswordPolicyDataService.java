package io.graphenee.security;

import java.util.List;

import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.core.model.entity.GxUserAccount;

public interface GxPasswordPolicyDataService {

	public List<GxPasswordPolicy> findAllPasswordPolicyByNamespace(GxNamespace namespace);

	public GxPasswordPolicy findPasswordPolicyByNamespace(GxNamespace namespace);

	public GxPasswordPolicy save(GxPasswordPolicy entity);

	public void delete(GxPasswordPolicy entity);

	Boolean findPasswordIsValid(GxNamespace namespace, String username, String password);

	void assertPasswordPolicy(GxNamespace namespace, String username, String password) throws AssertionError;

	void assertPasswordPolicy(GxPasswordPolicy entity, String username, String password) throws AssertionError;

	void changePassword(GxNamespace namespace, String username, String oldPassword, String newPassword) throws ChangePasswordFailedException;

	void changePassword(GxNamespace namespace, String username, String newPassword) throws ChangePasswordFailedException;

	Boolean isPasswordExpired(GxNamespace namespace, GxUserAccount usAccount);
}
