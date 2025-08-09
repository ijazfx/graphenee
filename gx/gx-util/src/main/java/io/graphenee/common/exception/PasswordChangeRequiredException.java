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
package io.graphenee.common.exception;

/**
 * An exception that is thrown when a password change is required.
 */
@SuppressWarnings("serial")
public class PasswordChangeRequiredException extends Exception {

	/**
	 * Creates a new instance of this exception.
	 */
	public PasswordChangeRequiredException() {
		super();
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 * @param cause The cause.
	 * @param enableSuppression Whether or not suppression is enabled or disabled.
	 * @param writableStackTrace Whether or not the stack trace should be writable.
	 */
	public PasswordChangeRequiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 * @param cause The cause.
	 */
	public PasswordChangeRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 */
	public PasswordChangeRequiredException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param cause The cause.
	 */
	public PasswordChangeRequiredException(Throwable cause) {
		super(cause);
	}

}