package de.marcelgerber.springboard.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for handling Server-Sent-Events
 */
@Service
public class EventService {

    private final ConcurrentHashMap<String, List<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    /**
     * Removes an emitter from the HashMap
     *
     * @param gameId String
     * @param emitter SseEmitter
     */
    private void removeEmitter(String gameId, SseEmitter emitter) {
        List<SseEmitter> emitterList = subscribers.get(gameId);
        emitterList.remove(emitter);

        if(emitterList.isEmpty()) {
            subscribers.remove(gameId);
        }
    }

    /**
     * Creates an SseEmitter
     *
     * @param gameId String
     * @return SseEmitter
     */
    public SseEmitter createEmitter(final String gameId) {
        // Set timeout to 300.000 ms = 5 minutes
        SseEmitter emitter = new SseEmitter(300000L);

        emitter.onCompletion(() -> removeEmitter(gameId, emitter));
        emitter.onTimeout(() -> removeEmitter(gameId, emitter));
        emitter.onError((error) -> removeEmitter(gameId, emitter));

        subscribers.computeIfAbsent(gameId, key -> new CopyOnWriteArrayList<>()).add(emitter);

        return emitter;
    }

    /**
     * Sends a move update to all subscribers of the game with 'gameId'
     *
     * @param gameId String
     * @param move String
     */
    public void sendMoveUpdate(String gameId, String move) {
        List<SseEmitter> emittersList = subscribers.getOrDefault(gameId, Collections.emptyList());
        List<SseEmitter> deadEmitters = new ArrayList<>();

        emittersList.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("move").data(move));
            } catch(IOException e) {
                // We don't know when the connection is closed on the client's side. When we want to send a
                // message to the client and get an error, we know that the connection has been closed client side
                deadEmitters.add(emitter);
            }
        });
        emittersList.removeAll(deadEmitters);
    }

    /**
     * Sends a player-joined update to all subscribers of the game with 'gameId'
     *
     * @param gameId String
     * @param username String
     */
    public void sendPlayerJoinedUpdate(String gameId, String username) {
        List<SseEmitter> emittersList = subscribers.getOrDefault(gameId, Collections.emptyList());
        List<SseEmitter> deadEmitters = new ArrayList<>();

        emittersList.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("join").data(username));
            } catch(IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emittersList.removeAll(deadEmitters);
    }

}
