package particles;

public class ParticleTexture {
    private int textureID;
    private int rowNum;

    public ParticleTexture(int textureID, int rowNum) {
        this.textureID = textureID;
        this.rowNum = rowNum;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getRowNum() {
        return rowNum;
    }
}
