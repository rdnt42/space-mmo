import * as pixi from './libs/pixi.min.js';
import * as characterService from "./character-service.js";

const app = new pixi.Application({
    resizeTo: window
});

const Sort = {
    OTHER_PLAYERS: 30,
    PLAYER: 90,
    INVENTORY: 100
}

// fast container for all spritesContainer (faster than Container in 3-5 times)
let spritesContainer;
let inventory;

let windowWidth;
let windowHeight;

export function initRender() {
    windowWidth = window.innerWidth / 2;
    windowHeight = window.innerHeight / 2;

    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("./images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("./images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    inventory = createInventory();
    app.stage.addChild(inventory);

    spritesContainer = createSpritesContainer();
    app.stage.addChild(spritesContainer);

    let posInfoLabel = createPosInfoLabel();
    app.stage.addChild(posInfoLabel);

    //TODO #24 window.addEventListener('resize', resize);

    app.stage.sortChildren();

    app.ticker.add(() => {
        updateLocationText(posInfoLabel);
        updateBackground(bgLast, 3);
        updateBackground(bgFirst, 2);
    });
}


// CREATE
function createPosInfoLabel() {
    const locationText = new pixi.Text("", {
        fill: 0xffffff,
    });
    locationText.x = 25;
    locationText.y = 50;

    return locationText;
}

function createBackground(texture) {
    let bg = new pixi.TilingSprite(texture, window.innerWidth, window.innerHeight);
    bg.position.set(0, 0);

    return bg;
}

function createSpritesContainer() {
    return new pixi.ParticleContainer(1000, {
        scale: true,
        position: true,
        rotation: true,
        uvs: true,
        alpha: true,
        zIndex: Sort.OTHER_PLAYERS
    });
}

function createInventory() {
    inventory = pixi.Sprite.from("./images/inventory_empty.png");
    inventory.visible = false;
    inventory.anchor.set(0.5, 0.5);
    inventory.position.set(window.innerWidth / 2, window.innerHeight / 2);
    inventory.zIndex = Sort.INVENTORY

    return inventory;
}

function getX(currX, diffX) {
    return currX - diffX + windowWidth;
}

function getY(currY, diffY) {
    return currY - diffY + windowHeight;
}

function updateLocationText(posInfoLabel) {
    let playerCharacter = characterService.getPlayerCharacter();
    posInfoLabel.text = playerCharacter.getLocation();
}

function updateBackground(bg, div) {
    let playerCharacter = characterService.getPlayerCharacter();
    bg.tilePosition.x -= (isNaN(playerCharacter.getDiffX()) ? 0 : playerCharacter.getDiffX()) / div;
    bg.tilePosition.y -= (isNaN(playerCharacter.getDiffY()) ? 0 : playerCharacter.getDiffY()) / div;
}

export function changeStateInventory(state) {
    inventory.visible = state;
}

/// Character
export function renderCharacter(character) {
    let playerCharacter = characterService.getPlayerCharacter();
    let movement = character.movement;
    let sprite = character.sprite;

    let x = getX(movement.x, playerCharacter.movement.x);
    let y = getY(movement.y, playerCharacter.movement.y);
    sprite.position.set(x, y);
    sprite.angle = movement.angle;
}

export function renderCharacterLabel(character) {
    let sprite = character.sprite;
    character.label.position.set(sprite.x - sprite.width / 2, sprite.y - sprite.height - 5);
}

export function createCharacterSprite() {
    let sprite = pixi.Sprite.from("./images/spaceship.png");
    sprite.anchor.set(0.5, 0.5);
    sprite.width = 64;
    sprite.height = 64;

    spritesContainer.addChild(sprite);

    return sprite;
}

export function createCharacterLabel(character) {
    let label = new pixi.Text(character.characterName, {
        fill: 0xffffff,
    });
    app.stage.addChild(label);

    return label;
}

export function removeCharacter(character) {
    spritesContainer.removeChild(character.sprite);
    app.stage.removeChild(character.label);
}
