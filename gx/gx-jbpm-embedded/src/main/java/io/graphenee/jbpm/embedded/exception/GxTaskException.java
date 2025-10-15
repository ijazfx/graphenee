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
package io.graphenee.jbpm.embedded.exception;

/**
 * An exception that is thrown when a task fails.
 */
public class GxTaskException extends Exception {

	/**
	 * Creates a new instance of this exception.
	 * @param e The cause of this exception.
	 */
	public GxTaskException(Exception e) {
		super(e);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message of this exception.
	 * @param cause The cause of this exception.
	 */
	public GxTaskException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message of this exception.
	 */
	public GxTaskException(String message) {
		super(message);
	}

}
