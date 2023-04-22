import * as THREE from "../../libs/three.js"

export function Background(scene, level, url, scale) {
    const loader = new THREE.TextureLoader();
    const texture = loader.load(url);
    const geometry = new THREE.PlaneGeometry(window.innerWidth , window.innerHeight );
    const material = new THREE.MeshBasicMaterial({ map: texture });
    const mesh = new THREE.Mesh(geometry, material);

    mesh.position.set(0, 0, level);

    scene.add(mesh);

    return mesh;
}