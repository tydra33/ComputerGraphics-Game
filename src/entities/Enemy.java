package entities;

import engine.Loader;
import engine.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import textures.ModelTexture;

public class Enemy extends Entity {
    private static final Loader loader = new Loader();
    private static final RawModel RAW_MODEL = OBJLoader.loadObjModel("ufo", loader);
    private static final ModelTexture TEXTURE = new ModelTexture(loader.loadTexture("ufo"));
    private static final TexturedModel MODEL = new TexturedModel(RAW_MODEL, TEXTURE);

    private float HEALTH_POINTS = 250;
    private boolean turn1 = true;
    private boolean turn2 = true;
    private boolean firstDeath = true;

    public Enemy(Vector3f position, float rx, float ry, float rz, float scale) {
        super(MODEL, position, rx, ry, rz, scale);
    }

    public void takeDamage(float damage) {
        HEALTH_POINTS -= damage;
    }

    public boolean walk() {
        boolean end = true;

        if (super.getPosition().z > -10) {
            super.changePos(0, 0, -0.005f);
            end = false;
        }
        else if (super.getPosition().x <= 15) {
            if (turn1) {
                super.changeRotation(0, -90, 0);
                turn1 = false;
            }
            super.changePos(0.005f, 0, 0);
            end = false;
        }
        else if (super.getPosition().z >= -25) {
            if (turn2) {
                super.changeRotation(0, 90, 0);
                turn2 = false;
            }
            super.changePos(0, 0, -0.005f);
            end = false;
        }

        return end;
    }

    public float getHealthPoints(){
        if (HEALTH_POINTS <= 0 && firstDeath) {
            dieSound();
            firstDeath = false;
        }
        return HEALTH_POINTS;
    }

    private void dieSound() {
        new Thread(new Runnable() {
            public void run() {
                loader.playSound("res\\explosion.wav");
            }
        }).start();
    }

    public float getHEALTH_POINTS() {
        return HEALTH_POINTS;
    }

    public void setHEALTH_POINTS(float HEALTH_POINTS) {
        this.HEALTH_POINTS = HEALTH_POINTS;
    }
}
