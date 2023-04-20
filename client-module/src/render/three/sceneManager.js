import {Background} from "./background.js";
import {Spaceship} from "./spaceship.js";
import * as THREE from "../../libs/three.js"

export function SceneManager(canvas) {

    const screenDimensions = {
        width: 1920,
        height: 1080
    };

    const scene = buildScene();
    const renderer = buildRender(screenDimensions);
    const camera = buildCamera(screenDimensions);

    const ambientLight = new THREE.AmbientLight('#ffffff', 1.5);
    scene.add(ambientLight);

    const dynamicSubjects = [];
    createSceneSubjects();


    function buildScene() {
        return new THREE.Scene();
    }

    function buildRender({ width, height }) {
        const renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true, alpha: true });

        renderer.setClearColor("#222222");
        renderer.setSize(width, height);

        return renderer;
    }

    function buildCamera({ width, height }) {

        const nearPlane = 1;
        const farPlane = 1000;
        const camera = new THREE.OrthographicCamera(-width/2, width/2, height/2, -height/2, nearPlane, farPlane);

        camera.position.z = 10;

        return camera;
    }

    function createSceneSubjects() {
        let theBackground = new Background(scene);
        let theSpaceship = new Spaceship(scene);

        dynamicSubjects.push(theSpaceship);
    }


    this.update = function() {

        renderer.render(scene, camera);
    }

    this.onWindowResize = function() {
        const { width, height } = canvas;

        screenDimensions.width = width;
        screenDimensions.height = height;

        renderer.setSize(width, height);

        camera.left = -width / 2;
        camera.right = width / 2;
        camera.top = height / 2;
        camera.bottom = -height / 2;
        camera.updateProjectionMatrix();
    }
}