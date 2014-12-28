package pl.evelanblog.dynamicobjects;

import pl.evelanblog.paxcosmica.Assets;
import pl.evelanblog.paxcosmica.PaxCosmica;
import pl.evelanblog.scenes.GalaxyMap;
import pl.evelanblog.scenes.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends DynamicObject {
	private float bulletSpeed = 1000f;
	private float shootFrequency = 0.2f;
	private Sprite shieldSprite;
	public float shieldReloadLvl = 0;
	private float temp_x, temp_y;
	
	public static float powerLvl = 4;
	public static float shieldLvl = 1;
	public static float hullLvl = 1;
	public static float weaponLvl = 1;
	public static float engineLvl = 1;

	public static float powerGenerator;
	public static float shieldPwr = 1;
	public static float hullPwr = 1;
	public static float weaponPwr = 1;
	public static float enginePwr = 1;

	public Player() {
		// pos x, pos y, speed , hp, shield, impactDamage, texture
		super(100, 300, 180f, 3f, 0f, 100f, "spaceship.png");
		shieldSprite = new Sprite(Assets.bubbleShield);
		shieldSprite.setOriginCenter();
	}

	/**
	 * @deprecated
	 * Konstruktor ktory ustawia maly statek na mapie galaktyki {@link GalaxyMap}
	 * @param x pozycja x
	 * @param y pozycja y
	 */
	public Player(float x, float y)
	{
		super(x, y, 0, 0, 0, 0, "spaceship.png");
		setScale(0.7f);
	}

	/** 
	 * @deprecated
	 * Ustawianie malego statku na mapie galaktyki {@link GalaxyMap}
	 * @param x pozycja x
	 * @param y pozycja y
	 */
	

	public void update(float deltaTime) {
		powerGenerator = powerLvl - shieldPwr - hullPwr - weaponPwr - enginePwr;

		if (enginePwr < 1)
			speed = 0;
		else
			speed = 180f + (enginePwr * 10);

		if (shield < shieldPwr)
		{
			shieldReloadLvl += deltaTime;
			if (shieldReloadLvl > 10f){
				shieldReloadLvl = 0;
				shield++;
			}
		}

		bulletSpeed = 600f + (weaponPwr * 80); //standardowo pocisk ma predkosc 600f, z kazdym kolejnym poziomem weaponPwr bedzie on przyspieszac razy 80

		temp_y = PaxCosmica.getGameScene().getCamera().position.y + (GameScreen.getVelY() * deltaTime * speed);
		if(temp_y > 0 && temp_y < Gdx.graphics.getHeight() - getSprite().getHeight())
		{
			PaxCosmica.getGameScene().getCamera().position.y=temp_y;
			GameScreen.getBackground().moveBy(0, PaxCosmica.getGameScene().getCamera().position.y*0.1f);
			getSprite().setY(PaxCosmica.getGameScene().getCamera().position.y);
		}
			
		
		temp_x = getSprite().getX() + (GameScreen.getVelX() * deltaTime * speed);
		if(temp_x > 0 && temp_x < Gdx.graphics.getWidth() - getSprite().getWidth())
			getSprite().setX(temp_x);
		
		Assets.playerEngineEffect.setPosition(getSprite().getX() + 10, getSprite().getY() + (getSprite().getHeight() / 2));
		shieldSprite.setPosition(getSprite().getX() - 30, getSprite().getY() - ((shieldSprite.getHeight() - getSprite().getHeight()) / 2));
	}

	/**
	 * Rysujemy gracza, efekt silnika oraz oslone jesli {@link DynamicObject#shield} jest wieksze od 0
	 * @param batch SpriteBatch, wiadomo
	 * @param delta be kwadrat minus cztery ace
	 */
	
	@Override
	public void draw(Batch batch, float alpha){
		if (enginePwr > 0)
			Assets.playerEngineEffect.draw(batch, Gdx.graphics.getDeltaTime());
		batch.draw(getSprite().getTexture(), getSprite().getX(), getSprite().getY());
		if (shield > 0)
			shieldSprite.draw(batch);
	}

	/** 
	 * Odtwarza dzwiek strzalu z Assets
	 * @return
	 */
	public Bullet shoot() {
		Assets.playSound(Assets.shootSfx);
		return new Bullet(getSprite().getX() + getSprite().getWidth() - 10, getSprite().getY() + (getSprite().getHeight() / 2) - 4, bulletSpeed, true, 1f);
	}

	public float getShootFrequency() {
		return shootFrequency;
	}
	
	/**
	 * Sprawdza czy gracz ma mozlowosc strzelania
	 * @param weaponPwr poziom energii  broni
	 * @return true jesli moze strzelic, w innym wypadku false
	 */
	public boolean ableToShoot()
	{
		return (weaponPwr > 0 && live == true);
	}
	
	@Override
	public void kill()
	{
		live = false;
		Assets.playSound(Assets.explosionSfx);
		Assets.explosionEffect.setPosition(getSprite().getX() + (getSprite().getWidth() / 2), getSprite().getY() + (getSprite().getHeight() / 2));
		Assets.explosionEffect.start();
	}
}