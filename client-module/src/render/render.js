import * as pixi from '../libs/pixi.min.js';
import * as characterService from "../character-service.js";
import {doubleClickCallback, dragEndCallback} from "../inventory-service.js";
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
const IS_DEBUG = true;
let spritesContainer;
let INVENTORY_CONTAINER;

let characterLabelsMap = new Map();
let posInfoLabel;
let speedLabel;
let bgLast;
let bgFirst;

export function initRender() {
    app = new pixi.Application({
        resizeTo: window,
        // backgroundAlpha: 0.4,
    });
    app.stage.hitArea = app.screen;

    document.body.appendChild(app.view);
    bgLast = createBackground(pixi.Texture.from("./images/background/bgLastLevel.jpg"));
    app.stage.addChild(bgLast);

    bgFirst = createBackground(pixi.Texture.from("./images/background/bgFirstLevel.png"));
    app.stage.addChild(bgFirst);

    spritesContainer = createSpritesContainer();
    app.stage.addChild(spritesContainer);

    posInfoLabel = createPosInfoLabel();
    app.stage.addChild(posInfoLabel);

    window.addEventListener('resize', resizeHandler);
}

export function startEngineTimer() {
    app.ticker.add(() => {
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
    return new pixi.Container(1000, {
        scale: true,
        position: true,
        rotation: true,
        uvs: true,
        alpha: true,
        zIndex: Sort.OTHER_PLAYERS,
    });
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
export function renderCharacter(characterName, sprite, x, y, angle) {
    sprite.position.set(x, y);
    sprite.angle = angle;

    let label = characterLabelsMap.get(characterName);
    label.position.set(sprite.x - sprite.width / 2, sprite.y - sprite.height - 5);
}

export function createCharacter(characterName, shipTypeId, x, y, angle) {
    let textureArr = [];
    let cfg = shipsCfgMap.get(shipTypeId);
    for (let i = 0; i <= cfg.fragments; i++) {
        let img = ('./images/ships/ship' + shipTypeId + '/' + i.toString().padStart(3, '0') + '.png');
        const texture = pixi.Texture.from(img);
        textureArr.push(texture);
    }

    const sprite = new pixi.AnimatedSprite(textureArr);

    sprite.anchor.set(0.5, 0.5);
    sprite.animationSpeed = 0.3;
    sprite.scale.set(cfg.scale);
    sprite.zIndex = Sort.PLAYER;
    sprite.position.set(x, y);
    sprite.angle = angle;

    sprite.play();
    app.stage.addChild(sprite);

    if (IS_DEBUG) {
        let border = new pixi.Graphics();
        border.lineStyle(1, 0xFF0000);
        border.drawCircle(sprite.x, sprite.y, 64);
        border.endFill();
        sprite.addChild(border);
    }

    createCharacterLabel(characterName);

    return sprite;
}

function createCharacterLabel(characterName) {
    let label = new pixi.Text(characterName, {
        fill: 0xffffff,
    });
    label.zIndex = Sort.OTHER_PLAYERS;

    app.stage.addChild(label);
    characterLabelsMap.set(characterName, label);
}

export function removeCharacter(characterName, sprite) {
    sprite.destroy();

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
    inventory.width = 460;
    inventory.height = 617;

    let center = pixi.Sprite.from("./images/inventory_center.png");
    center.anchor.set(0.5, 0.5);
    center.position.set(inventory.width / 2, inventory.height / 2 - 10);
    center.zIndex = inventory.zIndex - 1;

    container.addChild(center);

    container.height = inventory.height;
    container.width = inventory.width;
    container.position.set(app.screen.width / 2, app.screen.height / 2);
    container.pivot.set(container.width / 2, container.height / 2);
    container.zIndex = Sort.INVENTORY;

    const speedText = new pixi.Text("cкорость", {
        fill: 0x000000,
        fontSize: 20,
    });
    speedText.anchor.set(0.5, 0.5);
    speedText.x = 393;
    speedText.y = 35;
    container.addChild(speedText);

    speedLabel = new pixi.Text("", {
        fill: 0x000000,
        fontSize: 20,
    });
    speedLabel.anchor.set(0.5, 0.5);
    speedLabel.x = 393;
    speedLabel.y = 65;

    container.addChild(speedLabel);
    container.sortChildren();

    INVENTORY_CONTAINER = container;
}

export function initCargoCell(idx) {
    const texture = pixi.Texture.WHITE;
    const sprite = new pixi.Sprite(texture);
    sprite.visible = IS_DEBUG;
    sprite.width = 38;
    sprite.height = 40;
    sprite.position.set(164 + idx * (sprite.width + 5), 569);
    sprite.anchor.set(0, 0.5);
    INVENTORY_CONTAINER.addChild(sprite);

    return sprite;
}

export function initEquipmentSlot(equipmentType) {
    const texture = pixi.Texture.WHITE;
    const sprite = new pixi.Sprite(texture);
    sprite.visible = IS_DEBUG;
    sprite.width = 60;
    sprite.height = 60;
    sprite.anchor.set(0.5, 0.5);
    switch (equipmentType) {
        case EquipmentSlotId.Engine:
            sprite.position.set(90, 245);
            break;
        case EquipmentSlotId.FuelTank:
            sprite.position.set(90, 350);
            break;
        case EquipmentSlotId.Scanner:
            sprite.position.set(365, 245);
            break;
        case EquipmentSlotId.Radar:
            sprite.position.set(365, 350);
            break;
        case EquipmentSlotId.CargoHook:
            sprite.position.set(230, 455);
            break;
        case EquipmentSlotId.Hull:
            sprite.position.set(230, 295);
            break;
    }
    INVENTORY_CONTAINER.addChild(sprite);

    return sprite;
}

export function initItem(typeId, equipmentType) {
    let url;
    switch (typeId) {
        case EquipmentSlotId.Engine:
            url = `./images/engine${equipmentType}.png`;
            break;
        case EquipmentSlotId.FuelTank:
            url = `./images/fuel_tank${equipmentType}.png`;
            break;
        case EquipmentSlotId.CargoHook:
            url = `./images/cargo_hook${equipmentType}.png`;
            break;
        case EquipmentSlotId.Hull:
            url = `./images/ships/ship${equipmentType}/preview.png`;
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

export function addToCargoCell(cargo, cell) {
    cargo.position.set(cell.x + cell.width / 2, cell.y);
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

export function hasHalfCollision(r1, r2) {
    let r1Bounds = r1.getBounds();
    let r2Bounds = r2.getBounds();

    return r1Bounds.x + r1Bounds.width * 0.5 > r2Bounds.x &&
        r1Bounds.x < r2Bounds.x + r2Bounds.width * 0.5 &&
        r1Bounds.y + r1Bounds.height * 0.5 > r2Bounds.y &&
        r1Bounds.y < r2Bounds.y + r2Bounds.height * 0.5;
}

function onDragMove(event) {
    if (dragTarget) {
        this.scale.set(0.75);
        dragTarget.parent.toLocal(event.global, null, dragTarget.position);
    }
}

let start;

function onDragStart() {
    start = Date.now();
    dragTarget = this;

    app.stage.on('pointermove', onDragMove);
}

function onDragEnd() {
    if (dragTarget) {
        app.stage.off('pointermove', onDragMove);
        if ((Date.now() - start) > 250) {
            dragEndCallback(dragTarget);
        }
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

function resizeHandler() {
    app.renderer.resize(window.innerWidth, window.innerHeight);
    app.renderer.render(app.stage);
    bgLast.width = window.innerWidth;
    bgLast.height = window.innerHeight;
    bgFirst.width = window.innerWidth;
    bgFirst.height = window.innerHeight;
}