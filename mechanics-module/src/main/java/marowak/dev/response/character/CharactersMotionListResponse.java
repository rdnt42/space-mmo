package marowak.dev.response.character;

import marowak.dev.dto.motion.CharacterMotion;

import java.util.Collection;

public record CharactersMotionListResponse(
        CharacterMotion motion,
        Collection<CharacterMotion> motions
) {
}
