package io.graphenee.core.model.bean;

public class GxValueHolderBean<T> {

	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
