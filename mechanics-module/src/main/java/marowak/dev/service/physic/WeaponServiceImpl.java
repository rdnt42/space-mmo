package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.item.ItemService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class WeaponServiceImpl implements WeaponService, Calculable {

    private final WorldService worldService;
    private final ItemService itemService;

    @Override
    public Flux<BodyInfo> getBulletsInRange(String characterName) {
        return Mono.just(worldService.getBody(SpaceShip.class, characterName))
                .map(ship -> ship.getTransform().getTranslation())
                .flatMapMany(base -> Flux.fromStream(worldService.getBodies(BulletBody.class).stream())
                        .filter(body -> Utils.isInRange(base, body.getTransform().getTranslation()))
                        .map(Utils.bodyToBodyInfo));
    }

    @Async
    @Override
    public void calculate() {
        worldService.getBodies(BulletBody.class)
                .forEach(this::calculateBullet);
//        itemService.getEquippedItems(ItemType.ITEM_TYPE_WEAPON)
//                .ofType(Weapon.class)
//                .filter(weapon -> weapon.isShooting() && weapon.isReadyForShoot())
//                .doOnNext(this::makeShot);
    }

    private void calculateBullet(IdentifiablePhysicalBody body) {
        if (body.isAtRest()) {
            worldService.removeBody(body);
        }
    }

//    private Mono<Void> makeShot(Weapon weapon) {
//        Vector2 translation = ship.getTransform().getTranslation();
//
//        itemService.getEquippedItems(ship.getId(), ItemType.ITEM_TYPE_WEAPON)
//                .map(Weapon.class::cast)
//                .filter(Weapon::isReadyForShoot)
//                .mapNotNull(weapon -> {
//                    BulletCreateRequest request =
//                            getNewBullet(ship.getId(), ship.getShootAngleRadians(), translation.x, translation.y, weapon);
//                    KineticBullet bullet = FactoryUtils.createKineticBullet(request);
//                    worldService.createBody(bullet);
//                    weapon.updateShoot();
//
//                    return null;
//                }).subscribe();
//    }
}
