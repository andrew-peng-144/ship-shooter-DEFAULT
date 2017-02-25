package menuscreen;

////
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import shipshooter.ActionButton;
import utils.AUtils;


public class MenuScreen {

	public ArrayList<ActionButton> menuButtons;

	public BufferedImage title;

	////
	public MenuScreen() throws IOException {
		menuButtons = new ArrayList<>();

		String path = "/shipshooter/buttons";
		
		//below is resizing the main menu button images and creating all the objects for the buttons
		BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path + "/playbutton.png"));
		BufferedImage hov = ImageIO.read(getClass().getResourceAsStream(path + "/playbuttonhovered.png"));
		img = AUtils.getResizedImage(img, img.getWidth()*4, img.getHeight()*4);
		hov = AUtils.getResizedImage(hov, hov.getWidth()*4, hov.getHeight()*4);
		PlayButton pb = new PlayButton(200, 300, img, hov, "");
		
		img = ImageIO.read(getClass().getResourceAsStream(path + "/helpbutton.png"));
		hov = ImageIO.read(getClass().getResourceAsStream(path + "/helpbuttonhovered.png"));
		img = AUtils.getResizedImage(img, img.getWidth()*4, img.getHeight()*4);
		hov = AUtils.getResizedImage(hov, hov.getWidth()*4, hov.getHeight()*4);
		HelpButton hb = new HelpButton(200, 400, img, hov, "");
		
		img = ImageIO.read(getClass().getResourceAsStream(path + "/quitbutton.png"));
		hov = ImageIO.read(getClass().getResourceAsStream(path + "/quitbuttonhovered.png"));
		img = AUtils.getResizedImage(img, img.getWidth()*4, img.getHeight()*4);
		hov = AUtils.getResizedImage(hov, hov.getWidth()*4, hov.getHeight()*4);
		QuitButton qb = new QuitButton(200, 500, img, hov, "");

		menuButtons.add(pb);
		menuButtons.add(hb);
		menuButtons.add(qb);

		try {
			title = ImageIO.read(getClass().getResourceAsStream("/shipshooter/TITLENAME.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param dt
	 */
	public void tick(double dt) {
		for (ActionButton ab : menuButtons)
			ab.tick(dt);

	}

	public void render(Graphics2D g) {
		g.drawImage(title, 100, 50, (int) (title.getWidth() * 8), (int) (title.getHeight() * 8), null);

		for (ActionButton ab : menuButtons)
			ab.render(g);
	}

}
