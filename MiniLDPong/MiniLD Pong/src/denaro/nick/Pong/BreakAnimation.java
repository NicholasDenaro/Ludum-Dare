package denaro.nick.Pong;

import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class BreakAnimation extends Entity
{

	public BreakAnimation(double x,double y)
	{
		super(Sprite.sprite("Break Animation"),x,y);
		time=15;
	}
	
	@Override
	public void tick()
	{
		if(time--<0)
		{
			GameEngine.instance().removeEntity(this,null);
		}
		this.imageIndex(3-time*4/15);
	}
	
	private int time;
}
