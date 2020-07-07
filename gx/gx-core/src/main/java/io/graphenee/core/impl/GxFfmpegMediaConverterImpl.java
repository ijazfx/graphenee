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
package io.graphenee.core.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import io.graphenee.core.api.GxFfmpegMediaConverter;
import io.graphenee.core.enums.GxAudioType;
import io.graphenee.core.enums.GxImageType;
import io.graphenee.core.enums.GxVideoType;
import io.graphenee.core.exception.GxUnsupportedMediaConversionException;

@Service
public class GxFfmpegMediaConverterImpl implements GxFfmpegMediaConverter {
	@Override
	public void convertAudioMedia(String filepath, GxAudioType sourceType, GxAudioType destinationType) throws Exception {
		String[] extension = filepath.split("\\.");
		String outputFile;
		if (extension[1] == null || extension[1].equals(sourceType.getExtension())) {
			outputFile = extension[0] + "." + destinationType.getExtension();
			String cmd = "ffmpeg -y -i " + filepath + " -vn " + outputFile;
			String[] command = new String[] { "bash", "-l", "-c", cmd };
			File file = new File(filepath);
			if (file.exists() && file.isFile()) {
				try {

					Process process = Runtime.getRuntime().exec(command, null, null);
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
					process.destroy();

				} catch (Exception e) {
					throw new GxUnsupportedMediaConversionException(e);
				}
			} else
				throw new GxUnsupportedMediaConversionException("File not found in the specific path (" + filepath + ")");
		} else
			throw new GxUnsupportedMediaConversionException("Source extension do not match file extension");

	}

	@Override
	public void convertVideoMedia(String filepath, GxVideoType sourceType, GxVideoType destinationType) throws Exception {
		String[] extension = filepath.split("\\.");
		String outputFile;
		if (extension[1] == null || extension[1].equals(sourceType.getExtension())) {
			outputFile = extension[0] + "." + destinationType.getExtension();
			String mpegFlag = "";
			if (destinationType.equals(GxVideoType.MPEG)) {
				mpegFlag = " -c:v mpeg2video ";
			}
			String cmd = "ffmpeg -y -i " + filepath + mpegFlag + " " + outputFile;
			String[] command = new String[] { "bash", "-l", "-c", cmd };
			File file = new File(filepath);
			if (file.exists() && file.isFile()) {
				try {

					Process process = Runtime.getRuntime().exec(command, null, null);
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
					process.waitFor();
					process.destroy();

				} catch (Exception e) {
					throw new GxUnsupportedMediaConversionException(e);
				}
			} else
				throw new GxUnsupportedMediaConversionException("File not found in the specific path (" + filepath + ")");
		} else
			throw new GxUnsupportedMediaConversionException("Source extension do not match with file extension");

	}

	@Override
	public void convertImageMedia(String filepath, GxImageType sourceType, GxImageType destinationType) throws Exception {
		String[] extension = filepath.split("\\.");
		String outputFile;
		if (extension[1] == null || extension[1].equals(sourceType.getExtension())) {
			outputFile = extension[0] + "." + destinationType.getExtension();
			String cmd = "ffmpeg -y -i " + filepath + " " + outputFile;
			String[] command = new String[] { "bash", "-l", "-c", cmd };
			File file = new File(filepath);
			if (file.exists() && file.isFile()) {
				try {

					Process process = Runtime.getRuntime().exec(command, null, null);
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					reader.close();
					process.waitFor();
					process.destroy();

				} catch (Exception e) {
					throw new GxUnsupportedMediaConversionException(e);
				}
			} else
				throw new GxUnsupportedMediaConversionException("File not found in the specific path (" + filepath + ")");
		} else
			throw new GxUnsupportedMediaConversionException("Source extension do not match with file extension");

	}

}
