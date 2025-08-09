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
package io.graphenee.jbpm.embedded;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.internal.process.CorrelationKey;

/**
 * An interface for KIE runtime engine services.
 */
public interface GxKieRuntimeEngineService {

	/**
	 * The default group for the jBPM manager.
	 */
	public static final String JBPM_MANAGER_DEFAULT_GROUP = "default-singleton";

	/**
	 * Creates a new singleton runtime engine.
	 * @return The new runtime engine.
	 */
	RuntimeEngine newSingletonRuntimeEngine();

	/**
	 * Creates a new per-request runtime engine.
	 * @return The new runtime engine.
	 */
	RuntimeEngine newPerRequestRuntimeEngine();

	/**
	 * Creates a new per-process runtime engine.
	 * @return The new runtime engine.
	 */
	RuntimeEngine newPerProcessRuntimeEngine();

	/**
	 * Creates a new per-process runtime engine.
	 * @param processInstanceId The process instance ID.
	 * @return The new runtime engine.
	 */
	RuntimeEngine newPerProcessRuntimeEngine(Long processInstanceId);

	/**
	 * Creates a new per-process runtime engine.
	 * @param key The correlation key.
	 * @return The new runtime engine.
	 */
	RuntimeEngine newPerProcessRuntimeEngine(CorrelationKey key);

}
