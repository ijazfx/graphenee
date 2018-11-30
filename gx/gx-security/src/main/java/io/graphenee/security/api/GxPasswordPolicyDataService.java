package io.graphenee.security.api;

import java.util.List;

import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxPasswordPolicyBean;

public interface GxPasswordPolicyDataService {

	public List<GxPasswordPolicyBean> findPasswordPolicyByNamespace(GxNamespaceBean gxNamespaceBean);

	public GxPasswordPolicyBean createOrUpdate(GxPasswordPolicyBean bean);

	public void delete(GxPasswordPolicyBean bean);

	Boolean findPasswordIsValid(String namespace, String username, String password);

}
