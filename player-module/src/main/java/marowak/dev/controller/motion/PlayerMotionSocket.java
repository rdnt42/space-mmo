package marowak.dev.controller.motion;

import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotionRequest;
import marowak.dev.dto.motion.PlayersMotionListResponse;
import marowak.dev.service.PlayerMotionService;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
@ServerWebSocket("/motion/{topic}/{playerName}")
public class PlayerMotionSocket {
    private final WebSocketBroadcaster broadcaster;
    private final PlayerMotionService playerMotionService;

    @OnOpen
    public Publisher<String> onOpen(String topic, String playerName, WebSocketSession session) {
        debugLog("onOpen", topic, playerName, session);

        return broadcaster.broadcast(String.format("[%s] Joined %s!", playerName, topic),
                isValid(topic, session, playerName));
    }

    @OnMessage
    public Publisher<PlayersMotionListResponse> onMessage(String playerName, String topic,
                                                          PlayerMotionRequest request, WebSocketSession session) {

        debugLog("onMessage", topic, playerName, session);

        if (request.isUpdate()) {
            playerMotionService.updatePlayerMotion(playerName, request);
        }

        PlayersMotionListResponse response = playerMotionService.getPlayersMotions(playerName);

        return broadcaster.broadcast(response, isValid(topic, session, playerName));
    }

    @OnClose
    public Publisher<String> onClose(String playerName, String topic, WebSocketSession session) {
        debugLog("onClose", topic, playerName, session);

//        playerMotionService.disconnectPlayer(playerName);
        return broadcaster.broadcast(String.format("[%s] Leaving %s!", playerName, topic),
                isValid(topic, session, playerName));
    }

    private Predicate<WebSocketSession> isValid(String topic, WebSocketSession session, String playerName) {
        return s -> s != session &&
                topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null)) &&
                playerName.equalsIgnoreCase(s.getUriVariables().get("playerName", String.class, null));
    }

    private void debugLog(String event, String topic, String playerName, WebSocketSession session) {
        log.debug("Event {}, session id: {}, playerName: {}, topic: {}", event, session.getId(), playerName, topic);
    }
}
