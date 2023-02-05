import * as socket from "./websocket-service.js";

createConnectInfo();

let state;

function keyboard(value) {
    const key = {};
    key.value = value;
    key.isDown = false;
    key.isUp = true;
    key.press = undefined;
    key.release = undefined;
    //The `downHandler`
    key.downHandler = (event) => {
        if (event.key === key.value) {
            if (key.isUp && key.press) {
                key.press();
            }
            key.isDown = true;
            key.isUp = false;
            event.preventDefault();
        }
    };

    //The `upHandler`
    key.upHandler = (event) => {
        if (event.key === key.value) {
            if (key.isDown && key.release) {
                key.release();
            }
            key.isDown = false;
            key.isUp = true;
            event.preventDefault();
        }
    };

    //Attach event listeners
    const downListener = key.downHandler.bind(key);
    const upListener = key.upHandler.bind(key);

    window.addEventListener("keydown", downListener, false);
    window.addEventListener("keyup", upListener, false);

    // Detach event listeners
    key.unsubscribe = () => {
        window.removeEventListener("keydown", downListener);
        window.removeEventListener("keyup", upListener);
    };

    return key;
}

function play(delta) {

    //Use the cat's velocity to make it move
    spaceship.x += spaceship.vx;
    spaceship.y += spaceship.vy;
}

function keyBoardInit() {
    //Capture the keyboard arrow keys
    const left = keyboard("a"),
        up = keyboard("w"),
        right = keyboard("d"),
        down = keyboard("s");

    //Left arrow key `press` method
    left.press = () => {
        //Change the cat's velocity when the key is pressed
        spaceship.vx = -5;
        spaceship.vy = 0;
        spaceship.rotation = 4.712;
        socket.sendMotion(spaceship.x, spaceship.y);
    };

    //Left arrow key `release` method
    left.release = () => {
        //If the left arrow has been released, and the right arrow isn't down,
        //and the cat isn't moving vertically:
        //Stop the cat
        if (!right.isDown && spaceship.vy === 0) {
            spaceship.vx = 0;
        }
    };

    //Up
    up.press = () => {
        spaceship.vy = -5;
        spaceship.vx = 0;
        spaceship.rotation = 0;
        socket.sendMotion(spaceship.x, spaceship.y);
    };
    up.release = () => {
        if (!down.isDown && spaceship.vx === 0) {
            spaceship.vy = 0;
        }
    };

    //Right
    right.press = () => {
        spaceship.vx = 5;
        spaceship.vy = 0;
        spaceship.rotation = 1.571;
        socket.sendMotion(spaceship.x, spaceship.y);
    };
    right.release = () => {
        if (!left.isDown && spaceship.vy === 0) {
            spaceship.vx = 0;
        }
    };

    //Down
    down.press = () => {
        spaceship.vy = 5;
        spaceship.vx = 0;
        spaceship.rotation = 3.142;
        socket.sendMotion(spaceship.x, spaceship.y);
    };
    down.release = () => {
        if (!up.isDown && spaceship.vx === 0) {
            spaceship.vy = 0;
        }
    };

    //Set the game state
    state = play;
}

function createConnectInfo() {
    console.log("createConnectInfo");
    let input = document.createElement("input");
    input.innerHTML = "InputText";
    input.type = "text";
    input.name = "inputText";
    document.body.appendChild(input);

    let btn = document.createElement("button");
    btn.innerHTML = "Submit";
    btn.type = "submit";
    btn.name = "formBtn";
    document.body.appendChild(btn);
    btn.onclick = function () {
        console.log("playerName: " + input.value)
        socket.initSocketConnection(input.value);
        input.remove();
        btn.remove();
    };
}