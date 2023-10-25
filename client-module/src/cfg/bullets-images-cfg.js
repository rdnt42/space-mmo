import {BulletType} from "../const/BulletType.js";

export const bulletsCfgMap = new Map();

bulletsCfgMap.set(BulletType.Kinetic, {
    fragments: 13,
    speed: 0.2,
    scale: 1,
    start: 0,
    fly: 6,
    explosion: 7,
});

bulletsCfgMap.set(BulletType.Electric, {
    fragments: 13,
    speed: 0.1,
    scale: 1,
    start: 0,
    fly: 6,
    explosion: 7,
});

bulletsCfgMap.set(BulletType.Thermal, {
    fragments: 13,
    speed: 0.1,
    scale: 1,
    start: 0,
    fly: 4,
    explosion: 5,
});