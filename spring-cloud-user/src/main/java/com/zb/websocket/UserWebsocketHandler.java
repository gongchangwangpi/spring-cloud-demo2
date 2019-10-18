package com.zb.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangbo
 * @date 2019-10-17
 */
@Slf4j
public class UserWebsocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSession>> holder = new ConcurrentHashMap<>();

    @Value("${spring.profiles:default}")
    private String profiles;

    private AtomicInteger count = new AtomicInteger(0);

    @PostConstruct
    public void send() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
        threadPool.scheduleAtFixedRate(() -> {
            if (!holder.isEmpty()) {
                holder.forEach((k, v) -> {
                    v.forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(profiles + " --- " + count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        holder.compute(userId, (k, v) -> {
            if (v == null) {
                v = new CopyOnWriteArrayList<>();
            }
            v.add(session);
            return v;
        });
    }

    private String getUserId(WebSocketSession session) {
        URI uri = session.getUri();
        String[] pathArr = uri.getPath().split("/");
        return pathArr[pathArr.length - 1];
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("{} --->>> receive text message: {}", profiles, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        holder.compute(userId, (k, v) -> {
            if (v != null) {
                v.remove(session);
            }
            return v;
        });
    }
}
