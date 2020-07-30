package io.graphenee.core.ml;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.enums.GxAreaPatternCode;
import io.graphenee.core.storage.FileStorage;

@Service
public class GxVideoProcessingImpl implements GxVideoProcessing {

	@Autowired
	FileStorage fileStorage;

	@Override
	public String getNumberPlatesConfigurationFileByCountryFromMedia(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception {
		Integer lineCount = 0;
		String areaFlag = " -c " + areaCode.getCountryCode();
		areaFlag += " -p " + areaCode.getCountryCode();

		FileWriter myWriter = new FileWriter(jsonFilePath);
		ProcessBuilder pb = new ProcessBuilder("echo");

		String cmd = "alpr " + mediaFilePath + areaFlag + " -j";
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

}
