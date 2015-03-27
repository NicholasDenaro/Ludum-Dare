package denaro.nick.Pong;

import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class BackBoard extends Entity
{

	public BackBoard(double x,double y)
	{
		super(Sprite.sprite("Back Wall"),x,y);
		size=0;
	}

	@Override
	public void tick()
	{
		
	}
	
	public void grow()
	{
		if(size<sprite().hSubimages()-1)
			size++;
		imageIndex(size);
	}
	
	public void shrink()
	{
		if(size>0)
			size--;
		imageIndex(size);
	}
	
	public int size()
	{
		return(size);
	}
	
	public void reset()
	{
		size=0;
		imageIndex(size);
	}
	
	private int size;
}
