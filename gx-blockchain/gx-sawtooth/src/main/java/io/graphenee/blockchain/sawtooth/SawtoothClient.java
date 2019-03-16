package io.graphenee.blockchain.sawtooth;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bitcoinj.core.ECKey;

import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.Batch;
import sawtooth.sdk.protobuf.BatchHeader;
import sawtooth.sdk.protobuf.BatchList;
import sawtooth.sdk.protobuf.Transaction;
import sawtooth.sdk.protobuf.TransactionHeader;
import sawtooth.sdk.signing.Context;
import sawtooth.sdk.signing.CryptoFactory;
import sawtooth.sdk.signing.PrivateKey;
import sawtooth.sdk.signing.Secp256k1PrivateKey;

public class SawtoothClient {

	private SawtoothRestService service;
	private ECKey transactionSignerECKey, batchSignerECKey;

	public SawtoothClient(String endpoint) {
		Retrofit.Builder rb = new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
				.addConverterFactory(ProtoConverterFactory.create());
		service = rb.baseUrl(endpoint).build().create(SawtoothRestService.class);
	}

	public void setSignerPrivateKey(byte[] keyAsBytes) {
		setTransactionSignerPrivateKey(keyAsBytes);
		setBatchSignerPrivateKey(keyAsBytes);
	}

	public void setTransactionSignerPrivateKey(byte[] keyAsBytes) {
		transactionSignerECKey = ECKey.fromPrivate(keyAsBytes);
	}

	public void setBatchSignerPrivateKey(byte[] keyAsBytes) {
		batchSignerECKey = ECKey.fromPrivate(keyAsBytes);
	}

	public void submitTransaction(String familyName, String version, Collection<String> inputAddress, Collection<String> outputAddress, byte[] payload) {
		try {
			Secp256k1PrivateKey transactionSignerPrivateKey = Secp256k1PrivateKey.fromHex(transactionSignerECKey.getPrivateKeyAsHex());
			Secp256k1PrivateKey batchSignerPrivateKey = Secp256k1PrivateKey.fromHex(batchSignerECKey.getPrivateKeyAsHex());
			Context ctx = CryptoFactory.createContext("secp256k1");

			// build transaction header
			String encodedPayload = Utils.hash512(payload);

			TransactionHeader transactionHeader = TransactionHeader.newBuilder().setFamilyName(familyName).setFamilyVersion(version)
					.setSignerPublicKey(transactionSignerECKey.getPublicKeyAsHex()).setBatcherPublicKey(batchSignerECKey.getPublicKeyAsHex()).setPayloadSha512(encodedPayload)
					.setNonce(UUID.randomUUID().toString()).addAllInputs(inputAddress).addAllOutputs(outputAddress).build();
			String transactionHeaderSignature = ctx.sign(transactionHeader.toByteArray(), transactionSignerPrivateKey);

			// build transaction
			Transaction transaction = Transaction.newBuilder().setHeader(transactionHeader.toByteString()).setHeaderSignature(transactionHeaderSignature)
					.setPayload(ByteString.copyFrom(payload)).build();

			// build batch header
			BatchHeader batchHeader = BatchHeader.newBuilder().setSignerPublicKey(batchSignerECKey.getPublicKeyAsHex()).addTransactionIds(transactionHeaderSignature).build();
			String batchHeaderSignature = ctx.sign(batchHeader.toByteArray(), batchSignerPrivateKey);

			// build batch
			Batch batch = Batch.newBuilder().setHeader(batchHeader.toByteString()).setHeaderSignature(batchHeaderSignature).addTransactions(transaction).build();

			// build batch list
			BatchList batchList = BatchList.newBuilder().addBatches(batch).build();

			RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"), batchList.toByteArray());
			Call<JsonObject> call = service.sendBatches(body);
			Response<JsonObject> response = call.execute();
			System.err.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SawtoothClient cli = new SawtoothClient("http://localhost:8008");
		//		cli.setSignerPrivateKey("d7f5894f14987b2bb56fdfed8c12e1cb40566bbc250a20c1e49a40ef4c050057".getBytes());
		//		cli.setSignerPrivateKey("41373efdf3ceeba60837b004aa6999f9d68e53cd8e26f4bb199e34a409e57216".getBytes());
		PrivateKey privateKey = CryptoFactory.createContext("secp256k1").newRandomPrivateKey();
		cli.setSignerPrivateKey(privateKey.getBytes());

		String address = Utils.hash512("adp".getBytes()).substring(0, 6) + Utils.hash512("farrukh".getBytes()).substring(0, 64);

		System.err.println(address);

		cli.submitTransaction("adp", "1.0", Arrays.asList(address), Arrays.asList(address), "Hello Universe!".getBytes());

		//		cli.submitTransaction("intkey", "1.0", null, null, "\\xa3eValue\\ndVerbcsetdNamedname".getBytes());
	}

}
