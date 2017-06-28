package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by karltrout on 6/8/17.
 */
public class Camera {

    private Entity player;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 45;

    private  float pitch;
    private  float yaw;
    private float roll;

    private Vector3f position= new Vector3f(0,0,0);

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch(){
        if(Mouse.isButtonDown(1)){
            float pitchChange = Mouse.getDY() *0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPLayer(){
        if(Mouse.isButtonDown(0)){
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX =(float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ =(float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x -offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    public Camera( Entity player ){
        this.player = player;
        this.pitch = 15.0f;
    }


    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPLayer();

        float horizontalDistance= calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

}
