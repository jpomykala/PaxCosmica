package pl.evelanblog.dynamicobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import pl.evelanblog.world.World;

public abstract class DynamicObject extends Actor {

	protected float hp;
	protected float maxHp;
	protected float shield;
	protected float maxShield;
	protected boolean live;
	protected float speed;
	protected float impactDamage; // im wyższa tym większe obrażenia będą zadawane przy uderzeniu
	protected Sprite sprite; // brzmi legitymacjnie że actor nie ma sprite już w sobie

	public DynamicObject(float x, float y, float speed, float hp, float shield, float impactDamage, String file) {

		sprite = new Sprite(new Texture(Gdx.files.internal(file)));
		sprite.setBounds(x, y, sprite.getWidth(), sprite.getHeight());
		sprite.setOriginCenter();
		live = true;
		this.speed = speed;
		this.impactDamage = impactDamage;
		this.hp = hp;
		this.maxHp = hp;
		this.shield = shield;
		this.maxShield = shield;
	}

	public void setTexture(String file) {
		sprite.setTexture(new Texture(Gdx.files.internal(file)));
		setSize(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());
		sprite.setOriginCenter();
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(sprite, getX(), getY());
	}

	public float getImpactDamage() {
		return impactDamage;
	}

	public float getSpeed() {
		return speed;
	}

	public float getHealth() {
		return hp;
	}

	public float getShield() {
		return shield;
	}

	public float getMaxHealth() {
		return maxHp;
	}

	public float getMaxShield() {
		return maxShield;
	}

	/**
	 * Uszkadza obiekt, jak HP spadnie poniżej 0 to umiera
	 *
	 * @param damage ilość zadawanych obrażeń
	 */
	public void hurt(float damage) {
		if (shield > 0) {
			shield -= damage;
		} else {
			shield = 0;
			hp -= damage;
		}
		if (hp <= 0) {
			hp = 0;
			kill();
		}
	}

	public void update(float delta) {

		//jeśli osłona została zniszczona prawdopodobnie ma ujemną wartość dlatego ustawiamy ją na zero wtakim przypadku
		if (shield < 0)
			shield = 0;

		if (getX() + getWidth() < 0)
			dispose();
	}

	protected void dispose() // usuwanie obiektu który jest poza sceną
	{
		live = false;
		World.getObjects().removeActor(this);
	}

	public abstract void kill();

	public boolean overlaps(Actor actor) {
		Rectangle r = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		return this.getSprite().getBoundingRectangle().overlaps(r);
	}

	public boolean isAlive() {
		return live;
	}

	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public void setX(float foo) {
		sprite.setX(foo);
	}

	@Override
	public void setY(float foo) {
		sprite.setY(foo);
	}

	@Override
	public float getX() {
		return sprite.getX();
	}

	@Override
	public float getY() {
		return sprite.getY();
	}

	@Override
	public float getWidth() {
		return sprite.getWidth();
	}

	@Override
	public float getHeight() {
		return sprite.getHeight();
	}

}
