package engineTest;

import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleMaster;
import renderEngine.*;
import terrains.Terrain;
import terrains.TerrainTexture;
import terrains.TerrainTexturePack;
import tools.MousePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Main simulator Simulator
 * Created by karltrout on 6/6/17.
 */
public class MainSimTest {

    private static List<Entity> entities = new ArrayList<>();


    public static void  main (String[] args){

        DisplayManager.createDisplay();

        Loader loader = new Loader();

        TerrainTexture  backgroundTexture = new TerrainTexture(loader.loadTexture("terrain"));
        TerrainTexturePack pack = new TerrainTexturePack(backgroundTexture);

        Terrain terrain = new Terrain(-1,-1, loader, pack, null);

        Light light = new Light(new Vector3f(1000000,1000000,-1000000), new Vector3f(1,1,1));

        Entity entity = new Entity(null, new Vector3f(0.0f,0.0f,0.0f),0f,0f,0f,1f);
        Camera camera = new Camera(entity);

        MasterRenderer masterRenderer = new MasterRenderer(loader, camera);
        ParticleMaster.init(loader, masterRenderer.getProjectionMatrix());

        MousePicker picker = new MousePicker(camera, masterRenderer.getProjectionMatrix());

        List<GuiTexture> guis = new ArrayList<>();
        // GuiTexture shadowBoxGui = new GuiTexture(masterRenderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        // guis.add(shadowBoxGui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        /*
         * Game Loop Here.
         */
        while(!Display.isCloseRequested()){

            camera.move();

            picker.update();

            ParticleMaster.update(camera );

            masterRenderer.renderShadowMap(entities, light);
            //System.out.println(picker.getCurrentRay());

            masterRenderer.processTerrain(terrain);

            masterRenderer.render(light, camera);
            ParticleMaster.renderParticles(camera);
            guiRenderer.render(guis);

            DisplayManager.updateDisplay();

        }

        ParticleMaster.cleanUp();
        guiRenderer.cleanUp();
        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
