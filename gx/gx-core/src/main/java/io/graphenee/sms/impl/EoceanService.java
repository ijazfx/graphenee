package io.graphenee.sms.impl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * An interface for the Eocean SMS service.
 */
public interface EoceanService {

	/**
	 * Sends a request to the Eocean API.
	 * @param user The user.
	 * @param password The password.
	 * @param sender The sender.
	 * @param receiver The receiver.
	 * @param message The message.
	 * @return The response.
	 */
	@GET("/APIManagement/API/RequestAPI?response=json")
	Call<String> requestAPI(@Query("user") String user, @Query("pwd") String password, @Query("sender") String sender, @Query("reciever") String receiver,
			@Query("msg-data") String message);

}
