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

/**
 * An interface for KIE services.
 */
public interface GxKieService {

	/**
	 * Gets the value of a variable.
	 * @param processInstanceId The process instance ID.
	 * @param variableName The name of the variable.
	 * @return The value of the variable.
	 */
	public Object getVariableValue(Long processInstanceId, String variableName);

	/**
	 * Gets the value of a variable.
	 * @param processInstanceId The process instance ID.
	 * @param variableName The name of the variable.
	 * @param defaultValue The default value.
	 * @return The value of the variable.
	 */
	public Object getVariableValue(Long processInstanceId, String variableName, Object defaultValue);

	/**
	 * Gets the value of a variable.
	 * @param <T> The type of the variable.
	 * @param processInstanceId The process instance ID.
	 * @param variableName The name of the variable.
	 * @param variableType The type of the variable.
	 * @return The value of the variable.
	 */
	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType);

	/**
	 * Gets the value of a variable.
	 * @param <T> The type of the variable.
	 * @param processInstanceId The process instance ID.
	 * @param variableName The name of the variable.
	 * @param variableType The type of the variable.
	 * @param defaultValue The default value.
	 * @return The value of the variable.
	 */
	public <T> T getVariableValue(Long processInstanceId, String variableName, Class<? extends T> variableType, T defaultValue);

	/**
	 * Gets the value of a variable as a long.
	 * @param processInstanceId The process instance ID.
	 * @param variableName The name of the variable.
	 * @param defaultValue The default value.
	 * @return The value of the variable.
	 */
	public Long getVariableValueAsLong(Long processInstanceId, String variableName, Long defaultValue);

}
