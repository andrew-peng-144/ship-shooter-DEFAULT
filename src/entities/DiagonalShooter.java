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

public class DiagonalShooter extends EnemyShip {

	public static final float SPEED_DEFAULT = 15f / 60f;
	public static final int HP_DEFAULT = 100;
	public static final int SCORE_VALUE_DEFAULT = 150;

	public DiagonalShooter() {
		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT;
		scoreValue = SCORE_VALUE_DEFAULT;
		maxHp = hp;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/diagonalshooter.png"));
		} catch (IOException e1) {
		}

		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2,  MIN_SPACE_FROM_TOP); // middle
		
		setHb(new Rectangle((int)pos.x, (int)pos.y, width, height));
		eBullets = new ArrayList<>();

//		upperBound = 0;
//		lowerBound = Main.FRAME_HEIGHT - 300;
//		rightBound = Main.FRAME_WIDTH - getHb().width;
//		leftBound = 0;
	}

	public void tick(double dt) {
		super.tick(dt);
		totaldt += dt;
		if (totaldt >= 700) {
			execDefaultAI();
			totaldt = 0;
		}
	}

	public void render(Graphics2D g) {
		super.render(g);
	}

	@Override
	public void execDefaultAI() {
		move();
		shoot();
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub
		Bullet b;

		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/ball1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		b = new Bullet(20f / 60f, 25, bimg.getWidth(), bimg.getHeight(), (int) hb.getMinX() - bimg.getWidth(),
				(int) hb.getMaxY(), bimg, 225);
		eBullets.add(b);
		b = new Bullet(20f / 60f, 5, bimg.getWidth(), bimg.getHeight(), (int) hb.getMaxX(), (int) hb.getMaxY(), bimg,
				315);
		eBullets.add(b);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		Random r = new Random();

		int a = r.nextInt(6);

		up = false;
		down = false;
		left = false;
		right = false;

//		if (hb.getMaxY() >= lowerBound)
//			up = true;
//		else if (hb.y <= upperBound)
//			down = true;
//		else if (hb.x <= leftBound)
//			right = true;
//		else if (hb.getMaxX() >= rightBound)
//			left = true;

		if (SSGameLoop.p.getHb().x > this.hb.x && (a == 0 || a == 1))
			right = true;
		else if (SSGameLoop.p.getHb().x < this.hb.x && (a == 2 || a == 3))
			left = true;
		else if (a == 4)
			up = true;
		else // if a== 5
			down = true;

	}
}
