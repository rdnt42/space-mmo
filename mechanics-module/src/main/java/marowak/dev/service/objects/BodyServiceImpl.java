package marowak.dev.service.objects;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.response.BulletBodyInfo;
import marowak.dev.service.physic.WeaponService;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Singleton
public class BodyServiceImpl implements BodyService {
    private final WeaponService weaponService;

    @Override
    public Flux<BulletBodyInfo> getBullets(String characterName) {
        return weaponService.getBulletsInRange(characterName);
    }
}
