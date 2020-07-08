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
package io.graphenee.media;

import io.graphenee.core.enums.GxAudioType;
import io.graphenee.core.enums.GxImageType;
import io.graphenee.core.enums.GxVideoType;
import io.graphenee.core.exception.GxMediaConversionException;

public interface GxFfmpegMediaConverter {

	void convertAudioMedia(String sourceFilepath, String destinationFilepath, GxAudioType sourceType, GxAudioType destinationType) throws GxMediaConversionException;

	void convertVideoMedia(String sourceFilepath, String destinationFilepath, GxVideoType sourceType, GxVideoType destinationType) throws GxMediaConversionException;

	void convertImageMedia(String sourceFilepath, String destinationFilepath, GxImageType sourceType, GxImageType destinationType) throws GxMediaConversionException;

}
