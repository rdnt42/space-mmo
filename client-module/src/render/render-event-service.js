import * as pixi from "../libs/pixi.min.js";
import {Sort} from "../const/RenderSort.js";

let info;
let nameInfoText;
let dscInfoText;

export function initItemInfo() {
    info = new pixi.Container();
    const texture = pixi.Texture.from("./images/items/item_info.png");
    const sprite = new pixi.Sprite(texture);
    sprite.scale.set(1.5);

    const name = new pixi.Text("", {
        fill: 0xffffff,
        fontSize: 16,
    });
    name.x = 100;
    name.y = 40;

    const dsc = new pixi.Text("", {
        fill: 0xffffff,
        fontSize: 16,
    });
    dsc.x = 40;
    dsc.y = 120;

    info.visible = false;
    info.zIndex = Sort.ITEM_INFO;
    dsc.zIndex = sprite.zIndex + 1;
    info.addChild(sprite);
    info.addChild(dsc);
    info.addChild(name);

    dscInfoText = dsc;
    nameInfoText = name;

    return info;
}

export function addShowEvents(sprite) {
    sprite.eventMode = "static";
    sprite.addEventListener("pointerover", () => {
        if (sprite.textureParentObj && sprite.textureParentObj.getShowInfo) {
            showInfo(sprite);
        }
    });
    sprite.addEventListener("pointerout", () => {
        hideInfo();
    });
}

function showInfo(sprite) {
    const showInfo = sprite.textureParentObj.getShowInfo();
    nameInfoText.text = showInfo.name;
    dscInfoText.text = showInfo.dsc;
    info.position.set(sprite.x, sprite.y);
    info.visible = true;
}

function hideInfo() {
    nameInfoText.text = "";
    dscInfoText.text = "";
    info.visible = false;
}