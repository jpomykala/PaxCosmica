package pl.evelanblog.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import pl.evelanblog.dynamicobjects.DynamicObject;
import pl.evelanblog.paxcosmica.Assets;
import pl.evelanblog.paxcosmica.Stats;

public abstract class Enemy extends DynamicObject {

	protected float time = 0; // zmienna pomocnicza
	protected ParticleEffect engine; // efekt cząsteczkowy silnika
	protected float bulletSpeed; // prędkość pocisku
	protected float shootTime; // częstotliwość strzelania
	protected float startY; // "poziom zero" w sinusoidzie, na jakiej wysokości y ma latać
	protected float radians = 0; // radiany żeby latać po sinusoidzie :)
	protected float radius = 0; // jak mocno będzie się wychylać statek w pionie, to jest ustawiane w klasach które
	// dziedziczą po Enemy

	protected Enemy(float speed, float hp, float shield, float bulletSpeed, float shootTime, float impactDamage, String texture) {
		super(Assets.worldWidth, 0, speed, hp, shield, impactDamage, texture);
		engine = new ParticleEffect();
		engine.load(Gdx.files.internal("data/enemyEngine.p"), Gdx.files.internal(""));
		this.bulletSpeed = bulletSpeed;
		this.shootTime = shootTime;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime); // usuwanie obiektu jak jest poza sceną
		radians += deltaTime;
		setX(getX() - speed * deltaTime);
		setY((MathUtils.sin(radians) * radius) + startY);
		engine.setPosition(getX() + getWidth() - 20, getY() + (getHeight() / 2));

		if ((time += deltaTime) > shootTime)
			shoot();
	}

	@Override
	public void draw(Batch batch, float alpha) {
		engine.draw(batch, Gdx.graphics.getDeltaTime()); // najpierw rysujemey silnik
		super.draw(batch, alpha); // a potem zasłąniamy jego część texturą statku
	}

	/**
	 * każdy rodzaj wroga może strzelać inaczej, np dawać dwa pociski w jednym czasie albo inny dźwięk pocisku czy coś
	 */
	public abstract void shoot();

	@Override
	public void kill() {
		dispose();
		Stats.levelKills++;
		Assets.explosionEffect.setPosition(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
		Assets.playSound(Assets.explosionSfx);
		Assets.explosionEffect.start();
	}
}
