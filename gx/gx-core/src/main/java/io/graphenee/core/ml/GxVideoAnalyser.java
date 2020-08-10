package io.graphenee.core.ml;

import io.graphenee.core.enums.GxAreaPatternCode;

public interface GxVideoAnalyser {

	String getNumberPlatesConfigurationFileByCountryFromMedia(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception;

}
