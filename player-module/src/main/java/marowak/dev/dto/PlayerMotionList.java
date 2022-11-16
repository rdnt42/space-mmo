package marowak.dev.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 17.11.2022
 * Time: 0:07
 */
@Builder
@Data
public class PlayerMotionList {
    private List<PlayerMotion> playerMotions;
}
