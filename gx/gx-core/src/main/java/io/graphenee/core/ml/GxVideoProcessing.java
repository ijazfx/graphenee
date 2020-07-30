package io.graphenee.core.ml;

import io.graphenee.core.enums.GxAreaPatternCode;

public interface GxVideoProcessing {

	String getNumberPlatesConfigurationFileByCountryFromMedia(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception;

}
