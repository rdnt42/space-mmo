import * as pixi from './libs/pixi.min.js';
import * as characterService from "./character-service.js";
import {getAllCharacters} from "./character-service.js";

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
let inventoryContainer;

let windowWidth;
let windowHeight;

export function initRender() {
    windowWidth = Math.floor(window.innerWidth / 2);
    windowHeight = Math.floor(window.innerHeight / 2);

    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("./images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("./images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    inventoryContainer = createInventory();

    spritesContainer = createSpritesContainer();
    app.stage.addChild(spritesContainer);

    let posInfoLabel = createPosInfoLabel();
    app.stage.addChild(posInfoLabel);

    //TODO #24 window.addEventListener('resize', resize);

    app.ticker.add(() => {
        renderCharacters();
        updateLocationText(posInfoLabel);
        updateBackground(bgLast, 3);
        updateBackground(bgFirst, 2);
        app.stage.sortChildren();
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
    inventoryContainer.visible = state;
}

/// Character
function renderCharacters() {
    let characters = getAllCharacters();
    let playerCharacter = characterService.getPlayerCharacter();
    let x = playerCharacter.movement.x;
    let y = playerCharacter.movement.y;

    for (let character of characters.values()) {
        let movement = character.movement;
        let sprite = character.sprite;

        let newX = getX(movement.x, x);
        let newY = getY(movement.y, y);
        sprite.position.set(newX, newY);
        sprite.angle = movement.angle;
        character.label.position.set(sprite.x - sprite.width / 2, sprite.y - sprite.height - 5);
    }
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
    label.zIndex = Sort.OTHER_PLAYERS;
    app.stage.addChild(label);

    return label;
}

export function removeCharacter(character) {
    spritesContainer.removeChild(character.sprite);
    app.stage.removeChild(character.label);
}

/// Inventory
function createInventory() {
    let inventoryContainer = new pixi.Container();
    app.stage.addChild(inventoryContainer);
    inventoryContainer.visible = true;

    let inventory = pixi.Sprite.from("./images/inventory_empty.png");
    inventory.position.set(inventoryContainer.width / 2, inventoryContainer.height / 2)
    inventoryContainer.addChild(inventory);
    inventory.width = 436;
    inventory.height = 500;
    inventory.position.set(0, 0);

    let holdContainer = new pixi.Container();
    let hold = pixi.Sprite.from("./images/hold_empty.png");
    holdContainer.addChild(hold);
    hold.width = 336;
    hold.height = 76;
    hold.anchor.set(0.5, 0);
    hold.position.set(inventory.width / 2, inventory.height);

    inventoryContainer.addChild(holdContainer);
    inventoryContainer.height = inventory.height + hold.height;
    inventoryContainer.width = inventory.width;
    inventoryContainer.position.set(app.screen.width / 2, app.screen.height / 2);
    inventoryContainer.pivot.set(inventoryContainer.width / 2, inventoryContainer.height / 2);
    inventoryContainer.zIndex = Sort.INVENTORY;

    return inventoryContainer;
}

export function renderEngines() {

}