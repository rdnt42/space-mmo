package marowak.dev.service.motion;

import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayerLeavingResponse;
import marowak.dev.response.player.PlayersMotionListResponse;

public interface PlayerMotionSocketService {
    void onOpen(String playerName);

    PlayersMotionListResponse onMessage(PlayerMotionRequest request, String playerName);

    PlayerLeavingResponse onClose(String playerName);
}
