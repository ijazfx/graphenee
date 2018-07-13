package io.graphenee.core.retrofit;

import java.sql.Timestamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.graphenee.core.gson.JsonDeserializers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

	public static <T> T build(Class<T> interfaceType, String baseUrl) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Timestamp.class, JsonDeserializers.TIMESTAMP_DESERIALIZER);
		Gson gson = builder.create();
		Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson)).build();
		return retrofit.create(interfaceType);

	}

}
