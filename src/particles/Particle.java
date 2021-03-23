package particles;

import engine.DisplayManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Particle {
    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private ParticleTexture texture;

    private static final float GRAVITY = -50;

    private Vector2f textureOffset1 = new Vector2f();
    private Vector2f textureOffset2 = new Vector2f();
    private float blendFactor;

    private float elapsedTime = 0;

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength,
                    float rotation, float scale) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;

        ParticleTracker.addParticle(this);
    }

    public float getBlendFactor() {
        return blendFactor;
    }

    public Vector2f getTextureOffset1() {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    protected Vector3f getPosition() {
        return position;
    }

    protected float getRotation() {
        return rotation;
    }

    protected float getScale() {
        return scale;
    }

    protected boolean update() {
        velocity.y += GRAVITY * gravityEffect * DisplayManager.getFrameTimeSecs();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSecs());
        Vector3f.add(change, position, position);
        updateTextureInfo();
        elapsedTime += DisplayManager.getFrameTimeSecs();
        return elapsedTime < lifeLength;
    }

    private void updateTextureInfo() {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getRowNum() * texture.getRowNum();
        float atlasProgression = lifeFactor * stageCount;

        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blendFactor = atlasProgression % 1;

        setOffset(textureOffset1, index1);
        setOffset(textureOffset2, index2);
    }

    private void setOffset(Vector2f offset, int index) {
        int col = index % texture.getRowNum();
        int row = index / texture.getRowNum();

        offset.x = (float) col / texture.getRowNum();
        offset.y = (float) row / texture.getRowNum();
    }
}
