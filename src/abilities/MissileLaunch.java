package abilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.Missile;
import main.Main;
import shipshooter.MenuBar;
import shipshooter.SSGameLoop;
import utils.AUtils;

public class MissileLaunch extends Ability {

	float dmg;
	float speed;
	BufferedImage mimg;

	// int numMissiles, int fireRate4Barrage

	// public MissileLaunch(int cooldown, int level) {
	// super(cooldown, level);
	//
	// dmg = levelToDamage();
	// speed = levelToSpeed();
	// }
	public MissileLaunch() {
		cooldown = 300;
		dmg = 0;
		speed = 1f / 2f;
		energyCost = 40;
		
		try {
			icon = ImageIO.read(getClass().getResourceAsStream("/shipshooter/menubar/missilelaunchicon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = AUtils.getResizedImage(icon, icon.getWidth() * 4, icon.getHeight() * 4);

	}

	@Override
	public void execute() {
		mimg = null;
		try {
			mimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/playermissile.png"));
		} catch (IOException e) {
		}

		mimg = AUtils.getResizedImage(mimg, mimg.getWidth() * 4, mimg.getHeight() * 4);
		// totalShots++;

		// System.out.println("TEST123");
		Missile m = new Missile(speed, dmg, mimg.getWidth(), mimg.getHeight(),
				(int) (SSGameLoop.p.getHb().getCenterX() - mimg.getWidth() / 2), SSGameLoop.p.getHb().y, mimg, 90); // temp
		m.splashAnimation = new BufferedImage[2];
		// fill the animation with its frames
		for (int i = 0; i < m.splashAnimation.length; i++) {
			BufferedImage frame = null;
			try {
				frame = ImageIO.read(getClass()
						.getResourceAsStream("/shipshooter/animations/playermissile1/pm1_exp_frame_" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			m.splashAnimation[i] = AUtils.getResizedImage(frame, frame.getWidth() * 4, frame.getHeight() * 4);
		}

		SSGameLoop.p.pBullets.add(m);

	}

	@Override
	public void tick(double dt) {
		// TODO Auto-generated method stub
		dmg = SSGameLoop.p.bDamage * 6;
		iconx = MenuBar.bkgd.getWidth() * 3 / 4;
		icony = Main.loop.getHeight() - MenuBar.bkgd.getHeight() / 2 - icon.getHeight() / 2;
		super.tick(dt);
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);

	}

}
