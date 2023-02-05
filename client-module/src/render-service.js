import * as pixi from './libs/pixi.min.js';

const app = new pixi.Application();
document.body.appendChild(app.view);

const sprites = new pixi.ParticleContainer(10000, {
    scale: true,
    position: true,
    rotation: true,
    uvs: true,
    alpha: true,
    resizeTo: window
});
app.stage.addChild(sprites);

// create an array to store all the sprites
const spaceShips = new Map();
const newStateSpaceShips = new Map();

export function update(playerList) {
    for(let player of playerList) {
        let spaceShip = spaceShips.get(player.playerName);
        if (spaceShip !== undefined) {
            spaceShip.position.set(player.motion.x, player.motion.y);
        } else {
            spaceShip = pixi.Sprite.from("../images/spaceship.png");
            spaceShip.position.set(window.innerHeight / 2, window.innerWidth / 2);
            spaceShip.anchor.set(0.5, 0.5);

            app.stage.addChild(spaceShip);
            spaceShips.set(player.playerName, spaceShip);
        }
    }
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
    // iterate through the sprites and update their position
    for (let i = 0; i < spaceShips.length; i++) {
        const spaceShip = spaceShips[i];
        dude.scale.y = 0.95 + Math.sin(tick + dude.offset) * 0.05;
        dude.direction += dude.turningSpeed * 0.01;
        dude.x += Math.sin(dude.direction) * (dude.speed * dude.scale.y);
        dude.y += Math.cos(dude.direction) * (dude.speed * dude.scale.y);
        dude.rotation = -dude.direction + Math.PI;

        // wrap the maggots
        if (dude.x < dudeBounds.x) {
            dude.x += dudeBounds.width;
        } else if (dude.x > dudeBounds.x + dudeBounds.width) {
            dude.x -= dudeBounds.width;
        }

        if (dude.y < dudeBounds.y) {
            dude.y += dudeBounds.height;
        } else if (dude.y > dudeBounds.y + dudeBounds.height) {
            dude.y -= dudeBounds.height;
        }
    }

    // increment the ticker
    tick += 0.1;
});


export function renderOtherPlayers(playerList) {
    // app.stage.removeChildren();

    const players = JSON.parse(playerList);
    console.log("players", players.playerMotions);

    for(let player of players.playerMotions) {
        console.log("player", player.playerName);
    }
}