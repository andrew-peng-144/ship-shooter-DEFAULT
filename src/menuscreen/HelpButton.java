package menuscreen;

import java.awt.image.BufferedImage;

import shipshooter.ActionButton;
import shipshooter.GameState;
import shipshooter.SSGameLoop;

public class HelpButton extends ActionButton{

	public HelpButton(int x, int y, BufferedImage img, BufferedImage hoverImg, String desc) {
		super(x, y, img, hoverImg, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec() {
		super.exec();
		SSGameLoop.state = GameState.HELP;
	}

}
