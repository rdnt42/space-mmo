import * as pixi from './libs/pixi.min.js';
import * as characterService from "./character-service.js";
import {getAllCharacters} from "./character-service.js";
import {doubleClickCallback} from "./inventory-service.js";
import {EquipmentType} from "./const/EquipmentType.js";

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

// fast container for all spritesContainer (faster than Container in 3-5 times)
let spritesContainer;
let INVENTORY_CONTAINER;
let INVENTORY;
let CARGO;

let charactersMap = new Map();
let characterLabelsMap = new Map();

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
    INVENTORY_CONTAINER.visible = state;
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
    INVENTORY = inventory;

    let cargo = pixi.Sprite.from("./images/cargo_empty.png");
    cargo.width = 336;
    cargo.height = 76;
    cargo.anchor.set(0.5, 0);
    cargo.position.set(inventory.width / 2, inventory.height);
    container.addChild(cargo);
    CARGO = cargo;

    container.height = inventory.height + cargo.height;
    container.width = inventory.width;
    container.position.set(app.screen.width / 2, app.screen.height / 2);
    container.pivot.set(container.width / 2, container.height / 2);
    container.zIndex = Sort.INVENTORY;

    INVENTORY_CONTAINER = container;
}

export function initCargoCell(cell, idx) {
    cell.width = 40;
    cell.height = 40;
    cell.position.set(91 + idx * (cell.width + 3), INVENTORY.height + CARGO.height / 2 - 4);
    cell.anchor.set(0, 0.5);
    INVENTORY_CONTAINER.addChild(cell);
}

export function initEquipmentSlot(equipmentCell, equipmentType) {
    equipmentCell.width = 60;
    equipmentCell.height = 60;
    equipmentCell.anchor.set(0.5, 0.5);
    switch (equipmentType) {
        case EquipmentType.Engine:
            equipmentCell.position.set(85, 235);
            break;
        case EquipmentType.FuelTank:
            equipmentCell.position.set(350, 235);
    }
    INVENTORY_CONTAINER.addChild(equipmentCell);
}

export function initEquipment(equipment) {
    INVENTORY_CONTAINER.addChild(equipment);

    equipment.interactive = true;
    equipment.buttonMode = true;
    equipment.cursor = 'pointer';
    equipment
        .on('mousedown', onDragStart)
        .on('touchstart', onDragStart)
        .on('mouseup', onDragEnd)
        .on('mouseupoutside', onDragEnd)
        .on('touchend', onDragEnd)
        .on('touchendoutside', onDragEnd)
        .on('mousemove', onDragMove)
        .on('touchmove', onDragMove)
        .on('click', onClick)
    equipment.zIndex = Sort.EQUIPMENT;
    equipment.anchor.set(0.5);
    equipment.scale.set(0.5);
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

export function equip(equipment, slot) {

}

export function addToCargo(equipment, hold, isVisible) {
    equipment.visible = isVisible;
    equipment.position.set(hold.x + hold.width / 2, hold.y);
}

