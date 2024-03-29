package pl.evelanblog.paxcosmica.control;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import pl.evelanblog.paxcosmica.Assets;
import pl.evelanblog.GUI.Button;
import pl.evelanblog.GUI.CheckBox;
import pl.evelanblog.paxcosmica.Planet;

public class MousePointer extends Rectangle {
	private static final long serialVersionUID = 3373269279037859745L;

	public MousePointer()
	{
		setPosition(0, 0);
		setSize(1);
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

    public boolean overlaps(CheckBox checkBox){
        if(checkBox.isVisible())
        {

            if (this.overlaps(new Rectangle(checkBox.getBoundingRectangle())))
                return true;
            else
                return false;
        }

        return false;
    }

	public boolean overlaps(float x, float y, Button button)
	{
		if(button.isVisible())
		{

			//y = Gdx.graphics.getHeight() - y;
			setPosition(x, y);

            if (this.overlaps(button.getBoundingRectangle()))
				return true;
			else
				return false;
		}
		return false;

	}

    public boolean overlaps(Circle circle) {

        Circle mouse = new Circle(this.getX(), this.getY(), 5);

            return mouse.overlaps(circle);
    }

	public boolean overlaps(Button button) {
		if(button.isVisible())
		{
			if (this.overlaps(button.getBoundingRectangle()))
				return true;
			else
				return false;
		}
		return false;
	}


	public boolean overlaps(Planet planet, float moveValue, float x, float y) {
		if(planet.isVisible())
		{
			setPosition(x+moveValue * Assets.worldWidth,y);
			if (this.overlaps(planet.getSprite().getBoundingRectangle()))
			{
				setPosition(x,y);
				return true;
			}
			else
			{
				setPosition(x,y);
				return false;
			}
		}
		return false;
	}

	public boolean overlaps(Button button, float moveValue, float x, float y) {
		if(button.isVisible())
		{
			setPosition(x+moveValue * Assets.worldWidth, y);
			if (this.overlaps(button.getBoundingRectangle()))
			{
				setPosition(x,y);
				return true;
			}
			else
			{
				setPosition(x,y);
				return false;
			}
		}
		return false;
	}
}
