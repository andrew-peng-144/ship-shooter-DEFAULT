package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
//import javax.swing.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import abilities.MissileLaunch;
import entities_super.EnemyShip;
import main.Main;
import shipshooter.SSGameLoop;
import utils.AUtils;
import utils.Vector;

/**
 * 
 * @author ADNREW
 *
 */
public class PlayerShip {

	/** This is the HitBox */

	private Rectangle hb; // HitBox

	public java.util.List<Bullet> pBullets;

	public static final int SPACE_FROM_BOTTOM = 100;

	public BufferedImage img;

	private float hp, maxhp;

	private boolean left, right, up, down;

	public int maxPlayerBulletsAllowed = 30;

	public int score;

	public int money;

	public boolean isShooting;

	/**
	 * delay in milliseconds between shots
	 */
	public double bFiringDelay;

	/**
	 * bullet damage
	 */
	public float bDamage; // bullets
	public float bSpeed;

	public float mDamage; // missile
	public float mSpeed;
	public double mFiringDelay;

	// public boolean missilesEnabled = true;

	public final static float SPEED = 25f / 60f; // #player moves per invocation
													// of
	// move(); is
	// constant

	public boolean isDead;

	public MissileLaunch ml;

	public PlayerShip() { // when game stage 1 starts
		ml = new MissileLaunch();
		isDead = false;

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/playership.png"));
		} catch (IOException e1) {
		}
		// TODO PLAYER HITBOX IS SHRUNK A BIT TO MAKE IT FAIR
		img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);

		int width = img.getWidth();
		int height = img.getHeight() - (12 * 4);

		int x = Main.FRAME_WIDTH / 2 - width / 2; // middle
		int y = Main.FRAME_HEIGHT - SPACE_FROM_BOTTOM - (7 * 4); // 100 pixels
																	// from
																	// bottom

		hp = 200;
		setMaxhp(200);
		hb = new Rectangle(x, y, width, height);
		pos = new Vector(x, y);

		bFiringDelay = 200.0;
		bDamage = 3;
		bSpeed = 3f / 2f;

		mDamage = 15;
		mSpeed = 1f / 2f;
		mFiringDelay = 2000.0;

		pBullets = new ArrayList<>();

		energy = 0;
		maxEnergy = 200;
		energyRegenRate = 200;

	}

	/*
	 * total delta time for side bullets
	 */
	double totaldt4b;
	/*
	 * total delta time for missiles
	 */
	double totaldt4m;

	public int energy;
	public int maxEnergy;
	/**
	 * # of milliseconds of delay per one energy
	 */
	public int energyRegenRate;
	/**
	 * total delta time for energy regen
	 */
	double tdt4energy;

	public Vector pos;

	public void tick(double dt) {
		for (int i = 0; i < pBullets.size(); i++) {
			Bullet b = pBullets.get(i);
			if (b.shouldBeRemoved) {
				pBullets.remove(b);
				System.out.println("REVMOED");
			}
		}

		// PLAYER MOVEMENT LISTENER

		if (isDead) {
			try {
				img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/playerexplosion.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			// check if any enemy's bullet collided w/ player

			int ieb = -1; // index of enemy bullet
			int ie = -1; // index of the enemy whose bullet hit the player

			for (int i = 0; i < SSGameLoop.eList.size(); i++) {

				EnemyShip e = SSGameLoop.eList.get(i);

				for (int j = 0; j < e.eBullets.size(); j++) {
					if (e.eBullets.get(j).getHb().intersects(getHb())) {
						ieb = j;
						ie = i;
						break;
					}

				}

			}

			if (ieb >= 0 && ie >= 0) {
				EnemyShip e = SSGameLoop.eList.get(ie);
				setHp(getHp() - e.eBullets.get(ieb).getDamage());
				checkDeath();
				// subtract player's hp from enemy bullet's damage
				e.eBullets.remove(ieb); // remove enemy's bullet

			}
			if (isLeft()) {
				pos = pos.addTo(new Vector(-SPEED, 0).scale(dt));
				//System.out.println("left");
			}
			if (isRight()) {
				pos = pos.addTo(new Vector(SPEED, 0).scale(dt));
				//System.out.println("right");
			}
			if (isUp()) {
				pos = pos.addTo(new Vector(0, -SPEED).scale(dt));
				//System.out.println("up");
			}
			if (isDown()) {
				pos = pos.addTo(new Vector(0, SPEED).scale(dt));
				//System.out.println("down");
			}



			totaldt4b += dt;

			if (energy < maxEnergy) {
				tdt4energy += dt;
				if (tdt4energy >= energyRegenRate) {
					tdt4energy -= energyRegenRate;
					energy++;
				}
			}

			if (isShooting) {
				if (totaldt4b >= bFiringDelay) {

					shootBullets();
					totaldt4b = 0;
				}

			}
			if (pos.y > Main.FRAME_HEIGHT - SSGameLoop.LOWERWALL)
				pos.y = Main.FRAME_HEIGHT - SSGameLoop.LOWERWALL;
			if (pos.y <= Main.FRAME_HEIGHT - SSGameLoop.WALL)
				pos.y = Main.FRAME_HEIGHT - SSGameLoop.WALL;
			if (pos.x <= 0) // left bound
				pos.x += PlayerShip.SPEED * dt;
			if (pos.x>= Main.FRAME_WIDTH - getHb().width) // right bound
				pos.x = Main.FRAME_WIDTH - getHb().width;

			for (int i = 0; i < pBullets.size(); i++) {
				Bullet b = pBullets.get(i);
				b.tick(dt);
			}
			ml.tick(dt);
			
			hb.x = (int) pos.x;
			hb.y = (int) pos.y;
		}

	}

	public void render(Graphics2D g2d) {

		g2d.drawImage(img, hb.x, hb.y - 28, hb.width, hb.height + 48, null);

		if (SSGameLoop.hbIsOn)
			g2d.draw(hb);

		// player's bullets
		// I TRIED FOR-EACH BUT IT THREW ConcurrentModificationException
		for (int i = 0; i < pBullets.size(); i++) {
			Bullet b = pBullets.get(i);
			b.getHb().y -= b.getSpeed(); // TODO move the bullet

			if (b.getHb().y < 0) { // IF PLAYER BULLET GOES OFF SCREEN,
									// REMOVE IT
				pBullets.remove(i);
			} else
				b.render(g2d);
		}

		ml.render(g2d);
	}

	public int bulletLevel = 1;
	public int numBullets = 2;

	public void levelUpCannons() {
		bulletLevel++;

		switch (bulletLevel) {
		case 2:
			bDamage = 4;
			bFiringDelay = 170;
			// bSpeed = 3/2f;
			// numBullets = 2;
			break;
		case 3:
			bDamage = 5;
			numBullets = 3;
			break;
		case 4:
			bDamage = 7;
			bSpeed = 2f;
			bFiringDelay = 150;
			break;
		case 5:
			bDamage = 8;
			numBullets = 4;
			break;
		case 6:
			bDamage = 10;
			bFiringDelay = 130;
			break;
		default:
			bDamage = bulletLevel + 5;
			bFiringDelay = 130-((bulletLevel-6)*5);
			if(bFiringDelay < 50)
				bFiringDelay = 50;
			break;
		}

	}

	public void shootBullets() {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO
					.read(getClass().getResourceAsStream("/shipshooter/bullets/playerbullet" + (bulletLevel > 3 ? 3 : bulletLevel) + ".png"));
			if(bimg == null){
				bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/missingimg.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		Bullet b;

		switch (bulletLevel) { // lol this is probably the least efficient thing
								// ive done
		case 1:
		case 2:
			b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.x + 6 - bimg.getWidth() / 2, hb.y,
					bimg, false);
			pBullets.add(b);

			b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(),
					(int) (hb.getMaxX() - 6 - bimg.getWidth() / 2), hb.y, bimg, false);
			pBullets.add(b);
			break;

		case 3:
		default:
			b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), (int)(hb.getCenterX() + 4 - bimg.getWidth() / 2), hb.y,
					bimg, false);
			pBullets.add(b);

			b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(),
					(int) (hb.getMaxX() - 4 - bimg.getWidth() / 2), hb.y, bimg, false);
			pBullets.add(b);

			b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.x - bimg.getWidth() / 2, hb.y, bimg,
					false);
			pBullets.add(b);

		}

		// totalShots += 2;
	}

	private void checkDeath() {
		if (hp <= 0) {
			hp = 0;
			isDead = true;
		}
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public Rectangle getHb() {
		return hb;
	}

	public void setHb(Rectangle hb) {
		this.hb = hb;
	}
	/*
	 * public Bullet getPb() { return pb; }
	 * 
	 * public void setPb(Bullet pb) { this.pb = pb; }
	 */

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float f) {
		this.hp = f;
	}

	public float getMaxhp() {
		return maxhp;
	}

	public void setMaxhp(float maxhp) {
		this.maxhp = maxhp;
	}

	/**
	 * sets all directions to false
	 */
	public void stop() {
		// TODO Auto-generated method stub
		up = false;
		down = false;
		left = false;
		right = false;
	}

}
