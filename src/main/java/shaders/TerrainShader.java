package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import tools.Maths;

/**
 * Created by karltrout on 6/14/17.
 */
public class TerrainShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/main/java/shaders/terrainVertexShader.vert";
    private static final String FRAGMENT_FILE = "src/main/java/shaders/terrainFragmentShader.frag";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColor;
    private int location_toShadowMapSpace;
    private int location_backgroundTexture;
    private int location_shadowMap;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightColor = super.getUniformLocation("lightColor");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_shadowMap  = super.getUniformLocation("shadowMap");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");

        location_skyColor= super.getUniformLocation("skyColor");
        location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void  connectTextureUnits(){
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_shadowMap, 1);
    }

    public void loadShadowMapSpace(Matrix4f shadowMap){
        super.loadMatrix(location_toShadowMapSpace, shadowMap);
    }

    public void  loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector(location_skyColor, new Vector3f(r,g,b));
    }

    public void loadShineAndReflectivity( float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadLight(Light light){

        super.loadVector(location_lightColor, light.getColor());
        super.loadVector(location_lightPosition, light.getPosition());

    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

}
