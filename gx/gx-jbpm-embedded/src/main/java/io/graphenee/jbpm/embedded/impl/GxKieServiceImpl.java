/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.jbpm.embedded.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.strategy.PersistenceStrategyType;
import org.kie.api.runtime.manager.audit.VariableInstanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.jbpm.embedded.GxKieService;

/**
 * An implementation of {@link GxKieService}.
 */
@Service
@Transactional("jbpmTransactionManager")
public class GxKieServiceImpl implements GxKieService {

	/**
	 * Creates a new instance of this service.
	 */
	public GxKieServiceImpl() {
		// a default constructor
	}

	@Autowired
	@Qualifier("jbpmEntityManagerFactory")
	EntityManagerFactory emf;

	JPAAuditLogService auditLogService;

	/**
	 * Gets the audit log service.
	 * @return The audit log service.
	 */
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

	@Override
	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return variableType.cast(variableValue);
		return null;
	}

	@Override
	public Long getVariableValueAsLong(Long processInstanceId, String variableName, Long defaultValue) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return Long.parseLong(variableValue.toString());
		return defaultValue;
	}

	@Override
	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType, T defaultValue) {
		Object variableValue = getVariableValue(processInstanceId, variableName);
		if (variableValue != null)
			return variableType.cast(variableValue);
		return defaultValue;
	}

}
