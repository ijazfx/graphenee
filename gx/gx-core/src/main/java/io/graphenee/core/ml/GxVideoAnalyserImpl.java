package io.graphenee.core.ml;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.callback.TRErrorCallback;
import io.graphenee.core.callback.TRParamCallback;
import io.graphenee.core.enums.GxAreaPatternCode;
import io.graphenee.core.storage.FileStorage;

@Service
public class GxVideoAnalyserImpl implements GxVideoAnalyser {

	@Autowired
	FileStorage fileStorage;

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
					String jsonData = "[" + line + "]";
					jsonCallback.execute(jsonData);
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
