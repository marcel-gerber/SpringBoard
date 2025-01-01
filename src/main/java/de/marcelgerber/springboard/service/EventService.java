package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.model.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for handling Server-Sent-Events
 */
@Service
public class EventService {

    private final ConcurrentHashMap<String, List<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    /**
     * Sends a message to the provided SseEmitter
     *
     * @param emitter {@link SseEmitter}
     */
    private void sendInitialMessage(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("connection").data("ok"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes an emitter from the HashMap
     *
     * @param gameId String
     * @param emitter SseEmitter
     */
    private void removeEmitter(String gameId, SseEmitter emitter) {
        subscribers.get(gameId).remove(emitter);

        if(subscribers.get(gameId).isEmpty()) {
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

        subscribers.computeIfAbsent(gameId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(gameId, emitter));
        emitter.onTimeout(() -> removeEmitter(gameId, emitter));
        emitter.onError((error) -> removeEmitter(gameId, emitter));

        sendInitialMessage(emitter);

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

        synchronized(emittersList) {
            Iterator<SseEmitter> iterator = emittersList.iterator();

            while(iterator.hasNext()) {
                SseEmitter emitter = iterator.next();
                try {
                    emitter.send(SseEmitter.event().name("move").data(move));
                } catch(IOException e) {
                    // We don't know when the connection is closed on the client's side. When we want to send a
                    // message to the client and get an error, we know that the connection has been closed client side
                    emitter.complete();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Sends a player-joined update to all subscribers of the game with 'gameId'
     *
     * @param gameId String
     * @param player Player
     */
    public void sendPlayerJoinedUpdate(String gameId, Player player) {
        List<SseEmitter> emittersList = subscribers.getOrDefault(gameId, Collections.emptyList());

        String data = player.getId() + ":" + player.getUsername();

        synchronized(emittersList) {
            Iterator<SseEmitter> iterator = emittersList.iterator();

            while(iterator.hasNext()) {
                SseEmitter emitter = iterator.next();
                try {
                    emitter.send(SseEmitter.event().name("join").data(data));
                } catch(IOException e) {
                    emitter.complete();
                    iterator.remove();
                }
            }
        }
    }

}
