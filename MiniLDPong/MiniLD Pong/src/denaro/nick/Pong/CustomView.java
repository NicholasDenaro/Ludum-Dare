package denaro.nick.Pong;

import java.awt.Color;
import java.awt.Graphics2D;

import denaro.nick.core.Location;
import denaro.nick.core.Sprite;
import denaro.nick.core.view.GameView2D;

public class CustomView extends GameView2D
{

	public CustomView(int width,int height,double hscale,double vscale)
	{
		super(width,height,hscale,vscale);
	}
	
	public void drawLocation(Location currentLocation, Graphics2D g)
	{
		super.drawLocation(currentLocation,g);
		g.setColor(Color.white);
		int t=Main.time+59;
		String time=(t/60/60<0?"0":"")+t/60/60+":"+(t/60.0%60<10?"0":"")+t/60%60;
		g.drawString("Time: "+time,8,16);
		int offset=width()-8-g.getFontMetrics().stringWidth(""+Main.points);
		g.drawString(""+Main.points,offset,16);
		Sprite hearts=Sprite.sprite("Hearts");
		g.drawImage(hearts.subimage(Main.lives),offset-hearts.width(),0,null);
		
		if(Main.end)
			if(Main.endTimer.isFinished())
				g.drawString("Press x",width()/2-g.getFontMetrics().stringWidth("Press x")/2,height()/2+24);
	}
}
