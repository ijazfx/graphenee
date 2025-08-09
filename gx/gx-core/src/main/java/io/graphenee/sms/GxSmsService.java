package io.graphenee.sms;

import io.graphenee.sms.exception.GxSmsSendException;

/**
 * An interface for sending SMS messages.
 */
public interface GxSmsService {

	/**
	 * Sends a transactional message.
	 * @param phone The phone number to send the message to.
	 * @param message The message to send.
	 * @return The response.
	 * @throws GxSmsSendException If an error occurs.
	 */
	GxSmsResponse sendTransactionalMessage(String phone, String message) throws GxSmsSendException;

	/**
	 * Sends a promotional message.
	 * @param phone The phone number to send the message to.
	 * @param message The message to send.
	 * @return The response.
	 * @throws GxSmsSendException If an error occurs.
	 */
	GxSmsResponse sendPromotionalMessage(String phone, String message) throws GxSmsSendException;

	/**
	 * Sends a transactional message.
	 * @param senderId The sender ID.
	 * @param phone The phone number to send the message to.
	 * @param message The message to send.
	 * @return The response.
	 * @throws GxSmsSendException If an error occurs.
	 */
	GxSmsResponse sendTransactionalMessage(String senderId, String phone, String message) throws GxSmsSendException;

	/**
	 * Sends a promotional message.
	 * @param senderId The sender ID.
	 * @param phone The phone number to send the message to.
	 * @param message The message to send.
	 * @return The response.
	 * @throws GxSmsSendException If an error occurs.
	 */
	GxSmsResponse sendPromotionalMessage(String senderId, String phone, String message) throws GxSmsSendException;

}
