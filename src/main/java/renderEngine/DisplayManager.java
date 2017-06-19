package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Created by karltrout on 6/6/17.
 */


public class DisplayManager {

    private static final int WIDTH = 1028;
    private static final int HEIGHT = 720;
    private static final int FPS = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(){

        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);

        try {

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Simulator");

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0,WIDTH, HEIGHT);

        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay(){

        Display.sync(FPS);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = ( currentFrameTime - lastFrameTime ) / 1000f;
        lastFrameTime = currentFrameTime;

    }

    public static float getFrameTimeSeconds(){ return delta; }

    public static void closeDisplay(){
        Display.destroy();
    }

    private static long getCurrentTime(){
        return Sys.getTime() * 1000/Sys.getTimerResolution();
    }

}
