import * as pixi from "../libs/pixi.min.js";
import {Sort} from "../const/RenderSort.js";

let info;
let infoText;

export function initItemInfo() {
    info = new pixi.Container();
    const texture = pixi.Texture.from("./images/items/item_info.png");
    const sprite = new pixi.Sprite(texture);

    const text = new pixi.Text("", {
        fill: 0xffffff,
        fontSize: 16,
    });
    text.x = 50;
    text.y = 70;

    info.visible = false;
    info.zIndex = Sort.ITEM_INFO;
    text.zIndex = sprite.zIndex + 1;
    info.addChild(sprite);
    info.addChild(text);
    infoText = text;

    return info;
}

export function addShowEvents(sprite) {
    sprite.eventMode = "static";
    sprite.addEventListener("pointerover", () => {
        if (sprite.textureParentObj && sprite.textureParentObj.showInfo) {
            showInfo(sprite);
        }
    });
    sprite.addEventListener("pointerout", () => {
        hideInfo();
    });
}

function showInfo(sprite) {
    infoText.text = sprite.textureParentObj.showInfo();
    info.position.set(sprite.x, sprite.y);
    info.visible = true;
}

function hideInfo() {
    info.text = "";
    info.visible = false;
}