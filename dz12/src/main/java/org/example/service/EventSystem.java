package org.example.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventSystem {
    // Быстрый доступ к данным по ключу без блокировок всего Map
    private final ConcurrentHashMap<String, String> sessionCache = new ConcurrentHashMap<>();

    // Безопасный обход списка подписчиков при редких изменениях
    private final List<String> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(String listenerName) {
        listeners.add(listenerName);
    }

    public void processEvent(String userId, String action) {
        sessionCache.put(userId, "Last action: " + action + " at " + System.currentTimeMillis());

        listeners.forEach(listener ->
                System.out.println("[" + Thread.currentThread().getName() + "] Уведомление для " +
                        listener + ": Пользователь " + userId + " выполнил " + action));
    }

    public String getSessionInfo(String userId) {
        return sessionCache.getOrDefault(userId, "No data");
    }
}