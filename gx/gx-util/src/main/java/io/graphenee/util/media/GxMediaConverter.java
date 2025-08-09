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
package io.graphenee.util.media;

import io.graphenee.util.enums.GxAudioType;
import io.graphenee.util.enums.GxImageType;
import io.graphenee.util.enums.GxVideoType;
import io.graphenee.util.exception.GxMediaConversionException;

/**
 * An interface for media converters.
 */
public interface GxMediaConverter {

	/**
	 * Converts an audio file.
	 * @param sourceFile The source file.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertAudioMedia(String sourceFile, String targetFile, GxAudioType targetType) throws GxMediaConversionException;

	/**
	 * Converts an audio file.
	 * @param sourceFile The source file.
	 * @param sourceType The source type.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertAudioMedia(String sourceFile, GxAudioType sourceType, String targetFile, GxAudioType targetType) throws GxMediaConversionException;

	/**
	 * Converts a video file.
	 * @param sourceFile The source file.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertVideoMedia(String sourceFile, String targetFile, GxVideoType targetType) throws GxMediaConversionException;

	/**
	 * Converts a video file.
	 * @param sourceFile The source file.
	 * @param sourceType The source type.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertVideoMedia(String sourceFile, GxVideoType sourceType, String targetFile, GxVideoType targetType) throws GxMediaConversionException;

	/**
	 * Converts an image file.
	 * @param sourceFile The source file.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertImageMedia(String sourceFile, String targetFile, GxImageType targetType) throws GxMediaConversionException;

	/**
	 * Converts an image file.
	 * @param sourceFile The source file.
	 * @param sourceType The source type.
	 * @param targetFile The target file.
	 * @param targetType The target type.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void convertImageMedia(String sourceFile, GxImageType sourceType, String targetFile, GxImageType targetType) throws GxMediaConversionException;

	/**
	 * Compresses an image file.
	 * @param sourceFile The source file.
	 * @param targetFile The target file.
	 * @param quality The quality.
	 * @throws GxMediaConversionException If an error occurs.
	 */
	void compressImageMedia(String sourceFile, String targetFile, Integer quality) throws GxMediaConversionException;

}
