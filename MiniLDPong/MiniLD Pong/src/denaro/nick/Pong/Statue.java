package denaro.nick.Pong;

import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class Statue extends Entity
{

	public Statue(Sprite sprite,double x,double y, int points)
	{
		super(sprite,x,y);
		this.points=points;
		if(points>0)
			time=Main.TICKRATE*30;
		else
			time=1;
	}

	@Override
	public void tick()
	{
		if(points>0)
			time--;
		if(time==0)
		{
			destroy();
		}
	}
	
	public int points()
	{
		return(points);
	}
	
	public void destroy()
	{
		destroyed=true;
		GameEngine.instance().removeEntity(this,null);
	}
	
	public boolean isDestroyed()
	{
		return(destroyed);
	}
	
	private int points;
	private boolean destroyed;
	private int time;
}
