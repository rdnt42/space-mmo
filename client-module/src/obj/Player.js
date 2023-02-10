class Player {
    constructor(playerName, x, y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.speed = 0;
        this.angle = 270;
        this.maxSpeed = 10;
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

    turnRight() {
        if((this.angle += 5) >= 360) {
            this.angle = this.angle % 360;
        }
    }

    turnLeft() {
        if((this.angle -= 5) < 0) {
            this.angle = this.angle % 360 + 360;
        }
    }

    decreaseSpeed() {
        if (player.speed > (this.maxSpeed / 2) * -1) {
            player.speed -= 1;
        }
    }

    increaseSpeed() {
        if (player.speed < this.maxSpeed) {
            player.speed += 1;
        }
    }

}

const player = new Player();

export default player;