package pl.evelanblog.dynamicobjects;

import pl.evelanblog.paxcosmica.Assets;

import com.badlogic.gdx.Gdx;

/**
 * Klasa pocisków wszelakich
 * @author Evelan
 *
 */

public class Bullet extends DynamicObject {
	private boolean direction; // true - strzały gracza, false - strzały wroga

	public Bullet(float x, float y, float speed, boolean direction, float damage) {
		super(x, y, speed, 1, 0, damage, "bullet.png");
		this.direction = direction;
	}

	public void update(float deltaTime) {
		if (direction) // w którą stronę latajo szczały
			setX(getX() + speed * deltaTime);
		else
			setX(getX() - speed * deltaTime);

		if (getX() < 0 || getX() > Gdx.graphics.getWidth()) // jak wyleci za ekran to umieramy szczała żeby się już nie renderował
			live = false;
	}

	/**
	 * zwraca kierunek w którą stronę leci pocisk, true - strzały gracza, false - strzały wroga
	 * 
	 * @return
	 */
	public boolean getDirection()
	{
		return direction;
	}

	@Override
	public void kill()
	{
		live = false;
		Assets.playSound(Assets.hitSfx);
		Assets.hitEffect.setPosition(getX(), getY() + (getHeight() / 2));
		Assets.hitEffect.start();
	}
}
