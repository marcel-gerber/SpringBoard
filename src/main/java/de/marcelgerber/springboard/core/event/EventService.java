package de.marcelgerber.springboard.core.event;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;

/**
 * Class for handling Server-Sent-Events
 */
@Service
public class EventService {

    private final HashMap<String, SseEmitter> emitters = new HashMap<>();

    /**
     * Creates an SseEmitter
     *
     * @param gameId String
     * @return SseEmitter
     */
    public SseEmitter createEmitter(final String gameId) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(gameId, emitter);

        emitter.onCompletion(() -> emitters.remove(gameId));
        emitter.onTimeout(() -> emitters.remove(gameId));

        return emitter;
    }

    /**
     * Sends an update to all subscribers of the game with 'gameId'
     *
     * @param gameId String
     * @param move String
     */
    public void sendMoveUpdate(String gameId, String move) {
        SseEmitter emitter = emitters.get(gameId);
        if(emitter == null) return;

        try {
            emitter.send(SseEmitter.event().name("move").data(move));
        } catch (IOException e) {
            emitters.remove(gameId);
            throw new RuntimeException(e);
        }
    }

}
