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
            spaceShip.angle = player.angle;
            updateLocationText();
        }
    });
}

// create an array to store all the sprites
const spaceShips = new Map();

export function updateCurrentPlayer(players) {
    for (let playerMotion of players.playerMotions) {
        updateOrCreatePlayer(playerMotion, player.x, player.y);
    }
}

export function updateSingle(singlePlayer) {
    if (singlePlayer.playerMotion.playerName === player.playerName) {
        let diffX = player.getDiffX();
        let diffY = player.getDiffY();

        for (let [key, value] of spaceShips) {
            if (key !== player.playerName) {
                value.position.set(value.x - diffX, value.y - diffY);
            }
        }
    } else {
        updateOrCreatePlayer(singlePlayer.playerMotion, player.x, player.y);
    }
}

function updateOrCreatePlayer(player, absX, absY) {
    let spaceShip = spaceShips.get(player.playerName);
    if (spaceShip === undefined) {
        spaceShip = pixi.Sprite.from("../images/spaceship.png");
        spaceShip.anchor.set(0.5, 0.5);
        app.stage.addChild(spaceShip);
        spaceShips.set(player.playerName, spaceShip);
    }

    spaceShip.position.set(getX(player.motion.x, absX), getY(player.motion.y, absY));
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

