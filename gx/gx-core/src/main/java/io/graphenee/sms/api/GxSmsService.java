package io.graphenee.sms.api;

import io.graphenee.sms.GxSmsResponse;
import io.graphenee.sms.GxSmsSendException;

public interface GxSmsService {

	GxSmsResponse sendTransactionalMessage(String phone, String message) throws GxSmsSendException;

	GxSmsResponse sendPromotionalMessage(String phone, String message) throws GxSmsSendException;

	GxSmsResponse sendTransactionalMessage(String senderId, String phone, String message) throws GxSmsSendException;

	GxSmsResponse sendPromotionalMessage(String senderId, String phone, String message) throws GxSmsSendException;

}
