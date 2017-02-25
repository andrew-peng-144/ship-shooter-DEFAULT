package menuscreen;

import java.awt.image.BufferedImage;

import shipshooter.ActionButton;


public class QuitButton extends ActionButton{
	public QuitButton(int x, int y,  BufferedImage img, BufferedImage hoverImg, String desc) {
		super(x, y, img, hoverImg, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec() {
		// TODO Auto-generated method stub
		super.exec();
		System.exit(0);
	}
}
