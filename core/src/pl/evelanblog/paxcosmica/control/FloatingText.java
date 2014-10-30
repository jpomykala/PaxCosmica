package pl.evelanblog.paxcosmica.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class FloatingText extends BitmapFont {
	
	private Vector2 position;
	private final static float LIFE_TIME = 10f;
	
	public FloatingText(float x, float y)
	{
		super(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
		position = new Vector2(x, y);
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	

}
