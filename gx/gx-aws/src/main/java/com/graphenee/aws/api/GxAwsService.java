package com.graphenee.aws.api;

public interface GxAwsService {

	String sendTransactionalSMSMessage(String phone, String message);

	String sendPromotionalSMSMessage(String phone, String message);

}
