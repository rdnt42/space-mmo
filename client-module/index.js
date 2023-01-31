//Aliases
const Application = PIXI.Application,
    loader = PIXI.Loader.shared,
    resources = PIXI.Loader.shared.resources,
    Sprite = PIXI.Sprite;

//Create a Pixi Application
const app = new PIXI.Application({width: 222, height: 222});
app.renderer.view.style.position = "absolute";
app.renderer.view.style.display = "block";
app.renderer.autoDensity = true;
app.resizeTo = window;

//Add the canvas that Pixi automatically created for you to the HTML document
document.body.appendChild(app.view);

loader
    .add("images/spaceship.png")
    .load(setup);
loader.onProgress
    .add(loadProgressHandler);

function loadProgressHandler(loader, resource) {

    //Display the file `url` currently being loaded
    console.log("loading: " + resource.url);

    //Display the percentage of files currently loaded
    console.log("progress: " + loader.progress + "%");

    //If you gave your files names as the first argument
    //of the `add` method, you can access them like this
    //console.log("loading: " + resource.name);
}

let spaceship, state;
function setup() {
    console.log("All files loaded");
    spaceshipInit();
    keyBoardInit();

    //Start the game loop
    app.ticker.add((delta) => gameLoop(delta));
}

function gameLoop(delta) {
    state(delta);
}

function spaceshipInit() {
    spaceship = new PIXI.Sprite(
        PIXI.Loader.shared.resources["images/spaceship.png"].texture
    );

    app.stage.addChild(spaceship);

    spaceship.position.set(250, 500);
    spaceship.vx = 0;
    spaceship.vy = 0;

    spaceship.anchor.set(0.5, 0.5);
}

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
    };
    down.release = () => {
        if (!up.isDown && spaceship.vx === 0) {
            spaceship.vy = 0;
        }
    };

    //Set the game state
    state = play;
}