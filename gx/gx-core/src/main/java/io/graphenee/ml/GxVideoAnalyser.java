package io.graphenee.ml;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRParamCallback;

public interface GxVideoAnalyser {

	String analyse(GxAreaPatternCode areaCode, String mediaFilePath, String jsonFilePath) throws Exception;

	void analyseAsync(GxAreaPatternCode areaCode, String mediaFilePath, TRParamCallback<String> jsonCallback, TRErrorCallback errorCallback);

}
