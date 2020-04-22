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
package io.graphenee.core.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

public class TRImageUtil {

	private static final Logger L = LoggerFactory.getLogger(TRImageUtil.class);

	public static boolean resizeImage(File sourceFile, File targetFile) {
		try {
			// scale image on disk
			BufferedImage originalImage = ImageIO.read(sourceFile);
			if (originalImage.getWidth() < 1024)
				return false;
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImageJpg = resizeImage(originalImage, type, 1024);
			String ext = TRFileContentUtil.getExtensionFromFilename(sourceFile.getName());
			ImageIO.write(resizeImageJpg, ext, targetFile);
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static void compressImage(File sourceFile, File targetFile) {
		boolean compressed = false;
		try {
			String mimeType = TRFileContentUtil.getMimeType(sourceFile.getName());
			if (mimeType != null) {
				Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(mimeType);
				if (writers.hasNext()) {
					ImageWriter writer = writers.next();
					FileInputStream sourceStream = new FileInputStream(sourceFile);
					FileOutputStream targetStream = new FileOutputStream(targetFile);
					compressImage(sourceStream, targetStream, writer);
					compressed = true;
				}
			}
		} catch (Exception ex) {
			L.warn("Compression Failed:", ex);
		}
		if (!compressed) {
			try {
				FileCopyUtils.copy(sourceFile, targetFile);
			} catch (IOException ex) {
				L.warn("Compression Failed:", ex);
			}
		}
	}

	public static void compressImage(File sourceFile, OutputStream targetStream) {
		boolean compressed = false;
		try {
			String mimeType = TRFileContentUtil.getMimeType(sourceFile.getName());
			if (mimeType != null) {
				Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(mimeType);
				if (writers.hasNext()) {
					ImageWriter writer = writers.next();
					FileInputStream sourceStream = new FileInputStream(sourceFile);
					compressImage(sourceStream, targetStream, writer);
					compressed = true;
				}
			}
		} catch (Exception ex) {
			L.warn("Compression Failed:", ex);
		}
		if (!compressed) {
			try {
				StreamUtils.copy(new FileInputStream(sourceFile), targetStream);
			} catch (IOException ex) {
				L.warn("Compression Failed:", ex);
			}
		}
	}

	public static void compressImage(InputStream sourceStream, File targetFile) {
		boolean compressed = false;
		try {
			String mimeType = TRFileContentUtil.getMimeType(targetFile.getName());
			if (mimeType != null) {
				Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(mimeType);
				if (writers.hasNext()) {
					ImageWriter writer = writers.next();
					FileOutputStream targetStream = new FileOutputStream(targetFile);
					compressImage(sourceStream, targetStream, writer);
					compressed = true;
				}
			}
		} catch (Exception ex) {
			L.warn("Compression Failed:", ex);
		}
		if (!compressed) {
			try {
				StreamUtils.copy(sourceStream, new FileOutputStream(targetFile));
			} catch (IOException ex) {
				L.warn("Compression Failed:", ex);
			}
		}
	}

	public static void compressImage(InputStream sourceStream, OutputStream targetStream, ImageWriter imageWriter) {
		try {
			float imageQuality = 0.3f;

			//Create the buffered image
			BufferedImage bufferedImage = ImageIO.read(sourceStream);

			ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(targetStream);
			imageWriter.setOutput(imageOutputStream);

			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

			//Set the compress quality metrics
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality(imageQuality);

			//Created image
			imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

			// close all streams
			sourceStream.close();
			targetStream.close();
			imageOutputStream.close();
			imageWriter.dispose();
		} catch (Exception ex) {
			L.warn("Compression Failed:", ex);
		}
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer targetWidth) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		double aspectRatio = (height * 1.0) / (width * 1.0);
		int targetHeight = new Double(targetWidth * aspectRatio).intValue();
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g.dispose();

		return resizedImage;
	}

}
