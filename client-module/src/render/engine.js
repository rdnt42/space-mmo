import * as pixi from '../libs/pixi.min.js';
import * as characterService from "../character-service.js";
import {doubleClickCallback} from "../inventory-service.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import {shipsCfgMap} from "../cfg/images-cfg.js";

let app;
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

let posInfoLabel;
let speedLabel;
let bgLast;
let bgFirst;

export function initEngine() {
    app = new pixi.Application({
        resizeTo: window
    });
    app.stage.hitArea = app.screen;
    app.stage.on('pointerup', onDragEnd);
    app.stage.on('pointerupoutside', onDragEnd);

    windowWidth = Math.floor(window.innerWidth / 2);
    windowHeight = Math.floor(window.innerHeight / 2);

    document.body.appendChild(app.view);
    bgLast = createBackground(pixi.Texture.from("./images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    bgFirst = createBackground(pixi.Texture.from("./images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    spritesContainer = createSpritesContainer();
    app.stage.addChild(spritesContainer);

    posInfoLabel = createPosInfoLabel();
    app.stage.addChild(posInfoLabel);

}

//TODO #24 window.addEventListener('resize', resize);
export function startEngineTimer() {
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
    bg.tilePosition.set(0, 0);

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
    let addX = parseFloat(playerCharacter.getDiffX() / div);
    let addY = parseFloat(playerCharacter.getDiffY() / div);
    bg.tilePosition.x -= addX;
    bg.tilePosition.y -= addY;
}

export function changeStateInventory(state) {
    INVENTORY_CONTAINER.visible = state;
}

/// Character
function renderCharacters() {
    let characters = characterService.getAllCharacters();
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

export function createCharacter(characterName, shipTypeId) {
    let textureArr = [];
    for (let i = 0; i < shipsCfgMap.get(shipTypeId); i++) {
        let img = ('./images/ships/ship' + shipTypeId + '/' + i.toString().padStart(3, '0') + '.png');
        const texture = pixi.Texture.from(img);
        textureArr.push(texture);
    }

    const sprite = new pixi.AnimatedSprite(textureArr);

    sprite.anchor.set(0.5, 0.5);
    sprite.width = 0.75;
    sprite.height = 0.75;
    sprite.animationSpeed = 0.3;
    sprite.play();

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

    speedLabel = new pixi.Text("0", {
        fill: 0x000000,
        fontSize: 20,
    });
    speedLabel.x = 378;
    speedLabel.y = 48;
    speedLabel.anchor.set(0.5, 0.5);

    container.addChild(speedLabel);

    INVENTORY_CONTAINER = container;
}

export function initCargoCell(idx) {
    const texture = pixi.Texture.WHITE;
    const sprite = new pixi.Sprite(texture);
    sprite.visible = false;
    sprite.width = 40;
    sprite.height = 40;
    sprite.position.set(91 + idx * (sprite.width + 3), INVENTORY.height + CARGO.height / 2 - 4);
    sprite.anchor.set(0, 0.5);
    INVENTORY_CONTAINER.addChild(sprite);

    return sprite;
}

export function initEquipmentSlot(equipmentType) {
    const texture = pixi.Texture.WHITE;
    const sprite = new pixi.Sprite(texture);
    sprite.visible = false;
    sprite.width = 60;
    sprite.height = 60;
    sprite.anchor.set(0.5, 0.5);
    switch (equipmentType) {
        case EquipmentSlotId.Engine:
            sprite.position.set(85, 235);
            break;
        case EquipmentSlotId.FuelTank:
            sprite.position.set(85, 335);
            break;
        case EquipmentSlotId.Scanner:
            sprite.position.set(350, 235);
            break;
        case EquipmentSlotId.Radar:
            sprite.position.set(350, 335);
            break;
    }
    INVENTORY_CONTAINER.addChild(sprite);

    return sprite;
}

export function initItem(typeId, subTypeId) {
    let url;
    switch (typeId) {
        case EquipmentSlotId.Engine:
            url = "./images/engine" + subTypeId + ".png";
            break;
        case EquipmentSlotId.FuelTank:
            url = "./images/fuel_tank" + subTypeId + ".png";
            break;
    }
    const texture = pixi.Texture.from(url);
    const sprite = new pixi.Sprite(texture);
    sprite.eventMode = "static";

    sprite.buttonMode = true;
    sprite.cursor = 'pointer';
    sprite
        .on('mousedown', onDragStart)
        .on('touchstart', onDragStart)
        .on('mouseup', onDragEnd)
        .on('mouseupoutside', onDragEnd)
        .on('touchend', onDragEnd)
        .on('touchendoutside', onDragEnd)
        .on('mousemove', onDragMove)
        .on('touchmove', onDragMove)
        .on('click', onClick)
    sprite.zIndex = Sort.EQUIPMENT;
    sprite.anchor.set(0.5);
    sprite.visible = false;

    INVENTORY_CONTAINER.addChild(sprite);

    return sprite;
}

export function addToCargoCell(cargo, hold) {
    cargo.position.set(hold.x + hold.width / 2, hold.y);
    cargo.scale.set(0.5);
    cargo.visible = true;
}

export function removeFromCargoCell(cargo) {
    cargo.visible = false;
}

export function addToEquipmentSlot(equipment, slot) {
    equipment.position.set(slot.x, slot.y);
    equipment.scale.set(1);
    equipment.visible = true;
}

export function removeFromEquipmentSlot(equipment) {
    equipment.visible = false;
}

export function setSpeedLabel(speed) {
    speedLabel.text = speed;
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
        dragTarget = null;
    }
}

// event 'dblClick' doesn't work
let prevClickTime = Date.now();

function onClick() {
    if ((Date.now() - prevClickTime) <= 500) {
        doubleClickCallback(this);
    }
    prevClickTime = Date.now();
}