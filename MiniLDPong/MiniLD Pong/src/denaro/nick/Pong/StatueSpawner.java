package denaro.nick.Pong;

import denaro.nick.core.GameEngine;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;
import denaro.nick.core.entity.EntityEvent;
import denaro.nick.core.entity.EntityListener;
import denaro.nick.utilities.Pair;

public class StatueSpawner
{

	@SuppressWarnings({"unchecked","rawtypes"})
	public StatueSpawner()
	{
		points=new Pair[12];
		points[0]=new Pair(16*5,16*0);
		points[1]=new Pair(16*9,16*0);
		points[2]=new Pair(16*7,16*1);
		points[3]=new Pair(16*5,16*2);
		points[4]=new Pair(16*9,16*2);
		points[5]=new Pair(16*7,16*3);
		points[6]=new Pair(16*7,16*6);
		points[7]=new Pair(16*5,16*7);
		points[8]=new Pair(16*9,16*7);
		points[9]=new Pair(16*7,16*8);
		points[10]=new Pair(16*5,16*9);
		points[11]=new Pair(16*9,16*9);
		statues=new Statue[points.length];
	}
	
	public void spawn()
	{
		double cdf=Math.random();
		
		int count=0;
		for(int i=0;i<statues.length;i++)
		{
			if(statues[i]!=null&&statues[i].isDestroyed())
				statues[i]=null;
			if(statues[i]==null)
				count++;
		}
		int r=(int)(Math.random()*count);
		int i=-1;
		while(r>=0&&++i<statues.length)
			if(statues[i]==null)
				r--;
		if(i>=statues.length)
			return;
		if(cdf<0.2)
			statues[i]=new Statue(Sprite.sprite("Fairy Statue"),points[i].first+8,points[i].second+16,50);
		else
			statues[i]=new Statue(Sprite.sprite("Dark Fairy Statue"),points[i].first+8,points[i].second+16,-50);
		
		GameEngine.instance().addEntity(statues[i],null);
	}
	
	private Pair<Integer,Integer>[] points;
	private Statue[] statues;
}
