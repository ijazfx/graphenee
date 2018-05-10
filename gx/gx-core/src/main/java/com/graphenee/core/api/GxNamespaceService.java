package com.graphenee.core.api;

import com.graphenee.core.model.bean.GxNamespaceBean;

public interface GxNamespaceService {

	GxNamespaceBean systemNamespace();

	GxNamespaceBean applicationNamespace();

	GxNamespaceBean namespace(String namespace);

}
