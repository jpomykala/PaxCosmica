package pl.evelanblog.dynamicobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class DynamicObject extends Sprite {

	protected float hp;
	protected boolean live;
	protected float speed;
	protected float impactDamage; // im wyższa tym większe obrażenia będą zadawane przy uderzeniu
	protected float shield;

	protected DynamicObject(float x, float y, float speed, float hp, float shield, float impactDamage, String texture)
	{
		super(new Texture(Gdx.files.internal(texture)));
		setBounds(x, y, getTexture().getWidth(), getTexture().getHeight());
		setOriginCenter();
		live = true;
		this.speed = speed;
		this.impactDamage = impactDamage;
		this.shield = shield;
		this.hp = hp;
	}

	public void setTexture(String file)
	{
		setTexture(new Texture(Gdx.files.internal(file)));
		setSize(getTexture().getWidth(), getTexture().getHeight());
		setOriginCenter();
	}

	
	public float getImpactDamage()
	{
		return impactDamage;
	}

	public float getSpeed() {
		return speed;
	}

	public float getHealth() {
		return hp;
	}

	public float getShield()
	{
		return shield;
	}

	/**
	 * Uszkadza obiekt, jak HP spadnie poniżej 0 to umiera
	 * 
	 * @param damage
	 *            ilość zadawanych obrażeń
	 */
	public void hurt(float damage) {

		if (shield > 0) {
			shield -= damage;
			shield = 0;
		} else {
			hp -= damage;
		}

		if (hp <= 0)
			kill();
	}
	
	public abstract void kill();

	public boolean isAlive() {
		return live;
	}

	public void draw(SpriteBatch batch, float delta) {
		draw(batch);
	}

}
