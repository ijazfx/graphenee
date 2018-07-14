package io.graphenee.jbpm.embedded.service.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.strategy.PersistenceStrategyType;
import org.kie.api.runtime.manager.audit.VariableInstanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.jbpm.embedded.service.GxKieService;

@Service
@Transactional("jbpmTransactionManager")
public class GxKieServiceImpl implements GxKieService {

	@Autowired
	@Qualifier("jbpmEntityManagerFactory")
	EntityManagerFactory emf;

	JPAAuditLogService auditLogService;

	public JPAAuditLogService getAuditLogService() {
		if (auditLogService == null) {
			auditLogService = new JPAAuditLogService(emf, PersistenceStrategyType.STANDALONE_LOCAL_SPRING_SHARED_EM);
		}
		return auditLogService;
	}

	@Override
	public Object getVariableValue(Long processInstanceId, String variableName) {
		List<? extends VariableInstanceLog> variables = getAuditLogService().findVariableInstances(processInstanceId);
		VariableInstanceLog found = null;
		for (VariableInstanceLog variable : variables) {
			System.err.println(variable.getVariableId() + "=" + variable.getValue() + ",old=" + variable.getOldValue() + ",time=" + variable.getDate());
			if (variable.getVariableId().equals(variableName)) {
				found = variable;
			}
		}
		if (found != null) {
			return found.getValue();
		}
		return null;
	}

	@Override
	public Object getVariableValue(Long processInstanceId, String variableName, Object defaultValue) {
		Object value = getVariableValue(processInstanceId, variableName);
		if (value != null)
			return value;
		return defaultValue;
	}

	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return variableType.cast(variableValue);
		return null;
	}

	public Long getVariableValueAsLong(Long processInstanceId, String variableName, Long defaultValue) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return Long.parseLong(variableValue.toString());
		return defaultValue;
	}

	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType, T defaultValue) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return variableType.cast(variableValue);
		return defaultValue;
	}

}
