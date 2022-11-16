package marowak.dev.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import marowak.dev.dto.PlayerMotion;
import marowak.dev.dto.PlayerMotionList;
import marowak.dev.service.PlayerMotionService;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 16.11.2022
 * Time: 23:28
 */
@Controller("/player")
public class PlayerController {
    @Inject
    private PlayerMotionService playerMotionService;

    @Post("/motion")
    public PlayerMotionList update(@Body PlayerMotion request) {
        playerMotionService.updatePlayerMotion(request);

        return playerMotionService.getPlayersMotions();
    }
}
