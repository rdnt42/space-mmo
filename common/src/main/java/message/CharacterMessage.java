package message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CharacterMessage {
    private String characterName;

    private String accountName;

    private int experience;

    private double x;

    private double y;

    private int angle;

    private boolean online;
}
