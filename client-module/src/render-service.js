import * as pixi from './libs/pixi.min.js';
import player from "./obj/Player.js";

const app = new pixi.Application({
    resizeTo: window
});

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
}

// create an array to store all the sprites
const spaceShips = new Map();

export function updateAll(players) {
    console.log("players", players)

    for (let playerMotion of players.playerMotions) {
        updateOrCreatePlayer(playerMotion, player.x, player.y);
    }
}

export function updateSingle(singlePlayer) {
    console.log("playerMotion", singlePlayer.playerMotion);
    if (singlePlayer.playerMotion.playerName === player.playerName) {
        for (let [key, value] of spaceShips) {
            if (key !== player.playerName) {
                value.position.set(value.x + player.getDiffX(), value.y + player.getDiffY());
            }
        }
    } else {
        let absX = player.x;
        let absY = player.y;
        updateOrCreatePlayer(singlePlayer.playerMotion, absX, absY);
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


// // create a bounding box box for the little maggots
// const dudeBoundsPadding = 100;
// const dudeBounds = new PIXI.Rectangle(
//     -dudeBoundsPadding,
//     -dudeBoundsPadding,
//     app.screen.width + dudeBoundsPadding * 2,
//     app.screen.height + dudeBoundsPadding * 2,
// );

let tick = 0;

app.ticker.add(() => {
    // increment the ticker
    tick += 0.1;
});