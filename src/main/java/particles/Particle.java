package particles;


import entities.Player;
import javafx.print.PageLayout;
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

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;

        ParticleMaster.addParticle(this);

    }

    protected boolean update(){
        this.velocity.y =+Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change, position,position);
        elapsedTime =+ DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
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