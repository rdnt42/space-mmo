import * as pixi from './libs/pixi.min.js';

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
const newStateSpaceShips = new Map();

export function update(playerResponse) {
    let diffX = playerResponse.motion.x;
    let diffY = playerResponse.motion.y;

    for (let player of playerResponse.playerMotions) {
        let spaceShip = spaceShips.get(player.playerName);
        if (spaceShip === undefined) {
            spaceShip = pixi.Sprite.from("../images/spaceship.png");
            spaceShip.anchor.set(0.5, 0.5);
            app.stage.addChild(spaceShip);
            spaceShips.set(player.playerName, spaceShip);
        }

        spaceShip.position.set(getX(player.motion.x, diffX), getY(player.motion.y, diffY));
        console.log("y", spaceShip.anchor.y);
    }
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


export function renderOtherPlayers(playerList) {
    // app.stage.removeChildren();

}