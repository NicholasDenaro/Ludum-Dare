package denaro.nick.Pong;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import denaro.nick.core.GameEngine;
import denaro.nick.core.controller.Controller;
import denaro.nick.core.controller.ControllerEvent;
import denaro.nick.core.view.GameView2D;

public class KeyboardController extends Controller implements KeyListener
{
	@Override
	public boolean init(GameEngine engine)
	{
		GameView2D view=(GameView2D)engine.view();
		view.addKeyListener(this);
		this.addControllerListener(engine);
		createDefaultKeymap();
		return(true);
	}

	@Override
	protected void createDefaultKeymap()
	{
		HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
		map.put(KeyEvent.VK_UP,Main.UP);
		map.put(KeyEvent.VK_DOWN,Main.DOWN);
		map.put(KeyEvent.VK_LEFT,Main.LEFT);
		map.put(KeyEvent.VK_RIGHT,Main.RIGHT);
		map.put(KeyEvent.VK_X,Main.SWING);
		keymap(map);
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(keymap().containsKey(e.getKeyCode()))
			this.actionPerformed(new ControllerEvent(this,ControllerEvent.PRESSED,keymap().get(e.getKeyCode())));
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if(keymap().containsKey(e.getKeyCode()))
			this.actionPerformed(new ControllerEvent(this,ControllerEvent.RELEASED,keymap().get(e.getKeyCode())));
	}
}