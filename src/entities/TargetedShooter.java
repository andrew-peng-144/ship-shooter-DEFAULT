package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import entities_super.EnemyShip;
import main.Main;
import shipshooter.SSGameLoop;
import utils.AUtils;
import utils.Vector;

/**
 * AIMS AT THE PLAYER THEN SHOOTS
 * 
 * @author ANDREW
 *
 */
public class TargetedShooter extends EnemyShip {

	public static final float SPEED_DEFAULT = 10f / 60f;
	public static final int HP_DEFAULT = 100;
	public static final int SCORE_VALUE_DEFAULT = 150;

	public TargetedShooter() {
		super();

		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT;
		maxHp = hp;
		scoreValue = SCORE_VALUE_DEFAULT;

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/targetingsaucer.png"));
		} catch (IOException e) {
		}

		img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);

		int width = img.getWidth();
		int height = img.getHeight();
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2, MIN_SPACE_FROM_TOP); // middle

		setHb(new Rectangle((int) pos.x, (int) pos.y, width, height));
		eBullets = new ArrayList<>();

//		upperBound = 30;
//		lowerBound = Main.FRAME_HEIGHT - 300;
//		rightBound = Main.FRAME_WIDTH;
//		leftBound = 0;

		bSpeed = 30f / 60f;
		bDamage = 50;
	}

	public void tick(double dt) {
		super.tick(dt);
		totaldt += dt;
		if (totaldt >= 2000) {
			execDefaultAI();
			totaldt = 0;
		}
	}

	public void render(Graphics2D g) {
		super.render(g);
		//g.drawLine();
	}

	@Override
	public void execDefaultAI() {
		// TODO Auto-generated method stub
		shoot();
		move();
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub
		Bullet b;

		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/ball2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
//VECTOR between gunpoint of enemyship and center of player's hitbox
		
		double theta;
		double x1 = this.hb.getCenterX();
		double x2 = SSGameLoop.p.getHb().getCenterX();
		double y1 = this.hb.getMaxY();
		double y2 = SSGameLoop.p.getHb().getCenterY();
		System.out.println("x1: "+x1);
		System.out.println("x2: "+x2);
		if(x2 > x1){
			theta = Math.toDegrees(new Vector(x2-x1,-(y2-y1)).getAngle());
			System.out.println("TEST1");
		}
		else{
			theta = Math.toDegrees(new Vector(-(x2-x1),y2-y1).getAngle())+180;
			System.out.println("TEST2");
		}
		
		System.out.println(theta);
		
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.getCenterX() - bimg.getWidth() / 2,
				hb.getMaxY(), bimg, theta);
		eBullets.add(b);
	}

	@Override
	public void move() {
		// TODO same as enemylevel2 so far
		Random r = new Random();

		int a = r.nextInt(10);

		up = false;
		down = false;
		left = false;
		right = false;

		if (SSGameLoop.p.getHb().x > this.hb.x && (a == 0 || a == 1 || a == 2 || a == 3))
			right = true;
		else if (SSGameLoop.p.getHb().x < this.hb.x && (a == 4 || a == 5 || a == 6 || a == 7))
			left = true;
		else if (a == 8)
			up = true;
		else // if a== 9
			down = true;
	}

}
