import * as THREE from "../../libs/three.js"
import {OBJShip} from "./OBJShip.js";
import {Background} from "./background.js";

let camera, scene, renderer;
let firstLevel, lastLevel;

function init() {
// Initialize the scene
    scene = new THREE.Scene();

    // Initialize the camera
    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.set(0, 0, 500);
    camera.lookAt(scene.position);

    const ambientLight = new THREE.AmbientLight('#ffffff', 1.5);
    scene.add(ambientLight);

    const pointLight = new THREE.PointLight(0xffffff, 0.8);
    camera.add(pointLight);
    scene.add(camera);

    // Initialize the renderer
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    document.body.appendChild(renderer.domElement);

    OBJShip(scene, 1);
    lastLevel = Background(scene, -1, '../../images/background/bgLastLevel.jpg');
    firstLevel = Background(scene, 0, '../../images/background/bgFirstLevel.png');
}


function render() {
    requestAnimationFrame(render);

    renderer.render(scene, camera);
    firstLevel.position.x = camera.position.x * 0.1;
    firstLevel.position.y = camera.position.y * 0.1;
}

export class ThreeEngine {
    constructor() {
        init();
        render();
    }
}

