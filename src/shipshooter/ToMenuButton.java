package shipshooter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Main;

public class ToMenuButton extends ActionButton {

	@Override
	public void exec() {
		// TODO Auto-generated method stub
		super.exec();
		SSGameLoop.state = GameState.MENU;
		Main.loop.reset();
	}

	@Override
	public void tick(double dt) {
		// TODO Auto-generated method stub
		super.tick(dt);
	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		super.render(g2d);
	}

	public ToMenuButton(int x, int y, BufferedImage img, BufferedImage hoverImg, String desc) {
		super(x, y, img, hoverImg, desc);
		// TODO Auto-generated constructor stub
	}

}
