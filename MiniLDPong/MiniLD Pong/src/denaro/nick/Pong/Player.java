package denaro.nick.Pong;


import denaro.nick.core.Focusable;
import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.controller.ControllerEvent;
import denaro.nick.core.controller.ControllerListener;
import denaro.nick.core.entity.Entity;
import denaro.nick.core.view.GameView2D;

public class Player extends Entity implements Focusable, ControllerListener
{

	public Player(Sprite sprite,double x,double y)
	{
		super(sprite,x,y);
		keys=new KEYS[Main.SWING+1];
		direction=2;
		walkIndex=0;
	}

	@Override
	public void actionPerformed(ControllerEvent event)
	{
		if(Main.end)
		{
			if(Main.endTimer.isFinished())
				if(event.action()==ControllerEvent.PRESSED&&event.code()==Main.SWING)
					Main.restart();
			return;
		}
		if(event.action()==ControllerEvent.PRESSED)
		{
			if(keys[event.code()]!=KEYS.PRESSED&&keys[event.code()]!=KEYS.HELD)
				keys[event.code()]=KEYS.PRESSED;
				
			else
				keys[event.code()]=KEYS.HELD;
		}
		if(event.action()==ControllerEvent.RELEASED)
			keys[event.code()]=KEYS.NONE;
	}

	@Override
	public void tick()
	{
		if(Main.end)
			return;
		//Move Character
		if(sword==null&&performMovement())
		{
			walkIndex++;
		}
		else
			walkIndex=0;
		
		if(sword==null&&keys[Main.SWING]==KEYS.PRESSED)
		{
			keys[Main.SWING]=KEYS.HELD;
			if(direction==0)
				sword=new Sword(this,x(),y(),2,true);
			else if(direction==1)
				sword=new Sword(this,x(),y(),0,false);
			else if(direction==2)
				sword=new Sword(this,x(),y(),2,false);
			else if(direction==3)
				sword=new Sword(this,x(),y(),4,false);
			GameEngine.instance().addEntity(sword,null);
			this.sprite(Sprite.sprite("Player Swing"));
		}
		else if(sword!=null)
		{
			if(sword.time<=0)
			{
				GameEngine.instance().removeEntity(sword,null);
				sword=null;
				this.sprite(Sprite.sprite("Player"));
			}
		}
		
		updateImage();
	}
	
	private void updateImage()
	{
		int base=0;
		if(direction==0)
			base=6;
		else if(direction==1)
			base=2;
		else if(direction==2)
			base=4;
		else
			base=0;
		if(sword==null)//"walking"
		{
			imageIndex(base+walkIndex/8%2);
		}
		else
		{
			int offset=sword.index();
			if(direction==0)
				offset=Math.abs(offset-2);
			else if(direction==1)
				offset-=0;
			else if(direction==2)
				offset=Math.abs(offset-2);
			else if(direction==3)
				offset=Math.abs(offset-4);
			
			
			if(offset==0||offset==1)
				imageIndex(base);
			else
				imageIndex(base+1);
		}
	}
	
	private boolean performMovement()
	{
		boolean moved=false;
		GameView2D view=(GameView2D)GameEngine.instance().view();
		
		if(keys[Main.UP]==KEYS.PRESSED||keys[Main.UP]==KEYS.HELD)
		{
			if(y()-16>0)
				moveDelta(0,-1.5);
			direction=1;
			moved=true;
		}
		if(keys[Main.DOWN]==KEYS.PRESSED||keys[Main.DOWN]==KEYS.HELD)
		{
			if(y()<view.height())
				moveDelta(0,1.5);
			direction=3;
			moved=true;
		}
		
		if(keys[Main.LEFT]==KEYS.PRESSED||keys[Main.LEFT]==KEYS.HELD)
		{
			if(x()<view.width()/2)
			{
				if(x()-8>0+8)
					moveDelta(-1.5,0);
			}
			else
				if(x()-8>view.width()-48)
					moveDelta(-1.5,0);
			direction=2;
			moved=true;
		}
		if(keys[Main.RIGHT]==KEYS.PRESSED||keys[Main.RIGHT]==KEYS.HELD)
		{
			if(x()>view.width()/2)
			{
				if(x()+8<view.width()-8)
					moveDelta(1.5,0);
			}
			else
				if(x()+8<48)
					moveDelta(1.5,0);
			direction=0;
			moved=true;
		}
		
		return(moved);
	}
	
	public int facing()
	{
		return(direction);
	}
	
	private enum KEYS{NONE,PRESSED,HELD};
	
	private KEYS[] keys;
	private byte direction;
	private Sword sword;
	private int walkIndex;
}
