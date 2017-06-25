package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrains.Terrain;
import terrains.TerrainTexturePack;
import textures.ModelTexture;
import tools.Maths;

import java.util.List;

/**
 * Created by karltrout on 6/14/17.
 */
public class TerrainRenderer {

    private TerrainShader terrainShader;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix) {

        this.terrainShader = terrainShader;
        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.connectTextureUnits();
        terrainShader.stop();
    }

    public void render ( List<Terrain> terrains, Matrix4f toShadowSpace){

        terrainShader.loadShadowMapSpace(toShadowSpace);
        for ( Terrain terrain : terrains ) {

            prepreTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
            unbindTextureModel();
        }
    }

    public void bindTextures(Terrain terrain){
        TerrainTexturePack texturePack = terrain.getTexturePAck();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTerrainID());

    }

    private void prepreTerrain(Terrain terrain){
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        bindTextures(terrain);
        terrainShader.loadShineAndReflectivity(50, 0.25f );

    }

    private void unbindTextureModel(){

        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

    }

    private void loadModelMatrix(Terrain terrain){

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0.0f, terrain.getZ()), 0, 0, 0, 1);

        terrainShader.loadTransformationMatrix(transformationMatrix);

    }
}
