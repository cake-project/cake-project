package com.cakemate.cake_platform.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public Optional<SseEmitter> findById(String emitterId) {
        return Optional.ofNullable(emitters.get(emitterId));
    }

    @Override
    public void deleteAllEmitterStartWithId(String emitterId) {
        emitters.keySet().stream()
                .filter(key -> key.startsWith(emitterId))
                .forEach(emitters::remove);
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
    }
}
