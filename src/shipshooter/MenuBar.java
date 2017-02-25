package shipshooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;

import abilities.MissileLaunch;
import entities.PlayerShip;
import main.Main;
import utils.AUtils;

public class MenuBar {

	public static BufferedImage bkgd;

	BufferedImage coin;

	private Rectangle hpBar;
	private Rectangle eBar;

	public MenuBar() {
		try {
			bkgd = ImageIO.read(getClass().getResourceAsStream("/shipshooter/menubar/menubarbkgd.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		bkgd = AUtils.getResizedImage(bkgd, bkgd.getWidth() * 4, bkgd.getHeight() * 4);

		hpBar = new Rectangle(40, Main.loop.getHeight() - bkgd.getHeight() + 16, 200, 20);
		eBar = new Rectangle(40, Main.loop.getHeight() - bkgd.getHeight() + 48, 200, 20);

		try {
			coin = ImageIO.read(getClass().getResourceAsStream("/shipshooter/menubar/coin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		coin = AUtils.getResizedImage(coin, coin.getWidth() * 2, coin.getHeight() * 2);
	}

	public void tick(double dt) {

	}

	public void render(Graphics2D g) {
		Color oc = g.getColor();
		Font of = g.getFont();

		g.drawImage(bkgd, 0, Main.loop.getHeight() - bkgd.getHeight(), null);
		// System.out.println(Main.loop.getHeight()+" ASDFASD
		// "+Main.loop.getWidth());

		PlayerShip ps = SSGameLoop.p;
		// health bar
		if (ps == null)
			System.out.println("ps is null");
		float red = (1 - ps.getHp() / ps.getMaxhp());
		float green = ps.getHp() / ps.getMaxhp();
		if (red > 1f)
			red = 1f;
		if (green > 1f)
			green = 1f;
		// System.out.println("R:"+red);
		// System.out.println("G:"+green);
		g.setColor(new Color(red, green, 0));
		g.fillRect(hpBar.x, hpBar.y, (int) (hpBar.width * (ps.getHp() / ps.getMaxhp())), hpBar.height);
		g.setColor(Color.WHITE);
		g.draw(hpBar);

		g.setColor(Color.BLUE);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		g.drawString((int) ps.getHp() + " / " + (int) ps.getMaxhp(), hpBar.x + 20, hpBar.y + hpBar.height / 2 + 5);

		// energy bar

		//System.out.println((int) ps.energy + " / " + (int) ps.maxEnergy);
		g.setColor(Color.yellow);
		g.fillRect(eBar.x, eBar.y, (int) (eBar.width * ((ps.energy+0.0) / ps.maxEnergy)), eBar.height);
		g.setColor(Color.white);
		g.draw(eBar);
		
		g.setColor(Color.BLUE);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		g.drawString((int) ps.energy + " / " + (int) ps.maxEnergy, eBar.x + 20, eBar.y + eBar.height / 2 + 5);

		// stage&wave text

		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
		g.setColor(Color.CYAN);
		String stg = "STAGE " + SSGameLoop.stage + " - " + SSGameLoop.wave;
		int w = AUtils.getStringWidth(stg, g);
		g.drawString(stg, Main.loop.getWidth() / 2 - w / 2, Main.loop.getHeight() - bkgd.getHeight() / 2 - 10);

		// money & score
		int coinX = (int) (hpBar.getMaxX() + 20);
		g.drawImage(coin, coinX, hpBar.y, null);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		g.drawString("" + SSGameLoop.p.money, coinX + coin.getWidth() + 10, hpBar.y + coin.getHeight());
		g.drawString("SCORE: " + SSGameLoop.p.score, coinX, hpBar.y + coin.getHeight() + 20);

		// time
		int secs = SSGameLoop.stageSeconds;
		String time = SSGameLoop.stageMinutes + ":" + (secs < 10 ? "0" + secs : "" + secs);
		g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18));
		g.setColor(Color.ORANGE);
		w = AUtils.getStringWidth(time, g);
		g.drawString(time, Main.loop.getWidth() / 2 - w / 2, Main.loop.getHeight() - bkgd.getHeight() / 2 + 20);

		g.setColor(Color.white);

		g.setColor(oc);
		g.setFont(of);
	}

}
