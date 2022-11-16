package marowak.dev.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 16.11.2022
 * Time: 23:39
 */
@NoArgsConstructor
@Data
public class Motion {
    private Integer x;
    private Integer y;

    private Integer vx;
    private Integer vy;
}
