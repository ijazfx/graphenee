package io.graphenee.core.model.bean;

import java.io.Serializable;

public class GxImportChartOfAccountBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer year;
	private GxNamespaceBean namespaceBean;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public GxNamespaceBean getNamespaceBean() {
		return namespaceBean;
	}

	public void setNamespaceBean(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
	}
}
