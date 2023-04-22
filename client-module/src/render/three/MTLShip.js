import {OBJLoader} from "../../libs/objloader.js";
import {MTLLoader} from "../../libs/MTLLoader.js";

export function MTLShip(scene, id) {
    let mesh = null;

    let mtlLoader = new MTLLoader();
    mtlLoader.setPath('../../images/ships/ship' + id + '/');
    mtlLoader.load('ship.mtl', function (materials) {
        materials.preload();

        let objLoader = new OBJLoader();
        objLoader.setMaterials(materials);
        objLoader.setPath('../../images/ships/ship1/');
        objLoader.load('ship.obj', function (object) {
            mesh = object;
            mesh.rotation.x = Math.PI / 2;
            mesh.position.set(0, 0, 0);
            mesh.scale.set(0.05, 0.05, 0.05)
            scene.add(mesh);
        });
    });
}