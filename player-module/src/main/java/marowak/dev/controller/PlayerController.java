package marowak.dev.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import marowak.dev.response.player.PlayerMotionListResponse;
import marowak.dev.response.player.PlayerMotionResponse;
import marowak.dev.service.PlayerMotionService;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 16.11.2022
 * Time: 23:28
 */
@RequiredArgsConstructor
@Controller("/player")
public class PlayerController {
    private final PlayerMotionService playerMotionService;

    @Post("/motion")
    public PlayerMotionListResponse update(@Body PlayerMotionResponse request) {
        playerMotionService.updatePlayerMotion(request);

        return playerMotionService.getPlayersMotions();
    }
}
