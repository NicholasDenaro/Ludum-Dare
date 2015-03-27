package denaro.nick.Pong;

import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import denaro.nick.core.EngineAction;
import denaro.nick.core.FixedFPSType;
import denaro.nick.core.GameEngine;
import denaro.nick.core.GameEngineException;
import denaro.nick.core.GameFrame;
import denaro.nick.core.Location;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;
import denaro.nick.core.timer.TickingTimer;
import denaro.nick.core.timer.Timer;
import denaro.nick.core.view.GameView2D;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			GameEngine engine=GameEngine.instance(new FixedFPSType(TICKRATE,60),false);
			GameView2D view=new CustomView(240,160,3,3);
			engine.view(view);
			GameFrame frame=new GameFrame("LoZ Pong",engine);
			frame.setVisible(true);
			
			KeyboardController controller=new KeyboardController();
			engine.controller(controller);

			//System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			//System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"../Resources/Player.png");
			//System.out.println(System.getProperty("java.class.path"));
			//solution 1, ugly: new Sprite("Player",System.getProperty("java.class.path").split(";")[0]+"/Resources/Player.class",16,16,new Point(8,16));
			//System.out.println(Main.class.getResource("/Player.png").getFile());
			
			//ImageIO.read(new File(Main.class.getResource("/Player.png").getFile()));
			//System.out.println(Main.class.getResource("/Player.png").toURI());
			new Sprite("Player","Player.png",16,16,new Point(8,16));
			new Sprite("Player Swing","Player Swing.png",16,16,new Point(8,16));
			
			new Sprite("Ball","Ball.png",8,8,new Point(4,4));
			
			new Sprite("Sword","Sword.png",16,16,new Point(8,8));
			
			new Sprite("Fairy Statue","FairyStatue.png",16,16,new Point(8,16));
			new Sprite("Dark Fairy Statue","DarkFairyStatue.png",16,16,new Point(8,16));
			
			new Sprite("Break Animation","BreakAnimation.png",16,16,new Point(8,16));
			new Sprite("Button","Button.png",16,16,new Point(0,0));
			
			new Sprite("Back Wall","BackWall.png",8,160,new Point(0,0));
			
			new Sprite("GG","Game Over.png",80,16,new Point(43,16));
			
			new Sprite("Hearts","Hearts.png",40,8,new Point(0,0));
			
			background=ImageIO.read(Main.class.getClassLoader().getResourceAsStream("Background.png"));
			
			ball=new Ball(Sprite.sprite("Ball"),120,80);
			ball.mask(new Area(new Rectangle2D.Double(-1,-1,1,1)));
			
			rightWall=new BackBoard(view.width()-8,0);
			leftWall=new BackBoard(0,0);
			
			player=new Player(Sprite.sprite("Player"),240-16,88);
			engine.addControllerListener(player);
			engine.requestFocus(0,player);
			
			starter=new Button(16*12,16*5);
			starter.mask(new Area(new Rectangle2D.Double(4,4,4,4)));
			
			Location loc=new Location();
			//BufferedImage bg=ImageIO.read(new File("Background.png"));
			loc.backgroundLayers().put(-10,background);
			
			engine.addEntity(ball,loc);
			engine.addEntity(player,loc);
			engine.addEntity(starter,loc);
			engine.addEntity(leftWall,loc);
			engine.addEntity(rightWall,loc);
			
			//startSpawnTimer();
			
			spawner=new StatueSpawner();
			engine.location(loc);
			
			spawner.spawn();
			
			engine.start();
			time=TIME;
		}
		catch(GameEngineException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void startSpawnTimer()
	{
		TickingTimer t=new TickingTimer(5*60,false)
		{
			@Override
			public void action()
			{
				if(!end)
				{
					spawner.spawn();
					Main.startSpawnTimer();
				}
			}
		};
		
		GameEngine.instance().addTimer(t);
	}
	
	public static void gameTimer()
	{
		if(!end)
		{
			TickingTimer t=new TickingTimer(Main.time,true)
			{
				@Override
				public void action()
				{
					if(Main.end)
					{
						this.kill();
						return;
					}
					if(Main.time--<=0)
					{
						endGame();
					}
				}
			};
			
			GameEngine.instance().addTimer(t);
		}
	}
	
	public static void restart()
	{
		/*time=TIME;
		starter.depress();
		ball.reset();
		leftWall.reset();
		rightWall.reset();
		player.move(240-16,88);
		end=false;
		GameEngine.instance().removeEntity(gg,null);*/
		end=false;
		time=TIME;
		GameView2D view=(GameView2D)GameEngine.instance().view();
		ball=new Ball(Sprite.sprite("Ball"),120,80);
		ball.mask(new Area(new Rectangle2D.Double(-1,-1,1,1)));
		
		rightWall=new BackBoard(view.width()-8,0);
		leftWall=new BackBoard(0,0);
		
		player=new Player(Sprite.sprite("Player"),240-16,88);
		GameEngine.instance().addControllerListener(player);
		GameEngine.instance().requestFocus(0,player);
		
		starter=new Button(16*12,16*5);
		starter.mask(new Area(new Rectangle2D.Double(4,4,4,4)));
		
		Location loc=new Location();
		loc.backgroundLayers().put(-10,background);
		
		GameEngine.instance().addEntity(ball,loc);
		GameEngine.instance().addEntity(player,loc);
		GameEngine.instance().addEntity(starter,loc);
		GameEngine.instance().addEntity(leftWall,loc);
		GameEngine.instance().addEntity(rightWall,loc);
		
		spawner=new StatueSpawner();
		GameEngine.instance().location(loc);
		
		spawner.spawn();
		points=0;
		lives=2;
	}
	
	public static void reset()
	{
		//starter.depress();
		//ball.reset();
		leftWall.reset();
	}
	
	public static void endGame()
	{
		gg=new Entity(Sprite.sprite("GG"),((GameView2D)GameEngine.instance().view()).width()/2,0){
			@Override
			public void tick()
			{
				
			}
		};
		GameEngine.instance().addEntity(gg,null);
		end=true;
		
		endTimer=new TickingTimer(TICKRATE*3,true)
		{
			@Override
			public void action()
			{
				if(time-->TICKRATE*3-((GameView2D)GameEngine.instance().view()).height()/2-5)
					gg.moveDelta(0,1);
			}
			
			private int time=TICKRATE*3;
			
		};
		GameEngine.instance().addTimer(endTimer);
	}
	
	public static StatueSpawner spawner;
	public static Ball ball;
	public static BackBoard leftWall;
	public static BackBoard rightWall;
	public static Button starter;
	public static Player player;
	public static boolean end;
	public static Entity gg;
	public static int time;
	public static TickingTimer endTimer;
	public static BufferedImage background;
	public static int points=0;
	public static int lives=2;
	
	public static final int TICKRATE=60;
	public static final int TIME=TICKRATE*60*3;
	
	public static final int UP=0;
	public static final int DOWN=1;
	public static final int LEFT=2;
	public static final int RIGHT=3;
	public static final int SWING=4;
}
