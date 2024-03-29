package pl.evelanblog.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.evelanblog.GUI.Button;
import pl.evelanblog.paxcosmica.Assets;
import pl.evelanblog.paxcosmica.PaxCosmica;
import pl.evelanblog.paxcosmica.Stats;
import pl.evelanblog.utilities.GameManager;
import pl.evelanblog.world.World;

public class UpgradeScreen extends Stage implements Screen, InputProcessor {

	private final PaxCosmica game;
	private BitmapFont font;
	private Sprite background;
	private Button apply, discard, upgrade;
	private Rectangle mousePointer;

	private float power, hull, shield, weapon, engine;
	private int powerLvl, hullLvl, shieldLvl, weaponLvl, engineLvl;
	private float hover = -1;
	private int cost = 5;
	private int scrap;

	public UpgradeScreen(final PaxCosmica game) {
		super(new StretchViewport(1920, 1080));
		this.game = game;

		background = new Sprite(Assets.upgradeBackground);
		upgrade = new Button(Assets.upgradeBtn, 200, 50);
		apply = new Button(1520, 116, 320, 96, Assets.applyButton);
		discard = new Button(1520, 20, 320, 96, Assets.discardButton);
		addActor(apply);
		addActor(discard);
		addActor(upgrade);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		getBatch().begin();
		background.draw(getBatch());
		createBar(power, powerLvl, "Power: " + powerLvl);
		createBar(hull, hullLvl, "Hull: " + hullLvl);
		createBar(shield, shieldLvl, "Shield: " + shieldLvl);
		createBar(weapon, weaponLvl, "Weapon: " + weaponLvl);
		createBar(engine, engineLvl, "Engine: " + engineLvl);
		font.draw(getBatch(), "Scrap: " + scrap, 10, Gdx.graphics.getHeight() - font.getLineHeight());
		getBatch().end();
		draw();

	}

	public void createBar(float x, float level, String name) {
		for (int i = 0; i < level; i++)
			getBatch().draw(Assets.upgradeBar, x, 200 + i * 30);

		font.draw(getBatch(), "Cost: " + cost, x, level * 30 + 250);
		font.draw(getBatch(), name, x + 10, 190);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		// pozycje X, tych pasków/stanów/poziomów ulepszeń
		float temp_pos = ((Assets.worldWidth + 100) / 5) - Assets.upgradeBar.getWidth(); // TODO usunąć to potem
		power = temp_pos;
		hull = temp_pos * 2;
		shield = temp_pos * 3;
		weapon = temp_pos * 4;
		engine = temp_pos * 5;

		powerLvl = World.getPlayer().powerLvl;
		hullLvl = World.getPlayer().hullLvl;
		shieldLvl = World.getPlayer().shieldLvl;
		weaponLvl = World.getPlayer().weaponLvl;
		engineLvl = World.getPlayer().engineLvl;
		scrap = Stats.scrap;

		mousePointer = GameManager.getMouse();

		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;

		screenX = (int) (screenX * getViewport().getWorldWidth() / Gdx.graphics.getWidth());
		screenY = (int) (screenY * getViewport().getWorldHeight() / Gdx.graphics.getHeight());

		mousePointer.setPosition(screenX, screenY);

		Assets.playSound(Assets.clickSfx);

		if (apply.getBoundingRectangle().overlaps(mousePointer)) {

			World.getPlayer().powerLvl = powerLvl;
			World.getPlayer().hullLvl = hullLvl;
			World.getPlayer().engineLvl = engineLvl;
			World.getPlayer().weaponLvl = weaponLvl;
			World.getPlayer().shieldLvl = shieldLvl;
			World.getPlayer().setStats();
			Stats.scrap = scrap;
			game.setScreen(GameManager.galaxyMap);
			dispose();
		} else if (discard.getBoundingRectangle().overlaps(mousePointer)) {
			game.setScreen(GameManager.galaxyMap);
			dispose();
		} else if (upgrade.getBoundingRectangle().overlaps(mousePointer)) {
			if (scrap >= cost) {
				scrap -= cost;

				if (hover == power)
					powerLvl++;
				else if (hover == hull)
					hullLvl++;
				else if (hover == weapon)
					weaponLvl++;
				else if (shield == hover)
					shieldLvl++;
				else if (engine == hover)
					engineLvl++;
			}
		}

		if (screenX > power && screenX < power + 200)
			hover = power;
		else if (screenX > hull && screenX < hull + 200)
			hover = hull;
		else if (screenX > weapon && screenX < weapon + 200)
			hover = weapon;
		else if (screenX > shield && screenX < shield + 200)
			hover = shield;
		else if (screenX > engine && screenX < engine + 200)
			hover = engine;
		else
			hover = -1;

		if (hover != -1)
			upgrade.setPosition(hover, 100);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
