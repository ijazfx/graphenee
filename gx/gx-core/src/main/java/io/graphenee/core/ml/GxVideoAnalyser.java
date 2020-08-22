package io.graphenee.core.ml;

import io.graphenee.core.callback.TRErrorCallback;
import io.graphenee.core.callback.TRParamCallback;
import io.graphenee.core.enums.GxAreaPatternCode;

public interface GxVideoAnalyser {

	String analyse(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception;

	void analyseAsync(GxAreaPatternCode areaCode, String mediaFilePath, TRParamCallback<String> jsonCallback, TRErrorCallback errorCallback);

}
