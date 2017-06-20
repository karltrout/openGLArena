package engineTest;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;

import java.io.FileNotFoundException;

/**
 * Main simulator Simulator
 * Created by karltrout on 6/6/17.
 */
public class MainSimTest {


    public static void  main (String[] args){

        DisplayManager.createDisplay();

        Loader loader = new Loader();
        RawModel model = null;


        try {
            model = OBJLoader.loadObjModel("A380", loader);
            //model = OBJLoader.loadObjModel("Boeing_757_200PF", loader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ModelTexture texture = new ModelTexture(loader.loadTexture("a380_AIRBUS"));
        //ModelTexture texture = new ModelTexture(loader.loadTexture("test_white"));
        texture.setShineDampner(25);
        texture.setReflectivity(0.5f);

        TexturedModel texturedModel = new TexturedModel(model, texture);


        Player entity = new Player(texturedModel, new Vector3f(0,15.5f,-100),-90,0,0,1.0f);
        //Entity entity = new Entity(texturedModel, new Vector3f(00,-200,-2500),15,0,0,1.0f);

        Terrain terrain = new Terrain(0,-1, loader, new ModelTexture(loader.loadTexture("terrain")));
        Terrain terrain2 = new Terrain(-1,-1, loader, new ModelTexture(loader.loadTexture("terrain")));
        Terrain terrain3 = new Terrain(0,0, loader, new ModelTexture(loader.loadTexture("terrain")));
        Terrain terrain4 = new Terrain(-1,0, loader, new ModelTexture(loader.loadTexture("terrain")));

        Light light = new Light(new Vector3f(200,1000,100), new Vector3f(1,1,1));

        Camera camera = new Camera(entity);

        MasterRenderer masterRenderer = new MasterRenderer(loader);

        /*
         * Game Loop Here.
         */
        while(!Display.isCloseRequested()){

            //entity.increaseRotation(0,0,.20f);

            camera.move();
            entity.move();

            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);
            masterRenderer.processTerrain(terrain3);
            masterRenderer.processTerrain(terrain4);
            masterRenderer.processEntitity(entity);

            masterRenderer.render(light, camera);
            DisplayManager.updateDisplay();

        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }
}
