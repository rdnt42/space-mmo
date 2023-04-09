class Inventory {
    constructor(){
        this.isOpen = false;
    }

    changeState() {
        return this.isOpen = !this.isOpen
    }
}

const inventory = new Inventory()

export default inventory;