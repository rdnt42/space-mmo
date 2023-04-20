import * as THREE from "../../libs/three.js"

export function Background(scene) {
    let geometry = new THREE.PlaneGeometry(3000, 3000)

    const textureLoader = new THREE.TextureLoader()
    let material = new THREE.MeshBasicMaterial({ map: textureLoader.load("../../images/background/bgLastLevel.jpg")})
    let bg = new THREE.Mesh(geometry, material)

    bg.rotation.z = -Math.PI / 2;
    bg.position.z = -900;
    bg.position.y = 1000;

    scene.add(bg);
}