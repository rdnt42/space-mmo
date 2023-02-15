import * as pixi from './libs/pixi.min.js';
import player from "./obj/Player.js";

const app = new pixi.Application({
    resizeTo: window
});


// fast container for all sprites (faster than Container in 3-5 times)
let sprites;
// map playerName - Sprite for fast changing
const spaceShips = new Map();

let players = [];

export function initRender(players) {
    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("../images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("../images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    sprites = createSpritesContainer();
    app.stage.addChild(sprites);

    updateOrCreatePlayer(players.playerMotion.motion, player.playerName, player.x, player.y);

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
    players = playersResponse.playerMotions;
}

// CREATE
function createPlayerNameLabel(playerName) {
    let ship = spaceShips.get(playerName);
    const nameText = new pixi.Text(playerName, {
        fill: 0xffffff,
    });
    nameText.x = ship.x - ship.width / 2;
    nameText.y = ship.y - ship.height - 5;

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
function updateOrCreatePlayer(motion, playerName, absX, absY) {
    let spaceShip = spaceShips.get(playerName);
    if (spaceShip === undefined) {
        spaceShip = pixi.Sprite.from("../images/spaceship.png");
        spaceShip.anchor.set(0.5, 0.5);
        spaceShip.width = 64;
        spaceShip.height = 64;

        spaceShips.set(playerName, spaceShip);
        sprites.addChild(spaceShip);
    }

    spaceShip.position.set(getX(motion.x, absX), getY(motion.y, absY));
    spaceShip.angle = motion.angle;
}

function getX(currX, diffX) {
    return currX - diffX + (window.innerWidth / 2);
}

function getY(currY, diffY) {
    return currY - diffY + window.innerHeight / 2;
}

function updateLocationText(posInfoLabel) {
    posInfoLabel.text = player.getLocation();
}

function updatePlayers() {
    for (let otherPlayer of players) {
        updateOrCreatePlayer(otherPlayer.motion, otherPlayer.playerName, player.x, player.y);
    }
}

function updateBackground(bg, div) {
    bg.tilePosition.x -= (isNaN(player.getDiffX()) ? 0 : player.getDiffX()) / div;
    bg.tilePosition.y -= (isNaN(player.getDiffY()) ? 0 : player.getDiffY()) / div;
}

export function deletePlayer(deleteResponse) {
    let ship = spaceShips.get(deleteResponse.playerName);
    sprites.removeChild(ship);

    spaceShips.delete(deleteResponse.playerName);
}

function resize() {
    let ship = spaceShips.get(player.playerName);
    ship.position.set(window.innerWidth / 2, window.innerHeight / 2);
}