package entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities_super.EnemyShip;
import shipshooter.SSGameLoop;
import utils.AUtils;
import utils.Vector;

public class Bullet {

	public Rectangle hb; // HitBox

	public BufferedImage img;

	public float damage;

	public float speed;

	public Vector pos;
	public Vector dir;

	/**
	 * DEPRECATED BECAUSE CONFUSING PLAYER'S lvl1 BULLET CONSTRUCTOR -- goes
	 * straight up
	 * 
	 * @param speed
	 * @param damage
	 */
	@Deprecated
	public Bullet(float speed, float damage) {
		// try {
		// img = ImageIO.read(new File("res/playerbullet.png"));
		// } catch (IOException e) {
		try {
			img = ImageIO.read(SSGameLoop.class.getResource("/shipshooter/bullets/playerbullet.png"));
		} catch (IOException e1) {
		}
		// }
		img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);
		this.speed = speed;
		this.damage = damage;
		int width = img.getWidth();
		int height = img.getHeight();
		int x = (int) SSGameLoop.p.getHb().getCenterX() - width / 2;
		int y = (int) SSGameLoop.p.getHb().getY() - height;
		hb = new Rectangle(x, y, width, height);

		pos = new Vector(x, y);
		dir = new Vector(0, -speed);
	}

	/**
	 * custom bullet for both player and enemy that will go straight up or down
	 * whether enemy or not
	 * 
	 * @param speed
	 * @param damage
	 * @param width
	 *            manually set, will not multiply by 4
	 * @param height
	 *            manually set, will not multiply by 4
	 * @param x
	 * @param y
	 * @param img
	 */
	public Bullet(float speed, float damage, int width, int height, int x, int y, BufferedImage img, boolean isEnemy) {
		// TODO img
		this.speed = speed;
		this.damage = damage;
		this.img = img;

		hb = new Rectangle(x, y, width, height);

		if (isEnemy)
			dir = new Vector(0, speed);
		else
			dir = new Vector(0, -speed);

		pos = new Vector(x, y);

	}

	/**
	 * make a directional bullet AND DO NOT ROTATE IMAGE
	 * 
	 * @param theta
	 *            IN DEGREES, the angle with (1,0) as the starting vector
	 * 
	 */
	public Bullet(float speed, float damage, int width, int height, double x, double y, BufferedImage img,
			double theta) {
		this.speed = speed;
		this.damage = damage;
		this.img = img;

		hb = new Rectangle((int) x, (int) y, width, height);

		pos = new Vector(x, y);

		dir = new Vector(speed * Math.cos(Math.toRadians(theta)), -speed * Math.sin(Math.toRadians(theta)));

	}

	/**
	 * make a directional bullet AND ROTATE IMAGE
	 * 
	 * @param speed
	 * @param damage
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @param img
	 * @param theta
	 * @param isEnemy
	 */
	public Bullet(float speed, float damage, int width, int height, double x, double y, BufferedImage img, double theta,
			boolean isEnemy) {
		this.speed = speed;
		this.damage = damage;
		this.img = img;

		hb = new Rectangle((int) x, (int) y, width, height);

		pos = new Vector(x, y);

		dir = new Vector(speed * Math.cos(Math.toRadians(theta)), -speed * Math.sin(Math.toRadians(theta)));

		if (isEnemy)
			this.img = AUtils.getRotatedImage(img, 270 - theta);
		else
			this.img = AUtils.getRotatedImage(img, 90 - theta);
		System.out.println(dir);
	}

	/**
	 * novice ship's bullet, exists only for convenience in constructing novice
	 * ships
	 * 
	 * @param e
	 */
	public Bullet(EnemyShip e) { // NOVICE ENEMY'S DEFAULT BULLET
		try {
			img = ImageIO.read(SSGameLoop.class.getResource("/shipshooter/bullets/novicebullet.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);
		speed = 20f / 60f;
		damage = 10;
		int width = img.getWidth();
		int height = img.getHeight();

		int x = (int) (e.getHb().getCenterX() - width / 2);
		int y = (int) e.getHb().getMaxY();

		hb = new Rectangle(x, y, width, height);

		pos = new Vector(x, y);
		dir = new Vector(0, speed);
	}
	public boolean isHarmless;
	public boolean shouldBeRemoved;

	/**
	 * different than other tick() methods because a player's bullet moves
	 * upward, while enemy's bullet moves downward
	 * 
	 * HOWEVER: TODO: BULLETS SHOULD BE MOVED BY VECTORS, SO IN THE END IT
	 * SHOULDNT MATTER!!
	 * 
	 * @param dt
	 */
	public void tick(double dt) { // TODO Vector direction
		// System.out.println("pos: "+pos);
		// System.out.println("dir: "+dir);
		if (!shouldBeRemoved) {
			pos = pos.addTo(dir.scale(dt));
			hb.x = (int) pos.x;
			hb.y = (int) pos.y;
		}
		// if(isPlayer)
		// getHb().y -= getSpeed()*dt;
		// else
		// getHb().y += getSpeed()*dt;
	}

	public void render(Graphics2D g) {
		if (!shouldBeRemoved) {
			g.drawImage(img, (int) pos.x, (int) pos.y, null);

			g.drawImage(img, 50, 50, null);

			if (SSGameLoop.hbIsOn)
				g.draw(hb);
		}
	}

	public Rectangle getHb() {
		return hb;
	}

	public void setHb(Rectangle hb) {
		this.hb = hb;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
