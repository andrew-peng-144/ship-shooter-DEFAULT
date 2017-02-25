package listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.Main;
import menuscreen.HelpScreen;
import shipshooter.ActionButton;
import shipshooter.GameState;
import shipshooter.SSGameLoop;
import shopstuff.ShopButton;
import shopstuff.ShopScreen;

public class AMouseListener implements MouseListener {
	public static Point click;

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		click = new Point(e.getX(), e.getY());
		System.out.println(click);
		if (e.getButton() == MouseEvent.BUTTON1) {

			if (SSGameLoop.state == GameState.MENU) {

				for (ActionButton ab : SSGameLoop.ms.menuButtons) {
					if (ab.clickArea.contains(click)) {
						ab.clicked = true;
						System.out.println("clicked on button");
					}
				}

			}
			else if( SSGameLoop.state == GameState.IN_GAME){
				if(SSGameLoop.p.isDead && SSGameLoop.toMenuButton.clickArea.contains(click)){
					SSGameLoop.toMenuButton.clicked = true;
				}
			}
			else if (SSGameLoop.state == GameState.HELP) {
				if (HelpScreen.backBtn.contains(click)) {
					HelpScreen.backBtnClicked = true;
				}
			}

			else if (SSGameLoop.state == GameState.SHOP) {
				for (ShopButton sb : ShopScreen.buttons) {
					if (sb.clickArea.contains(click))
						sb.clicked = true;
				}

				if (ShopScreen.continueBtn.contains(click))
					ShopScreen.continueBtnClicked = true;
			}

		}
		click = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
