import {OBJLoader} from "../../libs/objloader.js";
import * as THREE from "../../libs/three.js"

let object;
export function Spaceship(scene) {
    function loadModel() {

        object.traverse( function ( child ) {

            if ( child.isMesh ) child.material.map = texture;

        } );

        object.position.y = - 95;
        scene.add( object );

    }

    const manager = new THREE.LoadingManager( loadModel );

    // texture

    const textureLoader = new THREE.TextureLoader( manager );
    const texture = textureLoader.load( '../../images/ships/SciFi_Fighter_AK5-diffuse.jpg' );

    // model

    function onProgress( xhr ) {

        if ( xhr.lengthComputable ) {

            const percentComplete = xhr.loaded / xhr.total * 100;
            console.log( 'model ' + Math.round( percentComplete, 2 ) + '% downloaded' );

        }

    }

    function onError() {}

    const loader = new OBJLoader( manager );
    loader.load( '../../images/ships/SciFi_Fighter_AK5.obj', function ( obj ) {

        object = obj;
        object.rotation.x = -Math.PI / 2;
        object.scale.set(0.1,0.1,0.1)

    }, onProgress, onError );

}