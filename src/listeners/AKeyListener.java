package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.PlayerShip;
import main.Main;
import shipshooter.GameState;
import shipshooter.SSGameLoop;

public class AKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (SSGameLoop.state == GameState.IN_GAME) {
			int code = e.getKeyCode();

			if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W)
				SSGameLoop.p.setUp(true);
			if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A)
				SSGameLoop.p.setLeft(true);
			if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D)
				SSGameLoop.p.setRight(true);
			if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S)
				SSGameLoop.p.setDown(true);
			
			if (code == KeyEvent.VK_SPACE)
				SSGameLoop.p.isShooting = true;
			if (code == KeyEvent.VK_B) {
				System.out.println("CHEATERj");
				SSGameLoop.eList.clear();
				// Main.loop.nextWave();
			}
			if (code == KeyEvent.VK_M) {
				System.out.println("CHEATERj2");
				SSGameLoop.p.score += 10;
				SSGameLoop.p.money += 10;
			}
			if (code == KeyEvent.VK_N) {
				System.out.println("CHEATERj3");
				SSGameLoop.p.score += 1000;
				SSGameLoop.p.money += 1000;
			}
			if (code == KeyEvent.VK_H) {
				if (SSGameLoop.hbIsOn)
					SSGameLoop.hbIsOn = false;
				else
					SSGameLoop.hbIsOn = true;
			}
			//probably the most appropriate time to use switch{ statement and of course i dont
			if(code == KeyEvent.VK_F3){
				if (Main.loop.debugEnabled)
					Main.loop.debugEnabled = false;
				else
					Main.loop.debugEnabled = true;
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (SSGameLoop.state == GameState.IN_GAME) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_1) {
				SSGameLoop.p.ml.activated = true;
			}

			if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W)
				SSGameLoop.p.setUp(false);
			if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A)
				SSGameLoop.p.setLeft(false);
			if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D)
				SSGameLoop.p.setRight(false);
			if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S)
				SSGameLoop.p.setDown(false);

			if (code == KeyEvent.VK_SPACE)
				SSGameLoop.p.isShooting = false;
		}
	}

}
