let Engine = Matter.Engine,
    Runner = Matter.Runner,
    Render = Matter.Render,
    Composite = Matter.Composite,
    Body = Matter.Body,
    Bodies = Matter.Bodies,
    World = Matter.World;

let engine;
let world;

export function initPhysics() {
    engine = Engine.create();
    engine.world.gravity.y = 0;
    world = engine.world;

    let render = Render.create({
        element: document.body,
        engine: engine,
        options: {
            width: innerWidth,
            height: innerHeight,
        }
    });

    Render.run(render);
    let runner = Runner.create();
    Runner.run(runner, engine);
}

export function createCharacter(x, y, polygons, scale) {
    let body = Bodies.fromVertices(x, y, createPoints(polygons));
    Body.scale(body, scale, scale);
    Composite.addBody(world, body);

    return body;
}

function createPoints(polygons) {
    let answer = [];
    for (let i = 0; i < polygons.length; i += 2) {
        let x = polygons[i];
        let y = polygons[i + 1];
        answer.push({x: x, y: y});
    }

    return answer;
}

export function renderBody(body, x, y, angle) {
    Body.setPosition(body, {x: x, y: y}, false);
    Body.setAngle(body, angle, false)
}

export function removeBody(body) {
    World.remove(world, body);
}

export function moveBody(body, x, y, angle) {
    Body.setVelocity(body, {x: x, y: y});
    Body.setAngle(body, angle, false)
}