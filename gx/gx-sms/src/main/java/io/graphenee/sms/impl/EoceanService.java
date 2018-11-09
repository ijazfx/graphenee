package io.graphenee.sms.impl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EoceanService {

	@GET("/APIManagement/API/RequestAPI?response=json")
	Call<String> requestAPI(@Query("user") String user, @Query("pwd") String password, @Query("sender") String sender, @Query("reciever") String receiver,
			@Query("msg-data") String message);

}
