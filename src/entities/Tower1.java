package entities;

import engine.Loader;
import engine.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleEmitter;
import particles.ParticleTexture;
import textures.ModelTexture;

import java.util.Random;

public class Tower1 extends Tower {
    private static final Loader loader = new Loader();
    private static final RawModel RAW_MODEL = OBJLoader.loadObjModel("tower1", loader);
    private static final ModelTexture TEXTURE = new ModelTexture(loader.loadTexture("tower1"));
    private static final TexturedModel MODEL = new TexturedModel(RAW_MODEL, TEXTURE);

    private static final float MIN_DMG = 80;
    private static final float MAX_DMG = 115;
    private static final long COOL_DOWN = 1000; //in ms

    ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("fire"), 3);
    ParticleEmitter emitter = new ParticleEmitter(particleTexture, 5000, 5, 0.3f, 1, 0.1f);

    private long lastTime = System.currentTimeMillis();

    public Tower1(Vector3f position, float rx, float ry, float rz, float scale) {
        super(MODEL, position, rx, ry, rz, scale);
    }

    public void dealDamage() {
        super.filterEnemies();

        for (Enemy enemy : super.getEnemies()) {
            if (System.currentTimeMillis() - lastTime > COOL_DOWN) {
                explosion();

                Random random = new Random();
                float randDmg = MIN_DMG + random.nextFloat() * (MAX_DMG - MIN_DMG);

                enemy.takeDamage(randDmg);
                lastTime = System.currentTimeMillis();

                killExplosion();
            }
        }
    }

    public boolean isReady() {
        return (System.currentTimeMillis() - lastTime > COOL_DOWN);
    }

    private void explosion() {
        emitter.setAverageLifeLength(0.18f);
        emitter.setLifeError(0);
        emitter.setSpeedError(0.25f);
        emitter.setScaleError(0.5f);
        emitter.randomizeRotation();

        emitter.generateParticles(new Vector3f(this.getPosition().x + 0.3f, 0.7f, this.getPosition().z));
    }

    private void killExplosion() {
        emitter.setAverageLifeLength(0);
        emitter.setLifeError(0);
        emitter.setSpeedError(0);
        emitter.setScaleError(0);
    }
}
