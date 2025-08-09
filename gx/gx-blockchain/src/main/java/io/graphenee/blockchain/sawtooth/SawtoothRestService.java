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

/**
 * An interface for the Sawtooth REST API.
 */
public interface SawtoothRestService {

	/**
	 * Sends a batch list.
	 * @param data The batch list.
	 * @return The response.
	 */
	@Headers("Content-Type: application/octet-stream")
	@POST("/batches")
	Call<JsonObject> sendBatches(@Body RequestBody data);

	/**
	 * Fetches all batches.
	 * @return The response.
	 */
	@GET("/batches")
	Call<JsonObject> fetchBatches();

	/**
	 * Fetches all batches.
	 * @param head The head of the chain.
	 * @param start The start of the range.
	 * @param limit The limit of the range.
	 * @param reverse Whether to reverse the order.
	 * @return The response.
	 */
	@GET("/batches")
	Call<JsonObject> fetchBatches(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	/**
	 * Fetches a batch.
	 * @param batchId The ID of the batch.
	 * @return The response.
	 */
	@GET("/batches/{batch_id}")
	Call<JsonObject> fetchBatch(@Path("batch_id") String batchId);

	/**
	 * Fetches the statuses of batches.
	 * @param id The ID of the batch.
	 * @return The response.
	 */
	@GET("/batch_statuses")
	Call<JsonObject> fetchBatchStatuses(@Query("id") String id);

	/**
	 * Fetches the statuses of batches.
	 * @param id The ID of the batch.
	 * @param wait The time to wait.
	 * @return The response.
	 */
	@GET("/batch_statuses")
	Call<JsonObject> fetchBatchStatuses(@Query("id") String id, @Query("wait") Integer wait);

	/**
	 * Fetches the state.
	 * @return The response.
	 */
	@GET("/state")
	Call<JsonObject> fetchState();

	/**
	 * Fetches the state.
	 * @param address The address.
	 * @return The response.
	 */
	@GET("/state/{address}")
	Call<JsonObject> fetchState(@Path("address") String address);

	/**
	 * Fetches all blocks.
	 * @return The response.
	 */
	@GET("/blocks")
	Call<JsonObject> fetchBlocks();

	/**
	 * Fetches all blocks.
	 * @param head The head of the chain.
	 * @param start The start of the range.
	 * @param limit The limit of the range.
	 * @param reverse Whether to reverse the order.
	 * @return The response.
	 */
	@GET("/blocks")
	Call<JsonObject> fetchBlocks(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	/**
	 * Fetches a block.
	 * @param blockId The ID of the block.
	 * @return The response.
	 */
	@GET("/blocks/{block_id}")
	Call<JsonObject> fetchBlock(@Path("block_id") String blockId);

	/**
	 * Fetches all transactions.
	 * @return The response.
	 */
	@GET("/transactions")
	Call<JsonObject> fetchTransactions();

	/**
	 * Fetches all transactions.
	 * @param head The head of the chain.
	 * @param start The start of the range.
	 * @param limit The limit of the range.
	 * @param reverse Whether to reverse the order.
	 * @return The response.
	 */
	@GET("/transactions")
	Call<JsonObject> fetchTransactions(@Query("head") String head, @Query("start") String start, @Query("limit") Integer limit, @Query("reverse") Boolean reverse);

	/**
	 * Fetches a transaction.
	 * @param transactionId The ID of the transaction.
	 * @return The response.
	 */
	@GET("/transactions/{transaction_id}")
	Call<JsonObject> fetchTransaction(@Path("transaction_id") String transactionId);

	/**
	 * Fetches all receipts.
	 * @param id The ID of the transaction.
	 * @return The response.
	 */
	@GET("/receipts")
	Call<JsonObject> fetchReceipts(@Query("id") String id);

}
