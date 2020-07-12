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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.google.common.base.Strings;

import org.springframework.stereotype.Service;

import io.graphenee.core.enums.GxAudioType;
import io.graphenee.core.enums.GxImageType;
import io.graphenee.core.enums.GxVideoType;
import io.graphenee.core.exception.GxMediaConversionException;
import io.graphenee.core.util.TRFileContentUtil;

/**
 * 
 * @author muhammadhamza
 * @since 2.0.0
 */
@Service
public class GxFfmpegMediaConverterImpl implements GxMediaConverter {
	@Override
	public void convertAudioMedia(String sourceFile, GxAudioType sourceType, String targetFile, GxAudioType targetType)
			throws GxMediaConversionException {
		if (sourceType.equals(targetType))
			throw new GxMediaConversionException(
					"Both source and target types are same thus conversion is not required.");
		convertAudioMedia(sourceFile, targetFile, targetType);
	}

	@Override
	public void convertVideoMedia(String sourceFile, GxVideoType sourceType, String targetFile, GxVideoType targetType)
			throws GxMediaConversionException {
		if (sourceType.equals(targetType))
			throw new GxMediaConversionException(
					"Both source and target types are same thus conversion is not required.");
		convertVideoMedia(sourceFile, targetFile, targetType);
	}

	@Override
	public void convertImageMedia(String sourceFile, GxImageType sourceType, String targetFile, GxImageType targetType)
			throws GxMediaConversionException {
		if (sourceType.equals(targetType))
			throw new GxMediaConversionException(
					"Both source and target types are same thus conversion is not required.");
		convertImageMedia(sourceFile, targetFile, targetType);
	}

	@Override
	public void convertAudioMedia(String sourceFile, String targetFile, GxAudioType targetType)
			throws GxMediaConversionException {
		File source = new File(sourceFile);
		if (!source.exists())
			throw new GxMediaConversionException(sourceFile + " does not exist.");
		String targetExtension = TRFileContentUtil.getExtensionFromFilename(targetFile);
		if (!targetExtension.equals(targetType.getExtension()))
			throw new GxMediaConversionException(
					targetFile + " file extension does not match with target type " + targetType.getExtension());
		String cmd = "ffmpeg -y -i " + sanitize(sourceFile) + " -vn " + sanitize(targetFile);
		convertMedia(cmd);
	}

	@Override
	public void convertVideoMedia(String sourceFile, String targetFile, GxVideoType targetType)
			throws GxMediaConversionException {
		File source = new File(sourceFile);
		if (!source.exists())
			throw new GxMediaConversionException(sourceFile + " does not exist.");
		String targetExtension = TRFileContentUtil.getExtensionFromFilename(targetFile);
		if (!targetExtension.equals(targetType.getExtension()))
			throw new GxMediaConversionException(
					targetFile + " file extension does not match with target type " + targetType.getExtension());
		String mpegFlag = "-vf scale=\"720:-1\"";
		if (targetType.equals(GxVideoType.MPEG)) {
			mpegFlag = mpegFlag + " -c:v mpeg2video";
		}
		String cmd = "ffmpeg -y -i " + sanitize(sourceFile) + " " + mpegFlag + " " + sanitize(targetFile);
		convertMedia(cmd);
	}

	@Override
	public void convertImageMedia(String sourceFile, String targetFile, GxImageType targetType)
			throws GxMediaConversionException {
		File source = new File(sourceFile);
		if (!source.exists())
			throw new GxMediaConversionException(sourceFile + " does not exist.");
		String targetExtension = TRFileContentUtil.getExtensionFromFilename(targetFile);
		if (!targetExtension.equals(targetType.getExtension()))
			throw new GxMediaConversionException(
					targetFile + " file extension does not match with target type " + targetType.getExtension());
		String cmd = "ffmpeg -y -i " + sanitize(sourceFile) + " " + sanitize(targetFile);
		convertMedia(cmd);
	}

	protected void convertMedia(String cmd) throws GxMediaConversionException {
		try {
			// Process process = Runtime.getRuntime().exec(command, null, null);
			ProcessBuilder pb = new ProcessBuilder("echo");
			String[] command = new String[] { "bash", "-l", "-c", cmd };
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				String ffmpegBinary = pb.environment().get("FFMPEG_BINARY");
				System.err.println(ffmpegBinary);
				if (!Strings.isNullOrEmpty(ffmpegBinary)) {
					cmd = cmd.replaceFirst("ffmpeg", sanitize(ffmpegBinary));
				} 
			}
			command = new String[] { cmd };
			pb = new ProcessBuilder(command);
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			process.waitFor();
		} catch (Exception e) {
			throw new GxMediaConversionException(e);
		}
	}

	private String sanitize(String value) {
		return "\"" + value.replaceAll("\\\\", "\\\\\\\\")  + "\"";
	}

}
