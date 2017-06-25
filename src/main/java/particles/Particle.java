package particles;


import entities.Camera;
import entities.Player;
import javafx.print.PageLayout;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;



    private float  elapsedTime = 0;
    private float distance;


    private ParticleTexture texture;

    private Vector2f textureOffset1 = new Vector2f();
    private Vector2f textureOffset2 = new Vector2f();

    private float blend;

    public Vector2f getTextureOffset1() {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public float getBlend() {
        return blend;
    }

    public float getDistance() {
        return distance;
    }

    public Particle(ParticleTexture texture , Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {

        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;

        ParticleMaster.addParticle(this);

    }

    public ParticleTexture getTexture() {
        return texture;
    }

    protected boolean update(Camera camera){

        this.velocity.y = this.velocity.y + Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change, position,position);
        updateTextureCoordInfo();
        distance = Vector3f.sub(camera.getPosition(),position, null).lengthSquared();
        elapsedTime = elapsedTime + DisplayManager.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordInfo(){
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasProgression = lifeFactor*stageCount;
        int index =(int) Math.floor(atlasProgression);
        int index2 = (index < stageCount -1) ? index +1:index;
        this.blend = atlasProgression % 1;
        setTexturOffset(textureOffset1, index);
        setTexturOffset(textureOffset2, index2);

    }

    private void setTexturOffset(Vector2f offset, int index){
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float) column / texture.getNumberOfRows();
        offset.y = (float) row / texture.getNumberOfRows();

    }
    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}
