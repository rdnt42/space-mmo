import * as pixi from './libs/pixi.min.js';
import player from "./obj/Player.js";

const app = new pixi.Application({
    resizeTo: window
});

const locationText = new pixi.Text(player.getLocation(), {
    fill: 0xffffff,
});

let tick = 0;

export function initRender() {
    document.body.appendChild(app.view);

    const sprites = new pixi.ParticleContainer(10000, {
        scale: true,
        position: true,
        rotation: true,
        uvs: true,
        alpha: true
    });
    app.stage.addChild(sprites);

    locationText.x = 25;
    locationText.y = 50;
    app.stage.addChild(locationText);

    app.ticker.add(() => {
        // increment the ticker
        tick += 0.1;
        let spaceShip = spaceShips.get(player.playerName);
        if (spaceShip !== undefined) {
            updateLocationText();
        }
    });
}

// create an array to store all the sprites
const spaceShips = new Map();

export function updateCurrentPlayer(players) {
    let motion = players.playerMotion.motion;
    for (let otherPlayer of players.playerMotions) {
        updateOrCreatePlayer(otherPlayer.motion, otherPlayer.playerName, motion.x, motion.y);
    }
}

export function updateSingle(otherPlayer) {
    let otherPlayerMotion = otherPlayer.playerMotion;
    if (otherPlayerMotion.playerName === player.playerName) {
        let diffX = player.getDiffX();
        let diffY = player.getDiffY();

        for (let [key, value] of spaceShips) {
            if (key !== player.playerName) {
                value.position.set(value.x - diffX, value.y - diffY);
            } else {
                value.angle = player.angle;
            }
        }
    } else {
        updateOrCreatePlayer(otherPlayerMotion.motion, otherPlayerMotion.playerName, player.x, player.y);
    }
}

export function deletePlayer(deleteResponse) {
    let ship = spaceShips.get(deleteResponse.playerName);
    app.stage.removeChild(ship);

    spaceShips.delete(deleteResponse.playerName);
}

function updateOrCreatePlayer(motion, playerName, absX, absY) {
    let spaceShip = spaceShips.get(playerName);
    if (spaceShip === undefined) {
        spaceShip = pixi.Sprite.from("../images/spaceship.png");
        spaceShip.anchor.set(0.5, 0.5);
        app.stage.addChild(spaceShip);
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

function updateLocationText() {
    locationText.text = player.getLocation();
}

window.addEventListener('resize', resize);

function resize() {
    let ship = spaceShips.get(player.playerName);
    ship.position.set(window.innerWidth / 2, window.innerHeight / 2);
}

