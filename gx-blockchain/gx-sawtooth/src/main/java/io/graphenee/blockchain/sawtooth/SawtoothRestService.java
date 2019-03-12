package io.graphenee.blockchain.sawtooth;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SawtoothRestService {

	@Headers("Content-Type: application/octet-stream")
	@POST("/batches")
	Call<JsonObject> sendBatches(@Body RequestBody data);

	@GET("/batches")
	Call<JsonObject> fetchBatches();

	@GET("/batches")
	Call<JsonObject> fetchBatches(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	@GET("/batches/{batch_id}")
	Call<JsonObject> fetchBatch(@Path("batch_id") String batchId);

	@GET("/batch_statuses")
	Call<JsonObject> fetchBatchStatuses(@Query("id") String id);

	@GET("/batch_statuses")
	Call<JsonObject> fetchBatchStatuses(@Query("id") String id, @Query("wait") Integer wait);

	@GET("/state")
	Call<JsonObject> fetchState();

	@GET("/state/{address}")
	Call<JsonObject> fetchState(@Path("address") String address);

	@GET("/blocks")
	Call<JsonObject> fetchBlocks();

	@GET("/blocks")
	Call<JsonObject> fetchBlocks(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	@GET("/blocks/{block_id}")
	Call<JsonObject> fetchBlock(@Path("block_id") String blockId);

	@GET("/transactions")
	Call<JsonObject> fetchTransactions();

	@GET("/transactions")
	Call<JsonObject> fetchTransactions(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	@GET("/transactions/{transaction_id}")
	Call<JsonObject> fetchTransaction(@Path("transaction_id") String transactionId);

	@GET("/receipts")
	Call<JsonObject> fetchReceipts(@Query("id") String id);

}
