package denaro.nick.Pong;

import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;
import denaro.nick.core.view.GameView2D;
import denaro.nick.utilities.Pair;

public class Ball extends Entity
{
	private int startx;
	private int starty;
	
	public Ball(Sprite sprite,double x,double y)
	{
		super(sprite,x,y);
		startx=(int)x;
		starty=(int)y;
		velocity=new Pair<Double,Double>(0.0,0.0);
		lastHit=false;
		hitTimer=0;
		struck=false;
	}
	
	public void reset()
	{
		move(startx,starty);
		velocity=new Pair(0.0,0.0);
		struck=false;
	}
	
	public void start()
	{
		velocity=new Pair(1.5,1-Math.random()*2);
	}

	@Override
	public void tick()
	{
		if(Main.end)
			return;
		//Check Statue collision
		//if(struck)
		{
			for (Entity e:GameEngine.instance().location().entityList(Statue.class))
			{
				double dist=Math.sqrt((x()-e.x())*(x()-e.x())+(y()-e.y())*(y()-e.y()));
				if(this.collision(x(),y(),e))
				{
					Statue s=(Statue)e;
					s.destroy();
					int points=s.points();
					GameEngine.instance().addEntity(new BreakAnimation(s.x(),s.y()),null);
					if(points<0)
					{
						double r=Math.random();
						if(r<0.20)
						{
							Main.rightWall.grow();
							Main.leftWall.shrink();
						}
						else
						{
							if(velocity.first>0)
								velocity=new Pair(velocity.first*1.5,velocity.second);
							else
								velocity=new Pair(-velocity.first,velocity.second);
						}
					}
					else
					{
						double r=Math.random();
						if(r<0.1&&Main.lives<5)
						{
							Main.lives++;
						}
						else if(r<0.3)
						{
							Main.time+=Main.TICKRATE*20;
						}
						else if(r<0.35)
						{
							Main.rightWall.shrink();
							Main.rightWall.shrink();
							Main.rightWall.shrink();
							Main.rightWall.shrink();
						}
						else
						{
							Main.leftWall.grow();
						}
					}
				}
			}
		}
		
		//Check Sword collision
		if(hitTimer==0)
		{
			for (Entity e:GameEngine.instance().location().entityList(Sword.class))
			{
				if(this.collision(x(),y(),e))
				{
					Sword s=(Sword)e;
					double vel=vel();
					vel*=1.25;
					vel=Math.min(Math.max(2,vel),3);
					Player wielder=s.wielder();
					double dir=Math.atan2(y()-(s.wielder().y()-8),x()-s.wielder().x());
					double dir2=0;
					if(wielder.facing()==0)
						dir2=0;
					else if(wielder.facing()==1)
						dir2=Math.PI*3.0/2.0;
					else if(wielder.facing()==2)
						dir2=Math.PI;
					else
						dir2=Math.PI/2.0;
					
					double avg=dir;
					//double avg=dir*0.3+dir2*0.7;
					//double avg=Entity.dot(dir,dir2);
					
					velocity=new Pair(vel*Math.cos(avg),vel*Math.sin(avg));
					hitTimer=5;
					struck=true;
				}
			}
		}
		else
		{
			hitTimer--;
		}
		
		GameView2D view=(GameView2D)GameEngine.instance().view();
		
		if(y()>view.height()||y()<0)
		{
			if(y()>view.height())
				move(x(),view.height());
			else
				move(x(),0);
			if(vel()>1)
				velocity=new Pair(velocity.first*0.9,-velocity.second*0.95);
			else
				velocity=new Pair(velocity.first,-velocity.second);
			//struck=false;
		}
		if(x()+2>view.width()-8||x()-2<0+8)
		{
			if(x()+2>view.width()-8)
			{
				move(view.width()-8-2,y());
				checkPoint();
			}
			else
			{
				move(0+8+2,y());
				checkPoint();
			}
			if(vel()>1)
				velocity=new Pair(-velocity.first*0.95,velocity.second*0.9);
			else
				velocity=new Pair(-velocity.first,velocity.second);
			//struck=false;
		}
		
		
		//Move ball
		this.moveDelta(velocity.first,velocity.second);
	}
	
	private void checkPoint()
	{
		GameView2D view=(GameView2D)GameEngine.instance().view();
		if(x()<view.width()/2)
		{
			int change=0;
			if(Main.leftWall.size()==0)
				change=0;
			else if(Main.leftWall.size()==1)
				change=1;
			else if(Main.leftWall.size()==2)
				change=2;
			else
				change=3;
			if(y()>=8*(7-change)&&y()<8*(13+change))
			{
				Main.reset();
				Main.points++;
			}
		}
		else
		{
			int change=0;
			if(Main.rightWall.size()==0)
				change=0;
			else if(Main.rightWall.size()==1)
				change=1;
			else if(Main.rightWall.size()==2)
				change=2;
			else
				change=3;
			if(y()>=8*(7-change)&&y()<8*(13+change))
			{
				if(Main.lives==1)
				{
					Main.lives=0;
					Main.endGame();
				}
				else
					Main.lives--;
			}
			else if(!struck)
			{
				Main.reset();
				return;
			}
		}
	}
	
	private double vel()
	{
		return(Math.sqrt(velocity.first*velocity.first+velocity.second*velocity.second));
	}
	
	private Pair<Double, Double> velocity;
	private boolean lastHit;
	private int hitTimer;
	private boolean struck;
}
