package pl.evelanblog.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import pl.evelanblog.dynamicobjects.Asteroid;
import pl.evelanblog.dynamicobjects.DynamicObject;
import pl.evelanblog.dynamicobjects.Player;
import pl.evelanblog.enemy.Battleship;
import pl.evelanblog.enemy.Bomber;
import pl.evelanblog.enemy.EnemyBoss;
import pl.evelanblog.enemy.Fighter;
import pl.evelanblog.enums.GameState;
import pl.evelanblog.paxcosmica.Collider;
import pl.evelanblog.paxcosmica.Stats;
import pl.evelanblog.utilities.GameManager;
import pl.evelanblog.utilities.HUDController;

public class World {

	private static Group objects; // wszystkie obiekty na scenie (bez gracza)
	private static Player player; // gracz 1
	private static EnemyBoss enemyBoss; // boss
	private static GameState state; // stany gry
	private float[] sleepTime = new float[10]; // tablica gdzie trzymam czasy poszczególnych rzeczy kiedy mają się asteroidy etc.
	private static boolean enemyBossExists;
	private Collider collider; // zderzacz hadronów

	public World() {
		objects = new Group();
		clear();
	}

	public void clear() {
		player = new Player();
		enemyBoss = new EnemyBoss();
		collider = new Collider(player);
		objects.clear();
		Stats.levelKills = 0;
		for (int i = 0; i < sleepTime.length; i++) //zerujemy tablicę z czasami
			sleepTime[i] = 0;
		setState(GameState.ongoing); // już wszystko zostało ustawione wiec możemy startować z grą
		enemyBossExists = false;
	}

	public void bossFight() {
		if (Stats.levelKills > GameManager.levelKills && !enemyBossExists) {
			//TODO zmiana muzyki na jakąś poważniejszą
			objects.addActor(enemyBoss);
			enemyBossExists = true;
			Gdx.app.log("STATE", "Dodano do sceny EnemyBoss!");
		}

		//jeśli zabijemy bossa wygrywamy można jeszcze dodać jakieś 3 sekund odstępu zanim pokaże się ekran że wygraliśmy
		if (!enemyBoss.isAlive()) {
			state = GameState.win;
			Stats.kills += Stats.levelKills;
			Gdx.app.log("STATE", "EnemyBoss nie żyje " + state);
		}
	}

	public void update(float delta) {

		for (int i = 0; i < sleepTime.length; i++)
			sleepTime[i] += delta;

		bossFight();
		spawnObjects(); // spawning objects like asteroids
		updateObjects(delta); // update all objects

		if (player.isAlive()) {
			collider.checkPlayerCollision(objects);
			collider.checkBulletCollision(objects);
		} else {
			Stats.kills += Stats.levelKills;
			state = GameState.defeat;
			Gdx.app.log("STATE", "Gracz zginął, " + state);
		}
	}

	// w tej funkcji także spawnują się strzały wrogów!
	private void updateObjects(float delta) { // aktualizacja wszystkich obiektów, pozycji itd
		player.update(delta); // update position

		for (Actor obj : objects.getChildren())
			((DynamicObject) obj).update(delta);

		if (sleepTime[5] > 10f) {
			Gdx.app.log("COUNTER", "Liczba obiektów na scenie: " + objects.getChildren().size);
			sleepTime[5] = 0;
		}
	}

	private void spawnObjects() { // spownoawnie obiektów, oprócz pocisków wrogów, to się dzieje w update!!!!!!!!!!
		// TODO tutaj trzeba te ify jakoś skrócić za dużo podobnego kodu
		if (sleepTime[0] > player.getShootFrequency() && player.ableToShoot() && HUDController.getHit()) {
			objects.addActor(player.shoot());
			sleepTime[0] = 0;
		}

		if (sleepTime[1] > Asteroid.SPAWN_TIME) {
			objects.addActor(new Asteroid());
			sleepTime[1] = 0;
		}

		if (sleepTime[2] > Fighter.SPAWN_TIME) {
			objects.addActor(new Fighter());
			sleepTime[2] = 0;
		}

		if (sleepTime[3] > Bomber.SPAWN_TIME) {
			objects.addActor(new Bomber());
			sleepTime[3] = 0;
		}

		if (sleepTime[4] > Battleship.SPAWN_TIME) {
			objects.addActor(new Battleship());
			sleepTime[4] = 0;
		}
	}

	public static EnemyBoss getBoss() {
		return enemyBoss;
	}

	public static boolean isBossFight() {
		return enemyBossExists;
	}

	public static Player getPlayer() {
		return player;
	}

	public static Group getObjects() {
		return objects;
	}

	public static GameState getState() {
		return state;
	}

	public static void setState(GameState state) {
		World.state = state;
	}
}
