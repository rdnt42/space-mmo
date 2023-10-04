package marowak.dev.service.objects;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.response.character.BulletsResponse;
import marowak.dev.service.WorldService;

@RequiredArgsConstructor
@Singleton
public class BodyServiceImpl implements BodyService {
    private final WorldService worldService;

    @Override
    public BulletsResponse getBullets(String characterName) {
        return new BulletsResponse(worldService.getBulletsInRange(characterName));
    }
}
