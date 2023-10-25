import {BulletType} from "../const/BulletType.js";

export const bulletsCfgMap = new Map();

bulletsCfgMap.set(BulletType.Kinetic, {
    fragments: 13,
    speed: 0.2,
    scale: 1.5,
    start: 0,
    fly: 6,
    explosion: 7,
});