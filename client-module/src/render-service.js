import * as pixi from './libs/pixi.min.js';
import player from "./obj/Player.js";
import {SpriteShip} from "./obj/SpriteShip.js";

const app = new pixi.Application({
    resizeTo: window
});


// fast container for all sprites (faster than Container in 3-5 times)
let sprites;

// key: playerName, value: SpriteShip obj
let ships = new Map();

export function initRender() {
    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("../images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("../images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    sprites = createSpritesContainer();
    app.stage.addChild(sprites);

    updatePlayers();

    let playerNameLabel = createPlayerNameLabel(player.playerName);
    app.stage.addChild(playerNameLabel);

    let posInfoLabel = createPosInfoLabel(player);
    app.stage.addChild(posInfoLabel);

    window.addEventListener('resize', resize);

    app.ticker.add(() => {
        updateLocationText(posInfoLabel);
        updateBackground(bgLast, 3);
        updateBackground(bgFirst, 2);

        updatePlayers();
    });
}

export function updateAllPlayers(playersResponse) {
    for (let playerMotion of playersResponse.playerMotions) {
        let ship = ships.get(playerMotion.playerName);
        if (ship === undefined) {
            ship = new SpriteShip(playerMotion.motion);
            ships.set(playerMotion.playerName, ship);
        } else {
            ship.motion = playerMotion.motion;
        }
    }
}

// CREATE
function createPlayerNameLabel(playerName) {
    let sprite = ships.get(playerName).sprite;
    const nameText = new pixi.Text(playerName, {
        fill: 0xffffff,
    });
    nameText.x = sprite.x - sprite.width / 2;
    nameText.y = sprite.y - sprite.height - 5;

    return nameText;
}

function createPosInfoLabel(player) {
    const locationText = new pixi.Text(player.getLocation(), {
        fill: 0xffffff,
    });
    locationText.x = 25;
    locationText.y = 50;

    return locationText;
}

function createBackground(texture) {
    let bg = new pixi.TilingSprite(texture, window.innerWidth, window.innerHeight);
    bg.position.set(0, 0);

    return bg;
}

function createSpritesContainer() {
    return new pixi.ParticleContainer(1000, {
        scale: true,
        position: true,
        rotation: true,
        uvs: true,
        alpha: true
    });
}

// UPDATE
function updateOrCreatePlayer(ship, playerName, absX, absY) {
    if (ship.sprite === undefined) {
        let sprite = pixi.Sprite.from("../images/spaceship.png");
        sprite.anchor.set(0.5, 0.5);
        sprite.width = 64;
        sprite.height = 64;

        sprites.addChild(sprite)
        ship.sprite = sprite;
    }

    ship.sprite.position.set(getX(ship.motion.x, absX), getY(ship.motion.y, absY));
    ship.sprite.angle = ship.motion.angle;
}

function getX(currX, diffX) {
    return currX - diffX + (window.innerWidth / 2);
}

function getY(currY, diffY) {
    return currY - diffY + (window.innerHeight / 2);
}

function updateLocationText(posInfoLabel) {
    posInfoLabel.text = player.getLocation();
}

function updatePlayers() {
    ships.forEach((value, key) => {
        updateOrCreatePlayer(value, key, player.x, player.y);
    })
}

function updateBackground(bg, div) {
    bg.tilePosition.x -= (isNaN(player.getDiffX()) ? 0 : player.getDiffX()) / div;
    bg.tilePosition.y -= (isNaN(player.getDiffY()) ? 0 : player.getDiffY()) / div;
}

export function deletePlayer(deleteResponse) {
    let ship = ships.get(deleteResponse.playerName);
    sprites.removeChild(ship.sprite);
    ships.delete(deleteResponse.playerName);
}

function resize() {
    let ship = ships.get(player.playerName);
    ship.sprite.position.set(window.innerWidth / 2, window.innerHeight / 2);
}