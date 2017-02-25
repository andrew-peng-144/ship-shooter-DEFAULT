package entities_super;

import java.awt.Color;
import java.awt.Font;
//import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Bullet;
import entities.Missile;
import main.Main;
import shipshooter.SSGameLoop;
import utils.Vector;
import entities.DamageNumber;

//TODO AbstractShip class that serves as superclass for all ship classes, including PlayerShip
// and will have abstract methods move, shoot, and different instance variables

public abstract class EnemyShip {

	protected float hp;
	protected float maxHp;

	public List<Bullet> eBullets;

	public boolean isInvincible;

	protected static final int MIN_SPACE_FROM_TOP = 100;

	protected static int spaceBetween;

	public Rectangle hb; // HITBOX

	protected boolean up, down, left, right;

	protected int scoreValue;
	public BufferedImage img;
	protected float speed; // #pixels to move per invocation of move()

	public float bSpeed;
	public float bDamage;

	protected int upperBound;
	public int lowerBound;
	protected int rightBound;
	protected int leftBound;
	public double totaldt;

	public Vector pos;
	public Vector dir;

	public int AIcooldown;

	{ // COPIED TO ALL CONSTRUCTORS
		// System.out.println("C T A C");
		upperBound = 30;
		lowerBound = Main.FRAME_HEIGHT - 400;
		rightBound = Main.FRAME_WIDTH;
		leftBound = 0;

	}

	public EnemyShip() {

	}

	public EnemyShip(float speed, int hp, int scoreValue) {
		// TODO Auto-generated constructor stub
		this.speed = speed;
		this.hp = hp;
		this.maxHp = hp;
		this.scoreValue = scoreValue;
	}

	public EnemyShip(float speed, int hp, int scoreValue, float bSpeed, int bDamage) {
		this.speed = speed;
		this.hp = hp;
		this.maxHp = hp;
		this.scoreValue = scoreValue;
		this.bSpeed = bSpeed;
		this.bDamage = bDamage;
	}

	public abstract void execDefaultAI();

	public abstract void shoot();

	public boolean hasBeenHit;

	public void tick(double dt) {
		hb.x = (int) pos.x;
		hb.y = (int) pos.y;
		if (hp <= 0) {
			SSGameLoop.eList.remove(this);
			SSGameLoop.p.score += this.scoreValue;
			SSGameLoop.p.money += this.scoreValue;
		} else {
			// checking if player bullet hit ship
			int ipb = -1; // index of player bullet

			for (int i = 0; i < SSGameLoop.p.pBullets.size(); i++) {
				Bullet b = SSGameLoop.p.pBullets.get(i);
				// Missile m = null;
				// if (b instanceof Missile)
				// m = (Missile) b;
				// if (m != null && m.exploded) {
				// Point enemyCenter = new Point((int) this.hb.getCenterX(),
				// (int) this.hb.getCenterY());
				// Point explosionCenter = new Point((int) m.hb.getCenterX(),
				// (int) m.hb.getCenterY());
				// double dist = Math.sqrt(Math.pow(enemyCenter.x -
				// explosionCenter.x, 2)
				// + Math.pow(enemyCenter.y - explosionCenter.y, 2));
				// if (dist <= m.splashRadius)
				// ipb = i;
				//
				// } else
				if (b.getHb().intersects(this.hb)) {
					// System.out.println("TAEST123");
					ipb = i;
				}
			}

			if (ipb >= 0) {

				// System.out.println(this.hp);
				Bullet b = SSGameLoop.p.pBullets.get(ipb);

				if (!b.isHarmless) {
					hasBeenHit = true;
					this.hp -= b.getDamage();
					SSGameLoop.dmgNums.add(new DamageNumber(b.damage, 1f / 5f, Math.random() * 360,
							(int) hb.getCenterX(), (int) hb.getCenterY(), 1000,
							new Font(Font.MONOSPACED, Font.BOLD, 15), Color.ORANGE));

					if (b instanceof Missile) {
						Missile m = (Missile) b;
						if (m.exploded){
							m.explosionHitEnemy = true;
							//System.out.println("explosion hit enemy");
						}
					}
				}

				if (b instanceof Missile) {
					Missile m = (Missile) b;

					m.exploded = true; // missile explodes after hitting enemy
					//System.out.println("exploded");
				} else {
					SSGameLoop.p.pBullets.remove(ipb); // player hit the enemy
				}

				// SSGameLoop.p.hitShots++;
			}

			// mvmt checking
			if (getHb().getMaxY() >= lowerBound) {
				up = true;
				down = false;
				left = false;
				right = false;
			}
			if (getHb().y <= upperBound) {
				up = false;
				down = true;
				left = false;
				right = false;
			}
			if (getHb().x <= leftBound) {
				up = false;
				down = false;
				left = false;
				right = true;
			}
			if (getHb().getMaxX() >= rightBound) {
				up = false;
				down = false;
				left = true;
				right = false;
			}

			if (isUp())
				pos.y -= speed * dt;
			else if (isDown())
				pos.y += speed * dt;
			else if (isLeft())
				pos.x -= speed * dt;
			else if (isRight())
				pos.x += speed * dt;

			for (int i = 0; i < eBullets.size(); i++) {
				Bullet b = eBullets.get(i);
				b.tick(dt);
			}

		}
	}

	public void render(Graphics2D g) {
		Color oc = g.getColor();

		g.setColor(Color.WHITE);
		g.drawImage(img, hb.x, hb.y, hb.width, hb.height, null);
		g.drawLine(0, lowerBound, Main.FRAME_WIDTH, lowerBound);
		g.drawLine(0, upperBound, Main.FRAME_WIDTH, upperBound);

		if (SSGameLoop.hbIsOn)
			g.draw(this.hb);

		if (hasBeenHit) {
			g.setColor(Color.GREEN);
			g.fillRect((int) (this.hb.getCenterX() - 25), this.hb.y - 10, (int) (50 * (hp / maxHp)), 5);
		}

		// render bullets
		for (int i = 0; i < eBullets.size(); i++) {

			Bullet b = eBullets.get(i);
			b.getHb().y += b.getSpeed();

			if (b.getHb().y > Main.FRAME_WIDTH) { // remove enemy bullet if off
													// screen
				eBullets.remove(i);
			}

			else
				b.render(g);

		}

		g.setColor(oc);
	}

	public abstract void move();

	public void stop() {
		up = false;
		down = false;
		left = false;
		right = false;
	}
	// getters & setters below

	public Rectangle getHb() {
		return hb;
	}

	public void setHb(Rectangle hb) {
		this.hb = hb;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}
}