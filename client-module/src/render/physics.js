let engine;
let world;

export function initPhysics() {
    engine = Matter.Engine.create();
    world = engine.world;
}

export function createCharacter(sprite) {
    const spriteBody = Matter.Bodies.rectangle(sprite.x, sprite.y, sprite.width, sprite.height);
    Matter.World.add(world, spriteBody);

    return spriteBody;
}

export function renderBody(body, x, y) {
    Matter.Body.applyForce(body, body.position, {x: x, y: y});
    Matter.Engine.update(engine);
}

export function removeBody(body) {
    Matter.World.remove(world, body);
}
