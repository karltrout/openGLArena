package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import tools.MousePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CCW;

/**
 * Created by karltrout on 6/14/17.
 */
public class MasterRenderer {

    private StaticShader shader = new StaticShader();

    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;

    private TerrainShader terrainShader = new TerrainShader();

    private Matrix4f projectionMatrix;

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 10000.0f;

    //SKY COLOR  = 83.1, 94.5, 97.3
    private static float RED   = .831f;
    private static float GREEN = .945f;
    private static float BLUE  = .973f;


    private Map<TexturedModel, List<Entity>> entities = new HashMap();
    private List<Terrain> terrains = new ArrayList<>();

    private SkyboxRenderer skyboxRenderer;
    private ShadowMapMasterRenderer shadowMapRenderer;

    public MasterRenderer(Loader loader, Camera camera){

        GL11.glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK_RIGHT);
        glFrontFace(GL_CCW);

        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        shadowMapRenderer = new ShadowMapMasterRenderer(camera);

    }

    public void render(Light sun, Camera camera){

        prepare();
        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);

        renderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());

        shader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
        terrainShader.stop();

        skyboxRenderer.render(camera, RED, GREEN, BLUE);
        terrains.clear();
        entities.clear();

    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntitity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null){
            batch.add(entity);
        }
        else{
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
        shadowMapRenderer.cleanUp();
    }

    public void renderShadowMap(List<Entity> entitiesList, Light sun){
        for (Entity entity: entitiesList){
            processEntitity(entity);
        }
        shadowMapRenderer.render(entities, sun);
        entities.clear();
    }
    public int getShadowMapTexture(){
        return this.shadowMapRenderer.getShadowMap();
    }

    public void prepare(){

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(RED,GREEN ,BLUE,1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());

    }


    private  void createProjectionMatrix(){

        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ( 1f / Math.tan(Math.toRadians(FOV/2f)) );
        float x_scale = y_scale/aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;

    }
}
