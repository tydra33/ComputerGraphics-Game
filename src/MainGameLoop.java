import java.io.File;
import java.util.*;

import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextTracker;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.DisplayManager;
import engine.Loader;
import engine.MasterRenderer;
import engine.OBJLoader;
import particles.ParticleTracker;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.MousePicker;

public class MainGameLoop {
	private static boolean isOnTile(float x, float z) {
		boolean answer = false;

		if ((z >= -9.45 && z <= 0) && (x >= 4.45f && x <= 5.45)) {
			answer = true;
		}
		else if ((z >= -10.5f && z <= -9.5f) && (x >= 5.35f && x <= 15.35f)) {
			answer = true;
		}
		else if ((z >= -25.5f && z <= -9.5f) && (x >= 14.5f && x <= 15.5f)) {
			answer = true;
		}
		else if ((x >= 19 && x <= 22) && (z >= -10 && z <= -1.5f)) {
			answer = true;
		}
		else if ((x >= 4.5f && x <= 5.5f) && (z >= -10.5f && z <= -9.5f)) {
			answer = true;
		}

		return answer;
	}

	private static float getAngle(float x, float z) {
		float angle = 0;

		if ((z >= -10 && z <= 0) && (x <= 4.45f)) {
			angle = 90;
		}
		else if ((z >= -10 && z <= 0) && (x >= 5.45)) {
			angle = -90;
		}

		else if ((z >= -10.5f && z <= -10) && (x <= 5.35f)) {
			angle = 0;
		}
		else if ((z >= -10.5f && z <= -10) && (x <= 15)) {
			angle = 180;
		}
		else if ((z >= -25.7f && z <= -9.7f) && (x >= 14 && x <= 15.5f)) {
			angle = -90;
		}

		return angle;
	}

	private static boolean overlapsTower(List<Tower> towers, float x, float z) {
		if (towers.size() == 0) {
			return false;
		}

		for (Tower tower : towers) {
			if (Math.abs(x - tower.getPosition().x) < 0.6f && Math.abs(z - tower.getPosition().z) < 0.6f) {
				return true;
			}
		}

		return false;
	}

