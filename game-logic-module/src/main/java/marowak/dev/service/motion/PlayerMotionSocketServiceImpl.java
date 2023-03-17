package marowak.dev.service.motion;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.CharactersGetMessageKey;
import marowak.dev.enums.MessageCommand;
import marowak.dev.request.PlayerMotionRequest;
import marowak.dev.response.player.PlayerLeavingResponse;
import marowak.dev.response.player.PlayersMotionListResponse;
import marowak.dev.service.character.CharacterService;

import java.util.Collection;

@RequiredArgsConstructor
@Singleton
public class PlayerMotionSocketServiceImpl implements PlayerMotionSocketService {

    private final PlayerMotionService playerMotionService;
    private final CharacterService characterService;

    @Override
    public void onOpen(String playerName) {
        characterService.sendCharacterState(playerName, true);
        characterService.sendInitCharacter(CharactersGetMessageKey.CHARACTERS_GET_ONE, playerName);
    }

    @Override
    public PlayersMotionListResponse onMessage(PlayerMotionRequest request, String playerName) {

        if (request.isUpdate()) {
            playerMotionService.updatePlayerMotion(playerName, request);
        }

        Collection<PlayerMotion> motions = playerMotionService.getPlayersInRange(playerName);
        PlayerMotion currPlayerMotion = playerMotionService.getPlayerMotion(playerName);

        return new PlayersMotionListResponse(MessageCommand.CMD_UPDATE_CURRENT_PLAYER, currPlayerMotion, motions);
    }

    @Override
    public PlayerLeavingResponse onClose(String playerName) {
        // TODO #45 remove player after disconnect
//        playerMotionService.leavingPlayer(playerName);
        characterService.sendCharacterState(playerName, false);

        return new PlayerLeavingResponse(MessageCommand.CMD_LEAVING_PLAYER, playerName);
    }
}
