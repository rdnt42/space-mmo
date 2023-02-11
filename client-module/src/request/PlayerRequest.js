export class PlayerMotionRequest {
    constructor(isUpdate, motion) {
        this.isUpdate = isUpdate;
        this.motion = motion;
    }
}

export class MotionRequest {
    constructor(x, y, angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}