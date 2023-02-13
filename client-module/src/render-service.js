import * as pixi from './libs/pixi.min.js';
import player from "./obj/Player.js";

const app = new pixi.Application({
    resizeTo: window
});

let tick = 0;
let sprites;
// create an array to store all the sprites
const spaceShips = new Map();

export function initRender(players) {
    let playerName = players.playerMotion.playerName;
    document.body.appendChild(app.view);
    sprites = new pixi.ParticleContainer(10000, {
        scale: true,
        position: true,
        rotation: true,
        uvs: true,
        alpha: true
    });
    app.stage.addChild(sprites);

    updateOrCreatePlayer(players.playerMotion.motion, playerName, player.x, player.y);

    let playerNameLabel = createPlayerNameLabel(playerName);
    app.stage.addChild(playerNameLabel);

    let posInfoLabel = createPosInfoLabel(player);
    app.stage.addChild(posInfoLabel);

    app.ticker.add(() => {
        // increment the ticker
        tick += 0.1;
        updateLocationText(posInfoLabel);
    });
}

export function updateAllObjects(players) {
    let motion = players.playerMotion.motion;
    for (let otherPlayer of players.playerMotions) {
        updateOrCreatePlayer(otherPlayer.motion, otherPlayer.playerName, motion.x, motion.y);
    }
}

export function deletePlayer(deleteResponse) {
    let ship = spaceShips.get(deleteResponse.playerName);
    sprites.removeChild(ship);

    spaceShips.delete(deleteResponse.playerName);
}

function updateOrCreatePlayer(motion, playerName, absX, absY) {
    let spaceShip = spaceShips.get(playerName);
    if (spaceShip === undefined) {
        spaceShip = pixi.Sprite.from("../images/spaceship.png");
        spaceShip.anchor.set(0.5, 0.5);
        sprites.addChild(spaceShip);
        spaceShip.width = 64;
        spaceShip.height = 64;

        spaceShips.set(playerName, spaceShip);
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

window.addEventListener('resize', resize);

function resize() {
    let ship = spaceShips.get(player.playerName);
    ship.position.set(window.innerWidth / 2, window.innerHeight / 2);
}

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

