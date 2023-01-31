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

function setup() {
    const spaceship = new PIXI.Sprite(
        PIXI.Loader.shared.resources["images/spaceship.png"].texture
    );

    app.stage.addChild(spaceship);
}

