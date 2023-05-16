import * as socket from "./websocket-service.js";

createConnectInfo();

function createConnectInfo() {
    let input = document.createElement("input");
    input.innerHTML = "InputText";
    input.type = "text";
    input.name = "inputText";
    document.body.appendChild(input);

    let btn = document.createElement("button");
    btn.innerHTML = "Submit";
    btn.type = "submit";
    btn.name = "formBtn";
    document.body.appendChild(btn);
    btn.onclick = function () {
        console.log(`characterName: ${input.value}`)
        socket.initSocketConnection(input.value);
        input.remove();
        btn.remove();
    };
}