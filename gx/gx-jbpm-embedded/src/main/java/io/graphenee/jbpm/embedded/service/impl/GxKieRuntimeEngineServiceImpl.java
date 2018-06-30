package io.graphenee.jbpm.embedded.service.impl;

import javax.persistence.EntityManagerFactory;

import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.process.CorrelationKey;
import org.kie.internal.runtime.manager.context.CorrelationKeyContext;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;

import io.graphenee.jbpm.embedded.service.GxKieRuntimeEngineService;

@Service
public class GxKieRuntimeEngineServiceImpl implements GxKieRuntimeEngineService {

	@Autowired
	@Qualifier("jbpmEntityManagerFactory")
	EntityManagerFactory jbpmEntityManagerFactory;

	@Autowired
	@Qualifier("jbpmTransactionManager")
	JpaTransactionManager jbpmTransactionManager;

	@Autowired
	UserGroupCallback userGroupCallback;

	RuntimeManager singletonRuntimeManager;
	RuntimeManager perProcessRuntimeManager;
	RuntimeManager perRequestRuntimeManager;

	@Override
	public RuntimeEngine newSingletonRuntimeEngine() {
		if (singletonRuntimeManager == null) {
			RuntimeEnvironment renv = RuntimeEnvironmentBuilder.Factory.get().newClasspathKmoduleDefaultBuilder().userGroupCallback(userGroupCallback)
					.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, jbpmTransactionManager).entityManagerFactory(jbpmEntityManagerFactory).persistence(true).get();
			singletonRuntimeManager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(renv);
		}
		RuntimeEngine re = singletonRuntimeManager.getRuntimeEngine(EmptyContext.get());
		return re;
	}

	@Override
	public RuntimeEngine newPerRequestRuntimeEngine() {
		if (perRequestRuntimeManager == null) {
			RuntimeEnvironment renv = RuntimeEnvironmentBuilder.Factory.get().newClasspathKmoduleDefaultBuilder().userGroupCallback(userGroupCallback)
					.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, jbpmTransactionManager).entityManagerFactory(jbpmEntityManagerFactory).persistence(true).get();
			perRequestRuntimeManager = RuntimeManagerFactory.Factory.get().newPerRequestRuntimeManager(renv);
		}
		RuntimeEngine re = perRequestRuntimeManager.getRuntimeEngine(EmptyContext.get());
		return re;
	}

	@Override
	public RuntimeEngine newPerProcessRuntimeEngine() {
		initializePerProcessRuntimeManager();
		RuntimeEngine re = perProcessRuntimeManager.getRuntimeEngine(EmptyContext.get());
		return re;
	}

	@Override
	public RuntimeEngine newPerProcessRuntimeEngine(CorrelationKey key) {
		initializePerProcessRuntimeManager();
		RuntimeEngine re = perProcessRuntimeManager.getRuntimeEngine(CorrelationKeyContext.get(key));
		return re;
	}

	@Override
	public RuntimeEngine newPerProcessRuntimeEngine(Long processInstanceId) {
		initializePerProcessRuntimeManager();
		RuntimeEngine re = perProcessRuntimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(processInstanceId));
		return re;
	}

	private void initializePerProcessRuntimeManager() {
		if (perProcessRuntimeManager == null) {
			RuntimeEnvironment renv = RuntimeEnvironmentBuilder.Factory.get().newClasspathKmoduleDefaultBuilder().userGroupCallback(userGroupCallback)
					.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, jbpmTransactionManager).entityManagerFactory(jbpmEntityManagerFactory).persistence(true).get();
			perProcessRuntimeManager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(renv);
		}
	}

}
