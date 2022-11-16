package marowak.dev.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 16.11.2022
 * Time: 23:43
 */
@NoArgsConstructor
@Data
public class PlayerMotion {
    private String playerName;
    private Motion motion;
}
