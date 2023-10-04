package marowak.dev.service.objects;

import marowak.dev.response.character.BulletsResponse;

public interface BodyService {
    BulletsResponse getBullets(String characterName);
}
