package particles;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

import java.util.*;

/**
 * Created by karltrout on 6/23/17.
 */
public class ParticleMaster {

    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
    private static ParticleRenderer renderer;

    public static void init(Loader loader, Matrix4f projectionMatrix){

        renderer = new ParticleRenderer(loader, projectionMatrix);

    }

    public static void update(Camera camera){

        Iterator<Map.Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
        while (mapIterator.hasNext()){

            List<Particle> particleList = mapIterator.next().getValue();
            Iterator<Particle> interator = particleList.iterator();

            while(interator.hasNext()){

                Particle particle = interator.next();
                boolean isAlive = particle.update(camera);
                if(!isAlive){
                    interator.remove();
                    if (particleList.isEmpty()) mapIterator.remove();
                }

            }
            InsertionSort.sortHighToLow(particleList);

        }

    }

    public static void renderParticles(Camera camera){

        renderer.render(particles, camera);

    }

    public static void cleanUp (){
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle ){

        List<Particle> list = particles.get(particle.getTexture());
        if(list == null){

            list = new ArrayList<>();
            particles.put(particle.getTexture(), list);

        }

        list.add(particle);

    }

}
