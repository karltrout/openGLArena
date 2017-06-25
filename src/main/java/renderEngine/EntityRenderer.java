package renderEngine;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import tools.Maths;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by karltrout on 6/6/17.
 */
public class EntityRenderer {

    private static StaticShader shader;
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {

        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();

    }

    public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace){

        shader.loadShadowMapSpace(toShadowSpace);
        for (TexturedModel model : entities.keySet()
             ) {
            prepareTextureModel(model);
            List<Entity> batch = entities.get(model);
            for ( Entity entity : batch ) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);

            }
            unbindTextureModel();
            
        }

    }

    private void prepareTextureModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        shader.loadShineAndReflectivity(texture.getShineDampner(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());


    }

    private void unbindTextureModel(){

        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

    }

    private void prepareInstance(Entity entity){

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotX(),
                entity.getRotY(),
                entity.getRotZ(),
                entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);

    }

}
