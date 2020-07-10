package io.graphenee.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import io.graphenee.aws.impl.GxS3ServiceImpl;

public class GxS3ServiceBuilder {

	private AWSCredentialsProvider credentialsProvider;
	private String region;
	private String rootBucket;

	public static GxS3ServiceBuilder newBuilder() {
		return new GxS3ServiceBuilder();
	}

	public GxS3ServiceBuilder withCredentials(String accessKey, String secretKey) {
		credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
		return this;
	}

	public GxS3ServiceBuilder withCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
		return this;
	}

	public GxS3ServiceBuilder withRegion(String region) {
		this.region = region;
		return this;
	}

	public GxS3ServiceBuilder withRootBucket(String rootBucket) {
		this.rootBucket = rootBucket;
		return this;
	}

	public GxS3ServiceImpl build() {
		if (credentialsProvider == null)
			throw new IllegalStateException("Please provide aws credentials.");
		return new GxS3ServiceImpl(credentialsProvider, rootBucket, region);
	}

}