package com.marowak.request;

public record CharacterRequest(
        String accountName,
        String characterName,
        int x,
        int y,
        int angle
) {
}
