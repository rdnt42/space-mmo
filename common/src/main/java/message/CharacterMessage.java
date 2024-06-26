package message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import keys.CharacterMessageKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CharacterMessage {
    private CharacterMessageKey key;

    private String characterName;

    private String accountName;

    private int experience;

    private double x;

    private double y;

    private int angle;

    private boolean online;
}
