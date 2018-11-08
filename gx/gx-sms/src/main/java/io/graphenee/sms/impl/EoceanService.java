package io.graphenee.sms.impl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EoceanService {

	@GET("/APIManagement/API/RequestAPI?response=string")
	Call<Void> requestAPI(@Query("user") String user, @Query("pwd") String password, @Query("sender") String sender, @Query("receiver") String receiver,
			@Query("msg-data") String message);

}
