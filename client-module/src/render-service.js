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

let windowWidth;
let windowHeight;

export function initRender() {
    windowWidth = window.innerWidth / 2;
    windowHeight = window.innerHeight / 2;

    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("../images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("../images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    sprites = createSpritesContainer();
    app.stage.addChild(sprites);

    reRenderPlayers();

    let posInfoLabel = createPosInfoLabel(player);
    app.stage.addChild(posInfoLabel);

    window.addEventListener('resize', resize);

    app.ticker.add(() => {
        updateLocationText(posInfoLabel);
        updateBackground(bgLast, 3);
        updateBackground(bgFirst, 2);

        reRenderPlayers();
    });
}

export function updateAllPlayers(data) {
    for (let motion of data.playersMotions) {
        let ship = ships.get(motion.playerName);
        if (ship === undefined) {
            ship = new SpriteShip(motion);
            ships.set(motion.playerName, ship);
        } else {
            ship.motion = motion;
        }
    }
}

// CREATE
function createPlayerNameLabel(playerName) {
    return new pixi.Text(playerName, {
        fill: 0xffffff,
    });
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

// RENDER
function reRenderPlayers() {
    const playerShip = ships.get(player.playerName);
    ships.forEach((value, key) => {
        updateOrCreatePlayer(value, key, playerShip.motion.x, playerShip.motion.y);
        updateOrCreateLabel(value, key);
    })
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

function updateOrCreateLabel(ship, playerName) {
    if (ship.label === undefined) {
        let playerNameLabel = createPlayerNameLabel(playerName);
        app.stage.addChild(playerNameLabel);
        ship.label = playerNameLabel;
    }
    let x = ship.sprite.x - ship.sprite.width / 2;
    let y = ship.sprite.y - ship.sprite.height - 5;
    ship.label.position.set(x, y);
}

function getX(currX, diffX) {
    return currX - diffX + windowWidth;
}

function getY(currY, diffY) {
    return currY - diffY + windowHeight;
}

function updateLocationText(posInfoLabel) {
    posInfoLabel.text = player.getLocation();
}


function updateBackground(bg, div) {
    bg.tilePosition.x -= (isNaN(player.getDiffX()) ? 0 : player.getDiffX()) / div;
    bg.tilePosition.y -= (isNaN(player.getDiffY()) ? 0 : player.getDiffY()) / div;
}

export function deletePlayer(playerName) {
    let ship = ships.get(playerName);
    sprites.removeChild(ship.sprite);
    app.stage.removeChild(ship.label);
    ships.delete(playerName);
}

function resize() {
    let ship = ships.get(player.playerName);
    ship.sprite.position.set(window.innerWidth / 2, window.innerHeight / 2);
}