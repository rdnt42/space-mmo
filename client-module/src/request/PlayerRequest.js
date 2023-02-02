export class PlayerMotionRequest {
    constructor(isUpdate, motion) {
        this.isUpdate = isUpdate;
        this.motion = motion;
    }
}

export class MotionRequest {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}