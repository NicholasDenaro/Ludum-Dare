package denaro.nick.Pong;

import java.util.HashMap;

import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class Sword extends Entity
{

	public static final HashMap<Integer,Integer> map=setMap();
	private static HashMap<Integer,Integer> setMap()
	{
		HashMap<Integer,Integer> m=new HashMap<Integer,Integer>();
		m.put(0,5);
		m.put(1,2);
		m.put(2,1);
		m.put(3,0);
		m.put(4,3);
		m.put(5,6);
		m.put(6,7);
		m.put(7,8);
		return(m);
	}
	
	public Sword(Player wielder, double x, double y, int start, boolean clockwise)
	{
		super(Sprite.sprite("Sword"),x,y);
		this.wielder=wielder;
		centerx=(int)x;
		centery=(int)y;
		time=10;
		index=(byte)start;
		this.clockwise=clockwise;
		this.imageIndex(map.get((int)index));
		fixPosition();
	}

	@Override
	public void tick()
	{
		if(Main.end)
			return;
		time--;
		if(time==6||time==5)
			if(clockwise)
				index=(byte)((index+7)%8);
			else
				index=(byte)((index+1)%8);

		this.imageIndex(map.get((int)index));
		fixPosition();
	}
	
	private void fixPosition()
	{
		if(index==0)
			move(centerx+16-2,centery-8+4);
		if(index==1)
			move(centerx+16-4,centery-8-16+6);
		if(index==2)
			move(centerx,centery-8-16+3);
		if(index==3)
			move(centerx-16+4,centery-8-16+6);
		if(index==4)
			move(centerx-16+2,centery-8+4);
		if(index==5)
			move(centerx-16+6,centery+8);
		if(index==6)
			move(centerx+3,centery+8);
		if(index==7)
			move(centerx+16,centery+8);
	}
	
	public int index()
	{
		return(index);
	}
	
	public Player wielder()
	{
		return(wielder);
	}
	
	public boolean clockwise()
	{
		return(clockwise);
	}
	
	private int centerx;
	private int centery;
	private boolean clockwise;
	private byte index;
	public byte time;
	private Player wielder;
}
