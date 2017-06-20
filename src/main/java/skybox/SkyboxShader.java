package skybox;

/**
 * Created by karltrout on 6/17/17.
 */

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import tools.Maths;

public class SkyboxShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/main/java/skybox/skyboxVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/main/java/skybox/skyboxFragmentShader.frag";

    private static final float ROTATION_SPEED = 0.25f;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_cubeMap;
    private int location_cubeMap_night;
    private int location_blendfactor;

    private float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;

        rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);

        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadFogColor(float r, float g, float b){
        super.loadVector(location_fogColor, new Vector3f(r, g, b));
    }
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_cubeMap = super.getUniformLocation("cubeMap_night");
        location_cubeMap_night = super.getUniformLocation("cubeMap_night");
        location_blendfactor = super.getUniformLocation("blendFactor");
    }

    public void connectTextureUnits(){
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap_night, 1);

    }

    public void loadBlendFactor(float value){
        super.loadFloat(location_blendfactor, value);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
