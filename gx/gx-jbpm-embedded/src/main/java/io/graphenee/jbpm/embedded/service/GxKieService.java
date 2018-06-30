package io.graphenee.jbpm.embedded.service;

public interface GxKieService {

	public Object getVariableValue(Long processInstanceId, String variableName);

	public Object getVariableValue(Long processInstanceId, String variableName, Object defaultValue);

	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType);

	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType, T defaultValue);

	public Long getVariableValueAsLong(Long processInstanceId, String variableName, Long defaultValue);

}
