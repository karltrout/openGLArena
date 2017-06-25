package guis;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import tools.Maths;

import java.util.List;

/**
 * Created by karltrout on 6/21/17.
 */
public class GuiRenderer {
    private final RawModel quad;
    private GuiShader guiShader;

    public GuiRenderer(Loader loader) {

        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1 };
        quad = loader.loadToVAO(positions, 2);
        guiShader = new GuiShader();

    }

    public void render (List<GuiTexture> guis){

        guiShader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        for (GuiTexture gui : guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            guiShader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }


        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        guiShader.stop();
    }


    public void cleanUp(){
        guiShader.cleanUp();
    }


}
