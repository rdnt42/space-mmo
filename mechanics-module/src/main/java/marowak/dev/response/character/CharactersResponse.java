package marowak.dev.response.character;

import marowak.dev.dto.CharacterInfo;

import java.util.Map;

public record CharactersResponse(
        Map<String, CharacterInfo> charactersMap
) {
}
