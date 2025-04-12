package com.sean.socket.channel;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/channel/echo")
public class EchoChannel {

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoChannel.class);

	// 用來儲存所有連線的 session
	private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

	private Session session;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		sessions.put(session.getId(), session);
		LOGGER.info("[websocket] 新的連線：id={}", session.getId());
	}

	@OnMessage
	public void onMessage(String message) throws IOException {
		LOGGER.info("[websocket] 收到訊息：id={}，message={}", this.session.getId(), message);

		if (message.equalsIgnoreCase("bye")) {
			this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Bye"));
			return;
		}

		// 廣播給其他人
		sessions.forEach((id, s) -> {
			if (!id.equals(this.session.getId()) && s.isOpen()) {
				s.getAsyncRemote().sendText("[" + this.session.getId() + "] 說：" + message);
			}
		});
	}


	@OnClose
	public void onClose(CloseReason closeReason) {
		LOGGER.info("[websocket] 連線關閉：id={}，reason={}", session.getId(), closeReason);
		sessions.remove(session.getId());
	}

	@OnError
	public void onError(Throwable throwable) throws IOException {
		LOGGER.error("[websocket] 發生錯誤：id={}，錯誤={}", session.getId(), throwable.getMessage());
		if (session.isOpen()) {
			session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
		}
		sessions.remove(session.getId());
	}
}

