package models;

import textures.ModelTexture;

/**
 * Created by karltrout on 6/7/17.
 */
public class TexturedModel {

    RawModel model;
    private ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture) {

        this.model = model;
        this.texture = texture;

    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
