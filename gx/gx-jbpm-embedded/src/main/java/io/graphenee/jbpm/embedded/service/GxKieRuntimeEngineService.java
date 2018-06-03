package io.graphenee.jbpm.embedded.service;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.internal.process.CorrelationKey;

public interface GxKieRuntimeEngineService {

	RuntimeEngine newSingletonRuntimeEngine();

	RuntimeEngine newPerRequestRuntimeEngine();

	RuntimeEngine newPerProcessRuntimeEngine();

	RuntimeEngine newPerProcessRuntimeEngine(Long processInstanceId);

	RuntimeEngine newPerProcessRuntimeEngine(CorrelationKey key);

}
