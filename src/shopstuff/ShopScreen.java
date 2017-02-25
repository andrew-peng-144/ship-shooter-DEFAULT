package shopstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Main;
import shipshooter.GameState;
import shipshooter.SSGameLoop;
import utils.AUtils;

/**
 * very important that SIDE SHOOTERS are the new main shooter!
 * 
 * @author Rose
 *
 */
public class ShopScreen {
	public static BufferedImage title; // "SHOP"

	public static Rectangle continueBtn;
	public static boolean continueBtnClicked;

	/**
	 * for the mouse listener
	 */
	public static ArrayList<ShopButton> buttons;

	ShopButton umhp;
	ShopButton udef;
	// ShopButton uer;

	ShopButton uc;
	ShopButton uml;
	ShopButton us;

	public ShopScreen() {
		int pw = Main.loop.getWidth();
		int ph = Main.loop.getHeight();

		buttons = new ArrayList<>();

		try {
			setButtonImages();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		umhp = new ShopButton(ShopButtonType.upgradeMaxHp, pw * 2 / 4 - ucImg.getWidth() / 2,
				ph * 2 / 4 - ucImg.getHeight() / 2, umhpImg, umhpImg, "Increases max hp by 30.");
		// udef = new ShopButton(ShopButtonType.upgradeDefense, ph, ph, null,
		// null, null);
		uc = new ShopButton(ShopButtonType.upgradeCannons, pw * 1 / 4 - ucImg.getWidth() / 2,
				ph * 1 / 4 - ucImg.getHeight() / 2, ucImg, ucImg, "Increases the damage, firerate, and velocity of the cannons. Also changes the sprite.");
		// uml = new ShopButton(ShopButtonType.upgradeMissiles, ph, ph, null,
		// null, null);
		// us = new ShopButton(ShopButtonType.upgradeShield, ph, ph, null, null,
		// null);

		buttons.add(uc);
		buttons.add(umhp);
		
		continueBtn = new Rectangle(Main.FRAME_WIDTH * 3 / 4, Main.FRAME_HEIGHT * 1 / 24, 220, 50);

		// TODO shop design
		try {
			title = ImageIO.read(getClass().getResourceAsStream("/shipshooter/shoptitle.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		title = AUtils.getResizedImage(title, title.getWidth() * 4, title.getHeight() * 4);

	}

	BufferedImage umhpImg;
	BufferedImage udefImg;
	BufferedImage ucImg;
	BufferedImage umlImg;
	BufferedImage usImg;

	private void setButtonImages() throws IOException {
		BufferedImage temp = ImageIO
				.read(getClass().getResourceAsStream("/shipshooter/shopbuttons/upgradecannonbutton.png"));
		ucImg = AUtils.getResizedImage(temp, temp.getWidth() * 4, temp.getHeight() * 4);
		temp = ImageIO.read(getClass().getResourceAsStream("/shipshooter/shopbuttons/upgradehpbutton.png"));
		umhpImg = AUtils.getResizedImage(temp, temp.getWidth()*4, temp.getHeight()*4);
		
	
	}

	public void tick(double dt) {

		for (ShopButton sb : buttons)
			sb.tick(dt);

		// continue button's "tick" below
		if (continueBtnClicked) {
			SSGameLoop.state = GameState.IN_GAME;
			Main.loop.nextStage();
			continueBtnClicked = false;
		}
	}

	public void render(Graphics2D g2d) {

		Color oldColor = g2d.getColor();
		Font oldFont = g2d.getFont();

		g2d.setColor(Color.DARK_GRAY);
		// text box for displaying hover text of buttons
		g2d.fillRect(0, Main.loop.getHeight() * 3 / 4, Main.loop.getWidth(), Main.loop.getHeight() * 1 / 4);

		g2d.drawImage(title, Main.FRAME_WIDTH / 2 - title.getWidth() / 2,
				Main.FRAME_HEIGHT / 12 - title.getHeight() / 2, null);
		g2d.fillRect(50,50,80,40);
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g2d.drawString("YOU HAVE $"+SSGameLoop.p.money,100,55);
		
		for (ShopButton sb : buttons)
			sb.render(g2d);

		g2d.setColor(Color.CYAN);
		g2d.fill(continueBtn);

		g2d.setColor(Color.BLUE);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		FontMetrics fm = g2d.getFontMetrics();
		String cont = "Continue to next stage";
		g2d.drawString(cont, (int) (continueBtn.getCenterX() - fm.stringWidth(cont) / 2),
				(int) continueBtn.getCenterY());
		g2d.setColor(Color.white);
		g2d.drawString("Damage: "+SSGameLoop.p.bDamage, 50, Main.loop.getHeight()-200);
		g2d.drawString("Fires per sec: "+(1000f/SSGameLoop.p.bFiringDelay), 50, Main.loop.getHeight()-220);
		g2d.drawString("Bullet Speed: "+SSGameLoop.p.bSpeed, 50, Main.loop.getHeight()-240);
		g2d.drawString("Health: "+SSGameLoop.p.getHp(), 50, Main.loop.getHeight()-260);
		
		g2d.setColor(oldColor);
		g2d.setFont(oldFont);
	}

}
