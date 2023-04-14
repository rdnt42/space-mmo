import * as pixi from './libs/pixi.min.js';
import * as characterService from "./character-service.js";
import {getAllCharacters} from "./character-service.js";
import {doubleClickCallback} from "./inventory-service.js";

const app = new pixi.Application({
    resizeTo: window
});
let dragTarget = null;

const Sort = {
    OTHER_PLAYERS: 30,
    PLAYER: 90,
    INVENTORY: 100,
    EQUIPMENT: 110
}
const HOLD_CELL_NUM = 6;

// fast container for all spritesContainer (faster than Container in 3-5 times)
let spritesContainer;
let inventoryContainer;

let charactersMap = new Map();
let characterLabelsMap = new Map();
let holdCellsEmpty = [];

let windowWidth;
let windowHeight;

export function initRender() {
    app.stage.interactive = true;
    app.stage.hitArea = app.screen;
    app.stage.on('pointerup', onDragEnd);
    app.stage.on('pointerupoutside', onDragEnd);

    windowWidth = Math.floor(window.innerWidth / 2);
    windowHeight = Math.floor(window.innerHeight / 2);

    document.body.appendChild(app.view);
    const bgLast = createBackground(pixi.Texture.from("./images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    const bgFirst = createBackground(pixi.Texture.from("./images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

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
        let sprite = charactersMap.get(character.characterName);

        let newX = getX(movement.x, x);
        let newY = getY(movement.y, y);
        sprite.position.set(newX, newY);
        sprite.angle = movement.angle;

        let label = characterLabelsMap.get(character.characterName);
        label.position.set(sprite.x - sprite.width / 2, sprite.y - sprite.height - 5);
    }
}

export function createCharacterSprite(characterName) {
    let sprite = pixi.Sprite.from("./images/spaceship.png");
    sprite.anchor.set(0.5, 0.5);
    sprite.width = 64;
    sprite.height = 64;

    spritesContainer.addChild(sprite);
    charactersMap.set(characterName, sprite);
}

export function createCharacterLabel(characterName) {
    let label = new pixi.Text(characterName, {
        fill: 0xffffff,
    });
    label.zIndex = Sort.OTHER_PLAYERS;
    app.stage.addChild(label);
    characterLabelsMap.set(characterName, label);
}

export function removeCharacter(characterName) {
    let sprite = charactersMap.get(characterName);
    charactersMap.delete(characterName);
    spritesContainer.removeChild(sprite);

    let label = characterLabelsMap.get(characterName);
    characterLabelsMap.delete(characterName);
    app.stage.removeChild(label);
}

/// Inventory
export function createInventory() {
    let container = new pixi.Container();
    app.stage.addChild(container);
    container.visible = false;

    let inventory = pixi.Sprite.from("./images/inventory_empty.png");
    inventory.position.set(container.width / 2, container.height / 2)
    container.addChild(inventory);
    inventory.width = 436;
    inventory.height = 500;
    inventory.position.set(0, 0);

    let hold = pixi.Sprite.from("./images/hold_empty.png");
    hold.width = 336;
    hold.height = 76;
    hold.anchor.set(0.5, 0);
    hold.position.set(inventory.width / 2, inventory.height);
    container.addChild(hold);

    for (let i = 0; i < HOLD_CELL_NUM; i++) {
        const rectangle = pixi.Sprite.from(pixi.Texture.WHITE);
        rectangle.width = 40;
        rectangle.height = 40;
        rectangle.position.set(91 + i * (rectangle.width + 3), inventory.height + hold.height / 2 - 4);
        rectangle.anchor.set(0, 0.5);
        container.addChild(rectangle);
        holdCellsEmpty.push(rectangle);
    }
    let engineCell = pixi.Sprite.from(pixi.Texture.WHITE);
    engineCell.width = 60;
    engineCell.height = 60;
    engineCell.anchor.set(0.5, 0.5);
    engineCell.position.set(85, 235);
    container.addChild(engineCell);

    container.height = inventory.height + hold.height;
    container.width = inventory.width;
    container.position.set(app.screen.width / 2, app.screen.height / 2);
    container.pivot.set(container.width / 2, container.height / 2);
    container.zIndex = Sort.INVENTORY;

    inventoryContainer = container;
}

export function initEquipment(equipment, idx) {
    inventoryContainer.addChild(equipment);

    equipment.interactive = true;
    equipment.buttonMode = true;
    equipment.cursor = 'pointer';
    equipment
        // events for drag start
        .on('mousedown', onDragStart)
        .on('touchstart', onDragStart)
        // events for drag end
        .on('mouseup', onDragEnd)
        .on('mouseupoutside', onDragEnd)
        .on('touchend', onDragEnd)
        .on('touchendoutside', onDragEnd)
        // events for drag move
        .on('mousemove', onDragMove)
        .on('touchmove', onDragMove)
        .on('click', onClick)
    equipment.zIndex = Sort.EQUIPMENT;
    console.log(equipment)

    equipment.anchor.set(0.5);
    equipment.scale.set(0.6);
    if (idx <= HOLD_CELL_NUM) {
        equipment.visible = true;
        equipment.position.set(holdCellsEmpty[idx].x + holdCellsEmpty[idx].width / 2, holdCellsEmpty[idx].y);
    } else {
        equipment.visible = false;
    }

}

function onDragMove(event) {
    if (dragTarget) {
        dragTarget.parent.toLocal(event.global, null, dragTarget.position);
    }
}

function onDragStart() {
    dragTarget = this;
    app.stage.on('pointermove', onDragMove);
}

function onDragEnd() {
    if (dragTarget) {
        app.stage.off('pointermove', onDragMove);
        // TODO 1 when equipped
        // dragTarget.scale.set(1);
        dragTarget = null;
    }
}

// TODO event 'dblClick' doesn't work
let prevClickTime = Date.now();
function onClick() {
    if ((Date.now() - prevClickTime) <= 500) {
        doubleClickCallback(this);
    }
    prevClickTime = Date.now();
}

