package io.graphenee.workshop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;

import org.json.JSONObject;
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

		Map<WebSocketSession, String> sessionMap = new ConcurrentHashMap<>(new HashMap<>());

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			JSONObject json = new JSONObject(message.getPayload());
			String meetingId = json.getString("mid");
			System.err.println(json);
			// do whatever you want based on processing...
			for (WebSocketSession webSocketSession : sessionMap.keySet()) {
				if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
					String sessionMeetingId = sessionMap.get(webSocketSession);
					if(sessionMeetingId.equals(meetingId)) {
						webSocketSession.sendMessage(message);
					}
				}
			}
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			HttpHeaders headers = session.getHandshakeHeaders();
			String meetingId = headers.getFirst("X-MeetingId");
			if (meetingId == null) {
				String query = session.getUri().getQuery().trim();
				if (query.startsWith("mid=")) {
					meetingId = query.substring("mid=".length());
				}
			}
			// add session to sessions if meetingId is valid...
			if (!Strings.isNullOrEmpty(meetingId)) {
				sessionMap.put(session, meetingId);
			} else {
				session.close();
				throw new Exception("Invalid X-MeetingId");
			}
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			System.err.println("WebSocket connection closed: " + session.getId() + ", Status: " + status.getReason());
			sessionMap.remove(session);
		}

	}

}