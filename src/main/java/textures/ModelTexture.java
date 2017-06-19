package textures;

/**
 * Created by karltrout on 6/7/17.
 */
public class ModelTexture {

    private int textureID;
    private float shineDamper = 1.0f;
    private float reflectivity = 0;

    public float getShineDampner() {
        return shineDamper;
    }

    public void setShineDampner(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public ModelTexture(int id){
        this.textureID =id;
    }
    public int getTextureID(){
        return this.textureID;
    }
}
