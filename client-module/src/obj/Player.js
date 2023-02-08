class Player {
    constructor(playerName, x, y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
    };

    getDiffX() {
        return this.x - this.prevX;
    }

    getDiffY() {
        return this.y - this.prevY;
    }

}

const player = new Player();

export default player;