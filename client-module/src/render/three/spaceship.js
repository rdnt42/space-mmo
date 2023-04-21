import {OBJLoader} from "../../libs/objloader.js";
import * as THREE from "../../libs/three.js"

export function Spaceship(scene) {
    let ship;
    function loadModel() {
        ship.traverse(function (child) {
            if (child.isMesh) child.material.map = texture;
        });
        ship.position.set(0, 0, 0);
        scene.add(ship);
    }

    const manager = new THREE.LoadingManager(loadModel);
    // texture
    const textureLoader = new THREE.TextureLoader(manager);
    const texture = textureLoader.load('../../images/ships/ship2/ship2-texture.bmp');

    // model
    function onProgress(xhr) {
        if (xhr.lengthComputable) {
            const percentComplete = xhr.loaded / xhr.total * 100;
            console.log('model ' + Math.round(percentComplete, 2) + '% downloaded');
        }
    }

    function onError() {
    }

    const loader = new OBJLoader(manager);
    loader.load('../../images/ships/ship2/ship2.obj', function (obj) {
        ship = obj;
        ship.rotation.x = Math.PI / 2;
        ship.scale.set(0.1, 0.1, 0.1)

    }, onProgress, onError);
}