package io.graphenee.workshop;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.google.common.base.Strings;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(65536);
		container.setMaxBinaryMessageBufferSize(65536);
		return container;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketHandler(), "/socket").setAllowedOrigins("*");
	}

	class SocketHandler extends TextWebSocketHandler {

		List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			// do whatever you want based on processing...
			for (WebSocketSession webSocketSession : sessions) {
				if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
					webSocketSession.sendMessage(message);
				}
			}
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			HttpHeaders headers = session.getHandshakeHeaders();
			String authToken = headers.getFirst("X-AuthToken");
			if (authToken == null) {
				String query = session.getUri().getQuery().trim();
				if (query.startsWith("authToken=")) {
					authToken = query.substring("authToken=".length());
				}
			}
			// add session to sessions if token is valid...
			if (!Strings.isNullOrEmpty(authToken)) {
				sessions.add(session);
			} else {
				session.close();
				throw new Exception("Invalid X-AuthToken");
			}
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			System.err.println("WebSocket connection closed: " + session.getId() + ", Status: " + status.getReason());
			sessions.remove(session);
		}

	}

}