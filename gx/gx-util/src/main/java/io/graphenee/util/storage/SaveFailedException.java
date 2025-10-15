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
package io.graphenee.util.storage;

/**
 * An exception that is thrown when a save fails.
 */
public class SaveFailedException extends Exception {

	/**
	 * Creates a new instance of this exception.
	 */
	public SaveFailedException() {
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 */
	public SaveFailedException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param cause The cause.
	 */
	public SaveFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 * @param cause The cause.
	 */
	public SaveFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance of this exception.
	 * @param message The message.
	 * @param cause The cause.
	 * @param enableSuppression Whether or not suppression is enabled or disabled.
	 * @param writableStackTrace Whether or not the stack trace should be writable.
	 */
	public SaveFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
