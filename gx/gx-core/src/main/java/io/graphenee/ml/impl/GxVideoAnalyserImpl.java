package io.graphenee.ml.impl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import io.graphenee.ml.GxAreaPatternCode;
import io.graphenee.ml.GxVideoAnalyser;
import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRParamCallback;

@Service
public class GxVideoAnalyserImpl implements GxVideoAnalyser {

	@Override
	public String analyse(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception {
		Integer lineCount = 0;
		String areaFlag = " -c " + areaCode.getCountryCode();
		//		areaFlag += " -p " + areaCode.getCountryCode();

		FileWriter myWriter = new FileWriter(jsonFilePath);
		ProcessBuilder pb = new ProcessBuilder("echo");

		String cmd = "alpr --motion " + mediaFilePath + areaFlag + " -j";
		String[] command = new String[] { "bash", "-l", "-c", cmd };
		pb = new ProcessBuilder(command);

		myWriter.write("[");

		Process pr = pb.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (lineCount != 0)
				myWriter.write("," + line);
			else
				myWriter.write(line);
			lineCount++;
		}
		myWriter.write("]");

		myWriter.close();
		reader.close();
		pr.waitFor();
		return jsonFilePath;
	}

	@Override
	public void analyseAsync(GxAreaPatternCode areaCode, String mediaFilePath, TRParamCallback<String> jsonCallback, TRErrorCallback errorCallback) {
		String areaFlag = " -c " + areaCode.getCountryCode();
		//		areaFlag += " -p " + areaCode.getCountryCode();

		ProcessBuilder pb = new ProcessBuilder("echo");

		String cmd = "alpr --motion " + mediaFilePath + areaFlag + " -j";
		String[] command = new String[] { "bash", "-l", "-c", cmd };
		pb = new ProcessBuilder(command);

		Process pr;
		try {
			pr = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					jsonCallback.execute(line);
				}
			} catch (IOException e) {
				errorCallback.execute(e);
			}

			reader.close();
			pr.waitFor();
		} catch (Exception e) {
			errorCallback.execute(e);
		}
	}

}
