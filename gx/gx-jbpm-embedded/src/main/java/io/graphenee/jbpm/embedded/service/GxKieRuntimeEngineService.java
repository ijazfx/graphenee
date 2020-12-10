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
package io.graphenee.jbpm.embedded.service;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.internal.process.CorrelationKey;

public interface GxKieRuntimeEngineService {

	public static final String JBPM_MANAGER_DEFAULT_GROUP = "default-singleton";

	RuntimeEngine newSingletonRuntimeEngine();

	RuntimeEngine newPerRequestRuntimeEngine();

	RuntimeEngine newPerProcessRuntimeEngine();

	RuntimeEngine newPerProcessRuntimeEngine(Long processInstanceId);

	RuntimeEngine newPerProcessRuntimeEngine(CorrelationKey key);

}
