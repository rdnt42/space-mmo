import {GLTFLoader} from '../../libs/GLTFLoader.js';
import * as THREE from "../../libs/three.js"
import {DRACOLoader} from "../../libs/DRACOLoader.js";

export function MTLShip(scene) {
    const id = 4;
    let ship;

    const manager = new THREE.LoadingManager();
    // model
    function onProgress(xhr) {
        if (xhr.lengthComputable) {
            const percentComplete = xhr.loaded / xhr.total * 100;
            console.log('model ' + Math.round(percentComplete, 2) + '% downloaded');
        }
    }

    function onError() {
    }

    const loader = new GLTFLoader();
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath( '/examples/jsm/libs/draco/' );
    loader.setDRACOLoader( dracoLoader );
    loader.load('../../images/ships/ship' + id + '/images/ships/ship4/ship.mtl', function (obj) {
        scene.add( obj.scene );
        obj.scene.scale.set(10, 10, 10)
        obj.scene.rotation.x = Math.PI / 2;
    }, onProgress, onError);
}