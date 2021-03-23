package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.List;

public abstract class Tower extends Entity{
    private List<Enemy> enemies = new ArrayList<>();

    public Tower(TexturedModel model, Vector3f position, float rx, float ry, float rz, float scale) {
        super(model, position, rx, ry, rz, scale);
    }

    public void getNearbyEnemies(List<Enemy> allEnemies) {
        filterEnemies();

        for (Enemy enemy : allEnemies) {
            if (Math.abs(enemy.getPosition().x - super.getPosition().x) <= 1.5 &&
                    Math.abs(enemy.getPosition().z - super.getPosition().z) <= 1.5) {
                this.enemies.add(enemy);
            }
        }
    }

    public void filterEnemies() {
        enemies.removeIf(enemy -> !(Math.abs(enemy.getPosition().x - super.getPosition().x) <= 1.5 &&
                Math.abs(enemy.getPosition().z - super.getPosition().z) <= 1.5));
        enemies.removeIf(enemy -> enemy.getHealthPoints() <= 0);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public abstract void dealDamage();

    public abstract boolean isReady();
}
