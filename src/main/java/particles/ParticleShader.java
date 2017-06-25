package particles;

import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector2f;
import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/particles/particleVShader.vert";
	private static final String FRAGMENT_FILE = "src/main/java/particles/particleFShader.frag";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	private int location_texOffset1;
	private int location_texOffset2;
	private int location_texCoordInfo;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_texOffset1 = super.getUniformLocation("texOffset1");
		location_texOffset2 = super.getUniformLocation("texOffset2");
		location_texCoordInfo = super.getUniformLocation("texCoordInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadTextureCoordInfo(Vector2f offSet1, Vector2f offSet2, float numRows, float blend){
		super.load2DVector(location_texOffset1, offSet1);
		super.load2DVector(location_texOffset2, offSet2);
		super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
	}
	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
