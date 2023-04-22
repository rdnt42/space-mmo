import {OBJLoader} from "../../libs/objloader.js";
import * as THREE from "../../libs/three.js"

export function OBJShip(scene, id) {
    let ship;

    function loadModel() {
        ship.traverse((child) => {
            if (child instanceof THREE.Mesh) {
                child.material.map = texture1;
            }
        });
        ship.rotation.x = Math.PI / 2;
        ship.position.set(0, 0, 0);
        ship.scale.set(0.03, 0.03, 0.03)

        scene.add(ship);
    }

    const manager = new THREE.LoadingManager(loadModel);
    // texture
    const textureLoader = new THREE.TextureLoader(manager);
    const texture1 = textureLoader.load('../../images/ships/ship' + id + '/texture.jpg');

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
    loader.load('../../images/ships/ship' + id + '/ship.obj', function (obj) {
        scene.add(obj);
        ship = obj;
    }, onProgress, onError);
}