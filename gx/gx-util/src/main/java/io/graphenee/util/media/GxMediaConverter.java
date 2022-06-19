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

public interface GxMediaConverter {

	void convertAudioMedia(String sourceFile, String targetFile, GxAudioType targetType) throws GxMediaConversionException;

	void convertAudioMedia(String sourceFile, GxAudioType sourceType, String targetFile, GxAudioType targetType) throws GxMediaConversionException;

	void convertVideoMedia(String sourceFile, String targetFile, GxVideoType targetType) throws GxMediaConversionException;

	void convertVideoMedia(String sourceFile, GxVideoType sourceType, String targetFile, GxVideoType targetType) throws GxMediaConversionException;

	void convertImageMedia(String sourceFile, String targetFile, GxImageType targetType) throws GxMediaConversionException;

	void convertImageMedia(String sourceFile, GxImageType sourceType, String targetFile, GxImageType targetType) throws GxMediaConversionException;

	void compressImageMedia(String sourceFile, String targetFile, Integer quality) throws GxMediaConversionException;

}
