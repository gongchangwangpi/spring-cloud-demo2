package com.zb.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
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
@Component
@ServerEndpoint(value = "/api/users/tomcat_websocket/{id}")
public class TomcatWebsocket {

    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Session>> holder = new ConcurrentHashMap<>();

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
                            session.getBasicRemote().sendText(profiles + " --- " + count.getAndIncrement());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    @OnOpen
    public void open(@PathParam("id") String userId, Session session) {
        log.info("【open】userId = {}, sessionId = {}", userId, session.getId());
        holder.compute(userId, (k, v) -> {
            if (v == null) {
                v = new CopyOnWriteArrayList<>();
            }
            v.add(session);
            return v;
        });
    }

    @OnMessage
    public void message(@PathParam("id") String userId, Session session, String message) {
        log.info("【message】userId = {}, sessionId = {}, message = {}", userId, session.getId(), message);
    }

    @OnError
    public void error(@PathParam("id") String userId, Session session, Throwable throwable) {
        log.error("【error】userId = {}, sessionId = {}", userId, session.getId(), throwable);
        if (!session.isOpen()) {
            holder.compute(userId, (k, v) -> {
                if (v != null) {
                    v.remove(session);
                }
                return v;
            });
        }
    }

    @OnClose
    public void close(@PathParam("id") String userId, Session session) {
        log.info("【close】userId = {}, sessionId = {}", userId, session.getId());
        holder.compute(userId, (k, v) -> {
            if (v != null) {
                v.remove(session);
            }
            return v;
        });
    }


}
