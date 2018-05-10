package io.graphenee.core.api;

import io.graphenee.core.model.bean.GxNamespaceBean;

public interface GxNamespaceService {

	GxNamespaceBean systemNamespace();

	GxNamespaceBean applicationNamespace();

	GxNamespaceBean namespace(String namespace);

}
