package particles;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by karltrout on 6/23/17.
 */
public class ParticleMaster {

    private static List<Particle> particles = new ArrayList<>();
    private static ParticleRenderer renderer;

    public static void init(Loader loader, Matrix4f projectionMatrix){

        renderer = new ParticleRenderer(loader, projectionMatrix);

    }

    public static void update(){
        Iterator<Particle> interator = particles.iterator();
        while(interator.hasNext()){
            Particle particle = interator.next();
            boolean isAlive = particle.update();
            if(!isAlive){
                interator.remove();
            }
        }

    }

    public static void renderParticles(Camera camera){

        renderer.render(particles, camera);

    }

    public static void cleanUp (){
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle ){
        particles.add(particle);
    }

}
