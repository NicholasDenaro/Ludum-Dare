package denaro.nick.Pong;

import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class Button extends Entity
{

	public Button(double x,double y)
	{
		super(Sprite.sprite("Button"),x,y);
		depth(-9);
	}

	@Override
	public void tick()
	{
		if(this.collision(x(),y(),GameEngine.instance().location().entityList(Player.class)))
		{
			if(!pressed&&!hold)
			{
				pressed=true;
				Main.ball.start();
				imageIndex(1);
				if(Main.time==Main.TIME)
				{
					Main.gameTimer();
					Main.startSpawnTimer();
				}
				hold=true;
			}
		}
		else if(!pressed)
		{
			imageIndex(0);
			hold=false;
		}
	}
	
	public void depress()
	{
		pressed=false;
		if(!hold)
			imageIndex(0);
	}
	
	private boolean pressed;
	private boolean hold;
}
