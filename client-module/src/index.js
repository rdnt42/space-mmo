import * as socket from "./websocket-service.js";

let input;
let btn;
createConnectInfo();

function createConnectInfo() {
    input = document.createElement("input");
    input.innerHTML = "InputText";
    input.type = "text";
    input.name = "inputText";
    input.addEventListener('keydown', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            init();
        }
    })
    document.body.appendChild(input);

    btn = document.createElement("button");
    btn.innerHTML = "Submit";
    btn.type = "submit";
    btn.name = "formBtn";
    document.body.appendChild(btn);
    btn.onclick = function () {
        init();
    };
}

function init() {
    console.log(`characterName: ${input.value}`)
    socket.initSocketConnection(input.value);
    input.remove();
    btn.remove();
}