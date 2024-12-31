package de.marcelgerber.springboard.service;

import de.marcelgerber.springboard.model.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling Server-Sent-Events
 */
@Service
public class EventService {

    private final HashMap<String, ArrayList<SseEmitter>> emitters = new HashMap<>();

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
        emitters.get(gameId).remove(emitter);

        if(emitters.get(gameId).isEmpty()) {
            emitters.remove(gameId);
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

        if(!emitters.containsKey(gameId)) {
            emitters.put(gameId, new ArrayList<>());
        }
        emitters.get(gameId).add(emitter);

        emitter.onCompletion(() -> removeEmitter(gameId, emitter));
        emitter.onTimeout(() -> removeEmitter(gameId, emitter));

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
        ArrayList<SseEmitter> emittersList = emitters.get(gameId);
        if(emittersList == null || emittersList.isEmpty()) return;

        synchronized(emittersList) {
            emittersList.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("move").data(move));
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Sends a player-joined update to all subscribers of the game with 'gameId'
     *
     * @param gameId String
     * @param player Player
     */
    public void sendPlayerJoinedUpdate(String gameId, Player player) {
        ArrayList<SseEmitter> emittersList = emitters.get(gameId);
        if(emittersList == null || emittersList.isEmpty()) return;

        String data = player.getId() + ":" + player.getUsername();

        synchronized(emittersList) {
            emittersList.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("join").data(data));
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
