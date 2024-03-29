package pl.evelanblog.enemy;

import com.badlogic.gdx.math.MathUtils;
import pl.evelanblog.dynamicobjects.Bullet;
import pl.evelanblog.dynamicobjects.Scrap;
import pl.evelanblog.paxcosmica.Assets;
import pl.evelanblog.paxcosmica.Stats;
import pl.evelanblog.world.World;

/**
 * Końcowy badass
 *
 * @author Evelan
 */
public class EnemyBoss extends Enemy {

	public EnemyBoss() {
		// (float speed, hp, shield, bulletSpeed, shootTime, impactDamage, SPAWN_TIME, String texture)
		super(10f, 50f, 0f, 400f, 3f, 400f, "enemy/boss.png");

		shootTime += 1f; // aby nie strzelały w takim samym odstępie czasu, małe urozmaicenie
		radius = 120;
		startY = MathUtils.random(0, Assets.worldHeight / 2);
	}

	@Override
	public void hurt(float damage) {
		hp -= damage;
		if (hp <= 0)
			kill();
	}

	@Override
	public void shoot() {
		World.getObjects().addActor(new Bullet(getX(), getY() + getHeight() - (getHeight() / 6), bulletSpeed, false, 2f));
		World.getObjects().addActor(new Bullet(getX(), getY() + getHeight() / 6, bulletSpeed, false, 2f));
		Assets.playSound(Assets.shootSfx);
		time = 0;
	}

	@Override public void kill() {
		super.kill();
		Stats.score += 500;
		World.getObjects().addActor(new Scrap(getX(), getY(), 50));
	}
}
