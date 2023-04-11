class Player {
    constructor(playerName, x, y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.speed = 0;
        this.angle = 0;
        this.maxSpeed = 600;
    };

    getDiffX() {
        return this.x - this.prevX;
    }

    getDiffY() {
        return this.y - this.prevY;
    }

    getLocation() {
        return "X: " + this.x + " Y: " + this.y + " speed: " + this.speed + " angle: " + this.angle;
    }

}

const player = new Player();

export default player;