	private static boolean canBuildTower(MousePicker picker, List<Tower> towers) {
		return (picker.getCurrentTerrainPoint() != null &&
				picker.getCurrentTerrainPoint().x >= 1f &&
				picker.getCurrentTerrainPoint().x <= 25f &&
				picker.getCurrentTerrainPoint().z >= -25f &&
				picker.getCurrentTerrainPoint().z <= -1f &&
				!isOnTile(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z) &&
				!overlapsTower(towers, picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
	}

	public static void main(String[] args) {
		try {
			mainMenu();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private static void mainMenu() {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TextTracker.init(loader);
		FontType font = new FontType(loader.loadTexture("tahoma"), new File("res\\tahoma.fnt"));

		GUIText textTitle = new GUIText("Wardens of Steel", 4.5f, font, new Vector2f(0, 0), 1, true);
		textTitle.setColour(1, 0, 1);
		GUIText textStart = new GUIText("Start Game", 3, font, new Vector2f(0, 0.2f), 1, true);
		GUIText textControls = new GUIText("Controls", 3, font, new Vector2f(0, 0.4f), 1, true);
		GUIText textExit = new GUIText("Exit", 3, font, new Vector2f(0, 0.6f), 1, true);

		while (!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();

			textStart.remove();
			textControls.remove();
			textExit.remove();
			textTitle.remove();
			textTitle = new GUIText("Wardens of Steel", 4.5f, font, new Vector2f(0, 0), 1, true);
			textTitle.setColour(1, 0, 1);
			textStart = new GUIText("Start Game", 3, font, new Vector2f(0, 0.2f), 1, true);
			textControls = new GUIText("Controls", 3, font, new Vector2f(0f, 0.4f), 1, true);
			textExit = new GUIText("Exit", 3, font, new Vector2f(0, 0.6f), 1, true);

			//start game
			if (Mouse.isButtonDown(0)) {
				if ((Mouse.getX() >= 778 && Mouse.getX() <= 1123) && (Mouse.getY() >= 652 && Mouse.getY() <= 706)) {
					textStart.remove();
					textControls.remove();
					textExit.remove();
					textTitle.remove();
					TextTracker.cleanUp();

					loader.cleanUp();

					DisplayManager.discardDisplay();
					mainGame();
					break;
				}

				//exit game
				if ((Mouse.getX() >= 896 && Mouse.getX() <= 1009) && (Mouse.getY() >= 290 && Mouse.getY() <= 345)) {
					textStart.remove();
					textControls.remove();
					textExit.remove();
					textTitle.remove();
					TextTracker.cleanUp();

					loader.cleanUp();

					DisplayManager.discardDisplay();
					break;
				}

				//read game instructions
				if ((Mouse.getX() >= 829 && Mouse.getX() <= 1078) && (Mouse.getY() >= 473 && Mouse.getY() <= 526)) {
					textStart.remove();
					textControls.remove();
					textExit.remove();
					textTitle.remove();
					TextTracker.cleanUp();

					loader.cleanUp();

					DisplayManager.discardDisplay();
					instructionsMenu();
					break;
				}
			}

			TextTracker.render();
		}

		DisplayManager.discardDisplay();
	}

	private static void gameOver() {
		Loader loader = new Loader();

		TextTracker.init(loader);
		FontType font = new FontType(loader.loadTexture("tahoma"), new File("res\\tahoma.fnt"));

		GUIText textTitle = new GUIText("GAME OVER", 4f, font, new Vector2f(0, 0), 1, true);
		textTitle.setColour(1, 0, 0);

		while (!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();

			textTitle.remove();

			textTitle = new GUIText("GAME OVER", 4f, font, new Vector2f(0, 0), 1, true);
			textTitle.setColour(1, 0, 0);

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				textTitle.remove();

				TextTracker.cleanUp();

				loader.cleanUp();

				DisplayManager.discardDisplay();
				break;
			}

			TextTracker.render();
		}

		DisplayManager.discardDisplay();
	}

	private static void instructionsMenu() {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TextTracker.init(loader);
		FontType font = new FontType(loader.loadTexture("tahoma"), new File("res\\tahoma.fnt"));

		GUIText textTitle = new GUIText("Controls", 4f, font, new Vector2f(0, 0), 1, true);
		GUIText textWASD = new GUIText("Use W,A,S,D to move the camera forward, left, back, right", 1, font, new Vector2f(0, 0.2f), 1, false);
		GUIText textScroll = new GUIText("Use the scroll wheel to make the camera go up and down, use right click to change up/down angle(pitch) and click mouse scroll to change left/right angle(yaw)", 1, font,
				new Vector2f(0, 0.3f), 1, false);
		GUIText textBuildTower = new GUIText("To build a tower, you must press either 1 or 2(to select type of tower) " +
				"and click on the map(to build it there)", 1, font, new Vector2f(0, 0.4f), 1, false);
		GUIText textLose = new GUIText("If an enemy reaches the end of their path, you lose. Every 3rd wave, the night falls, and more enemies appear(they also gain health)", 1,
				font, new Vector2f(0, 0.5f), 1, false);
		GUIText textMenu = new GUIText("Whilst in game or in the Instructions page, press Esc to enter main menu", 1.5f, font,
				new Vector2f(0, 0.6f), 1, false);

		while (!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();

			textWASD.remove();
			textTitle.remove();
			textScroll.remove();
			textBuildTower.remove();
			textLose.remove();
			textMenu.remove();

			textTitle = new GUIText("Controls", 4.5f, font, new Vector2f(0, 0), 1, true);
			textTitle.setColour(1, 0, 1);
			textWASD = new GUIText("Use WASD to move forward, left, back, right", 1.5f, font, new Vector2f(0, 0.2f), 1, false);
			textScroll = new GUIText("Use the scroll wheel to make the camera go up and down, use right click to change up/down angle(pitch) and click mouse scroll to change left/right angle(yaw)",
					1.5f, font, new Vector2f(0, 0.3f), 1, false);
			textBuildTower = new GUIText("To build a tower, you must press either 1 or 2(to select type of tower) " +
					"and click on the map(to build it there)", 1.5f, font, new Vector2f(0, 0.4f), 1, false);
			textLose = new GUIText("If an enemy reaches the end of their path, you lose. Every 3rd wave, the night falls, and more enemies appear(they also gain health)", 1.5f,
					font, new Vector2f(0, 0.5f), 1, false);
			textMenu = new GUIText("Whilst in game or in the game over screen, press Esc to terminate the program. If you want to return to the menu NOW, press esc", 1.5f, font,
					new Vector2f(0, 0.6f), 1, false);

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				textWASD.remove();
				textTitle.remove();
				textScroll.remove();
				textBuildTower.remove();
				textLose.remove();
				textMenu.remove();
				textTitle.remove();
				TextTracker.cleanUp();

				loader.cleanUp();

				DisplayManager.discardDisplay();
				mainMenu();
				break;
			}

			TextTracker.render();
		}

		DisplayManager.discardDisplay();
	}

	private static void mainGame() {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TextTracker.init(loader);
		FontType font = new FontType(loader.loadTexture("tahoma"), new File("res\\tahoma.fnt"));
		
		RawModel modelTree = OBJLoader.loadObjModel("tree", loader);
		RawModel modelStone = OBJLoader.loadObjModel("stone", loader);
		RawModel modelGrass = OBJLoader.loadObjModel("bar", loader);
		RawModel modelPath = OBJLoader.loadObjModel("pathTile", loader);
		RawModel modelTown = OBJLoader.loadObjModel("town", loader);

		TexturedModel staticModelTree = new TexturedModel(modelTree,new ModelTexture(loader.loadTexture("tree")));
		TexturedModel staticModelStone = new TexturedModel(modelStone,new ModelTexture(loader.loadTexture("stone")));
		TexturedModel staticModelGrass = new TexturedModel(modelGrass,new ModelTexture(loader.loadTexture("bar")));
		TexturedModel staticModelPath = new TexturedModel(modelPath,new ModelTexture(loader.loadTexture("pathTile")));
		TexturedModel staticModelTown = new TexturedModel(modelTown,new ModelTexture(loader.loadTexture("town")));

		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")), "heightmapTest");

		List<Entity> trees = new ArrayList<>();
		List<Entity> rocks = new ArrayList<>();
		List<Entity> grass = new ArrayList<>();
		List<Tower> towers = new ArrayList<>();
		List<Entity> tiles = new ArrayList<>();
		Entity town = new Entity(staticModelTown, new Vector3f(20, 0.01f, -8), 5, 180, 0, 0.5f);

		long last =  System.currentTimeMillis();
		long lastWave = System.currentTimeMillis();
		int towerNumber = 1;
		Random random = new Random();

		boolean first3 = true;
		float healthVal = 250;
		int killCount = 0;
		int waveNum = 1;
		int waveCount = 0;
		int waveMax = 7;
		boolean startWave = true;
		List<Enemy> enemies = new ArrayList<>();

		boolean buildPath = true;
		float lastPosZ = -0.5f;
		float lastPosX = 5;
		while (lastPosZ > - 10) {
			tiles.add(new Entity(staticModelPath, new Vector3f(lastPosX, 0.03f, lastPosZ), 0, 0, 0, 0.5f));
			lastPosZ -= 1;
		}
		lastPosZ += 0.5f;
		while (lastPosX < 16) {
			tiles.add(new Entity(staticModelPath, new Vector3f(lastPosX, 0.03f, lastPosZ), 0, 0, 0, 0.5f));
			lastPosX += 1;
		}
		lastPosX -= 1;
		while (lastPosZ > - 25) {
			tiles.add(new Entity(staticModelPath, new Vector3f(lastPosX, 0.03f, lastPosZ), 0, 0, 0, 0.5f));
			lastPosZ -= 1;
		}

		for (int i = 0; i < 30; i++) {
			float randX = 0 + random.nextFloat() * (24.5f - 0);
			float randZ = -24.5f + random.nextFloat() * (0 - (-24.5f));

			while (isOnTile(randX, randZ)) {
				randX = 0 + random.nextFloat() * (24.5f - 0);
				randZ = -24.5f + random.nextFloat() * (0 - (-24.5f));
			}

			float randScale = 0.3f + random.nextFloat() * (0.7f - 0.3f);
			trees.add(new Entity(staticModelTree, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, randScale));
		}
		for (int i = 0; i < 15; i++) {
			float randX = 0 + random.nextFloat() * (24.5f - 0);
			float randZ = -24.5f + random.nextFloat() * (0 - (-24.5f));

			while (isOnTile(randX, randZ)) {
				randX = 0 + random.nextFloat() * (24.5f - 0);
				randZ = -24.5f + random.nextFloat() * (0 - (-24.5f));
			}

			float randScale = 0.1f + random.nextFloat() * (0.3f - 0.1f);
			rocks.add(new Entity(staticModelStone, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, randScale));
		}

		//add grass
		for (int i = 0; i < 3; i++) {
			float randX = 6.30f + random.nextFloat() * (9.60f - 6.30f);
			float randZ = -8.50f + random.nextFloat() * (-2f - (-8.50f));

			grass.add(new Entity(staticModelGrass, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, 0.2f));
		}
		for (int i = 0; i < 3; i++) {
			float randX = 9.91f + random.nextFloat() * (13.91f - 9.91f);
			float randZ = -24f + random.nextFloat() * (-11.67f - (-24f));

			grass.add(new Entity(staticModelGrass, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, 0.2f));
		}
		for (int i = 0; i < 3; i++) {
			float randX = 3f + random.nextFloat() * (4f - 3f);
			float randZ = -10.72f + random.nextFloat() * (-1.49f - (-10.72f));

			grass.add(new Entity(staticModelGrass, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, 0.2f));
		}
		for (int i = 0; i < 3; i++) {
			float randX = 2.2f + random.nextFloat() * (7.84f - 2.2f);
			float randZ = -16.80f + random.nextFloat() * (-11.00f - (-16.80f));

			grass.add(new Entity(staticModelGrass, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, 0.2f));
		}
		for (int i = 0; i < 3; i++) {
			float randX = 20f + random.nextFloat() * (24.72f - 20f);
			float randZ = -13.74f + random.nextFloat() * (-5.86f - (-13.74f));

			grass.add(new Entity(staticModelGrass, new Vector3f(randX, terrain.getHeightOfTerrain(randX, randZ), randZ), 0, 0, 0, 0.2f));
		}

		Light light = new Light(new Vector3f(10,10,-10),new Vector3f(1,1,1)); //make y = 0 for night
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		ParticleTracker.init(loader, renderer.getProjectionMatrix());

		GUIText text = new GUIText("Enemies killed: " + killCount, 2, font, new Vector2f(0, 0), 0.5f, false);

		while(!Display.isCloseRequested()){
			camera.move();
			picker.update();
			ParticleTracker.update();

			text.remove();
			text = new GUIText("Enemies killed: " + killCount, 2, font, new Vector2f(0, 0), 0.5f, false);

			if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
				towerNumber = 1;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
				towerNumber = 2;
			}

			if (Mouse.isButtonDown(0) && (System.currentTimeMillis() - last > 1000)) {
				if (canBuildTower(picker, towers)) {
					System.out.println(picker.getCurrentTerrainPoint());

					float y = terrain.getHeightOfTerrain(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z);
					if (towerNumber == 1) {
						Tower tower = new Tower1(new Vector3f(picker.getCurrentTerrainPoint().x, y - 0.2f, picker.getCurrentTerrainPoint().z),
								0, getAngle(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z), 0, 0.6f);
						towers.add(tower);
					}
					else if (towerNumber == 2) {
						Tower tower = new Tower2(new Vector3f(picker.getCurrentTerrainPoint().x, y - 0.2f, picker.getCurrentTerrainPoint().z),
								0, getAngle(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z), 0, 0.5f);
						towers.add(tower);
					}

					last = System.currentTimeMillis();
				}
			}

			for (Entity tile : tiles) {
				if (!buildPath) {
					break;
				}
				if (tile.getPosition().x == 15 && tile.getPosition().z == -25) {
					buildPath = false;
					break;
				}

				renderer.processEntity(tile);
			}
			for (Entity tree : trees) {
				renderer.processEntity(tree);
			}
			for (Entity rock : rocks) {
				renderer.processEntity(rock);
			}
			for (Entity bar : grass) {
				renderer.processEntity(bar);
			}
			renderer.processEntity(town);
			for (Tower tower : towers) {
				renderer.processEntity(tower);

				if (tower.isReady()) {
					tower.getNearbyEnemies(enemies);
					tower.dealDamage();

					int len1 = enemies.size();
					enemies.removeIf(enemy -> enemy.getHealthPoints() <= 0);
					int len2 = enemies.size();
					killCount += len1 - len2;
				}
			}
			if (!startWave && System.currentTimeMillis() - lastWave > 10000) {
				startWave = true;
			}
			if (System.currentTimeMillis() - lastWave > 1000 && startWave) {
				Enemy enemy = new Enemy(new Vector3f(5, 0.4f, -0.5f), 0, 180, 0, 0.05f);
				enemy.setHEALTH_POINTS(healthVal);
				enemies.add(enemy);
				lastWave = System.currentTimeMillis();

				waveCount++;
				if (waveCount == waveMax) {
					startWave = false;
					waveCount = 0;
					waveNum++;
					first3 = true;
				}

				if (waveNum % 3 == 0 && first3) {
					light.setColour(new Vector3f(1, 0, 1));
					waveMax += 1;
					healthVal += 350;
					first3 = false;
				}
				else {
					light.setColour(new Vector3f(1, 1, 1));
				}
			}
			for (Enemy enemy : enemies) {
				renderer.processEntity(enemy);
				if (enemy.walk()) {
					TextTracker.cleanUp();
					renderer.cleanUp();
					ParticleTracker.cleanUp();
					loader.cleanUp();
					gameOver();
					return;
				}
			}
			int len1 = enemies.size();
			enemies.removeIf(enemy -> enemy.getHealthPoints() <= 0);
			int len2 = enemies.size();
			killCount += len1 - len2;

			renderer.processTerrain(terrain);
			renderer.render(light, camera);

			ParticleTracker.renderParticles(camera);
			TextTracker.render();

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				text.remove();
				TextTracker.cleanUp();
				ParticleTracker.cleanUp();
				renderer.cleanUp();
				loader.cleanUp();
				DisplayManager.discardDisplay();
				break;
			}

			DisplayManager.updateDisplay();
		}

		/*
		TextTracker.cleanUp();
		ParticleTracker.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.discardDisplay();
		 */
	}
}
