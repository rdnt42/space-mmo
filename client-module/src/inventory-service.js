import * as render from "./render-service.js";
import inventory from "./obj/Inventory.js";

document.addEventListener("keydown", (event) => {
  if (event.key === "i") {
    render.changeStateInventory(inventory.changeState())
  }
});
