package io.graphenee.aws.impl;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import io.graphenee.aws.api.GxS3Service;
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.storage.ResolveFailedException;
import io.graphenee.core.storage.SaveFailedException;

public class GxS3ServiceImpl implements GxS3Service, FileStorage {

	private AmazonS3 awsS3Client;
	private String rootBucket;

	public GxS3ServiceImpl(AWSCredentialsProvider credentialProvider, String rootBucket, String region) {
		this.rootBucket = rootBucket;
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withCredentials(credentialProvider).withRegion(region);
		if (region != null) {
			builder.withRegion(region);
		}
		awsS3Client = builder.build();
		if (!doesBucketExists(rootBucket)) {
			createRootBucket();
		}
	}

	private void createRootBucket() {
		Executors.newCachedThreadPool().submit(() -> {
			try {
				System.out.println("S3 root bucket: " + rootBucket + " created successfully....");
				awsS3Client.createBucket(rootBucket);
			} catch (Exception e) {
				System.err.println("Failed to create S3 root bucket \"" + rootBucket + "\": " + e.getMessage());
			}
		});
	}

	@Override
	public boolean doesBucketExists(String bucketName) {
		if (bucketName == null)
			return false;
		return awsS3Client.doesBucketExistV2(bucketName);
	}

	@Override
	public boolean doesObjectExist(String bucketName, String objectName) {
		if (bucketName == null || objectName == null)
			return false;
		return awsS3Client.doesObjectExist(bucketName, objectName);
	}

	@Override
	public boolean doesObjectExist(String objectName) {
		if (rootBucket == null || objectName == null)
			return false;
		return awsS3Client.doesObjectExist(rootBucket, objectName);
	}

	@Override
	public boolean exists(String resourcePath) {
		return doesObjectExist(resourcePath);
	}

	@Override
	public InputStream resolve(String resourcePath) throws ResolveFailedException {
		if (awsS3Client.doesObjectExist(rootBucket, resourcePath)) {
			try {
				S3Object o = awsS3Client.getObject(new GetObjectRequest(rootBucket, resourcePath));
				S3ObjectInputStream s3is = o.getObjectContent();
				return s3is;

			} catch (AmazonServiceException e) {
				throw new ResolveFailedException("Failed to resolve resource " + resourcePath, e);
			}
		} else {
			throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
		}
	}

	@Override
	public Future<FileMetaData> save(String folder, String fileName, InputStream inputStream) throws SaveFailedException {
		return Executors.newCachedThreadPool().submit(() -> {
			String resourcePath = resourcePath(folder, fileName);
			File resource = new File(rootBucket, resourcePath.replace('/', File.separatorChar));
			try {
				ObjectMetadata metaData = new ObjectMetadata();
				awsS3Client.putObject(rootBucket, resourcePath, inputStream, metaData);

				long fileSize = resource.length();
				// checksum will be computed in future...
				String checksum = null;
				FileMetaData fileMetaData = new FileMetaData(resourcePath, fileName, Long.valueOf(fileSize).intValue(), checksum);
				return fileMetaData;
			} catch (Exception e) {
				throw new SaveFailedException(e);
			}
		});
	}
}
