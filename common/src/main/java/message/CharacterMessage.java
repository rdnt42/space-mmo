package message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import keys.CharacterMessageKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonSerialize
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class CharacterMessage {
    private final CharacterMessageKey key;

    private final String characterName;

    private final String accountName;

    private final int experience;

    private final double x;

    private final double y;

    private final int angle;

    private final boolean online;

    public static Builder builder() {
        return new Builder();
    }

    private CharacterMessage(Builder builder) {
        this.key = builder.key;
        this.characterName = builder.characterName;
        this.accountName = builder.accountName;
        this.experience = builder.experience;
        this.x = builder.x;
        this.y = builder.y;
        this.angle = builder.angle;
        this.online = builder.online;
    }

    public static class Builder {
        private CharacterMessageKey key;

        private String characterName;

        private String accountName;

        private int experience;

        private double x;

        private double y;

        private int angle;

        private boolean online;

        public CharacterMessage build() {
            return new CharacterMessage(this);
        }

        public Builder key(CharacterMessageKey key) {
            this.key = key;
            return this;
        }

        public Builder characterName(String characterName) {
            this.characterName = characterName;
            return this;
        }

        public Builder accountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public Builder experience(int experience) {
            this.experience = experience;
            return this;
        }

        public Builder x(double x) {
            this.x = x;
            return this;
        }

        public Builder y(double y) {
            this.y = y;
            return this;
        }

        public Builder angle(int angle) {
            this.angle = angle;
            return this;
        }

        public Builder online(boolean online) {
            this.online = online;
            return this;
        }
    }
}
