package menuscreen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import shipshooter.ActionButton;
import shipshooter.GameState;
import shipshooter.SSGameLoop;

public class PlayButton extends ActionButton {

	public PlayButton(int x, int y, BufferedImage img, BufferedImage hoverImg, String desc) {
		super(x, y, img, hoverImg, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec() {
		super.exec();
		SSGameLoop.state = GameState.IN_GAME;
		
	}
}
