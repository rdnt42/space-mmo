import {OBJLoader} from "../../libs/objloader.js";
import {MTLLoader} from "../../libs/MTLLoader.js";

export function MTLShip(scene) {
    let mesh = null;

    let mtlLoader = new MTLLoader();
    mtlLoader.setPath("../../images/ships/ship4/");
    mtlLoader.load('ship.mtl', function (materials) {
        materials.preload();

        let objLoader = new OBJLoader();
        objLoader.setMaterials(materials);
        objLoader.setPath("../../images/ships/ship4/");
        objLoader.load('SciFi_Fighter_AK5.obj', function (object) {
            mesh = object;
            mesh.rotation.x = Math.PI / 2;
            mesh.position.set(0, 0, 0);
            mesh.scale.set(0.06, 0.06, 0.06)
            scene.add(mesh);
        });
    });
}