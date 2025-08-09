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
 * An exception that is thrown when a password change fails.
 */
@SuppressWarnings("serial")
public class ChangePasswordFailedException extends Exception {

	/**
	 * Constructs a new exception with {@code null} as its detail message.
	 */
	public ChangePasswordFailedException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
	 * @param message the detail message.
	 * @param cause the cause.
	 * @param enableSuppression whether or not suppression is enabled or disabled.
	 * @param writableStackTrace whether or not the stack trace should be writable.
	 */
	public ChangePasswordFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * @param message the detail message.
	 * @param cause the cause.
	 */
	public ChangePasswordFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * @param message the detail message.
	 */
	public ChangePasswordFailedException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 * @param cause the cause.
	 */
	public ChangePasswordFailedException(Throwable cause) {
		super(cause);
	}

}