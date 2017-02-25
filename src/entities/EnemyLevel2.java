package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
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
 * harder AI than NoviceShip
 * 
 * @author glomarch
 *
 */
public class EnemyLevel2 extends EnemyShip {

	public static final float SPEED_DEFAULT = 15f / 60f;
	public static final int HP_DEFAULT = 60;
	public static final int SCORE_VALUE_DEFAULT = 80;

	public EnemyLevel2() {
		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT;
		scoreValue = SCORE_VALUE_DEFAULT;
		maxHp = hp;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/shiplvl2.png"));
		} catch (IOException e1) {
		}

		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2,  MIN_SPACE_FROM_TOP); // middle

		setHb(new Rectangle((int)pos.x, (int)pos.y, width, height));
		eBullets = new ArrayList<>();

//		//upperBound = 0;
//		lowerBound = Main.FRAME_HEIGHT - 300;
//		rightBound = Main.FRAME_WIDTH;
//		leftBound = 0;
	}

	public EnemyLevel2(float speed, int hp, int scoreValue) {
		super(speed, hp, scoreValue);
		// TODO Auto-generated constructor stub

		// try {
		// img = ImageIO.read(new File("res/poheadship.png"));
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/shiplvl2.png"));
		} catch (IOException e1) {

			// }
			// e.printStackTrace();
		}

		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2,  MIN_SPACE_FROM_TOP); // middle

		setHb(new Rectangle((int)pos.x, (int)pos.y, width, height));
		eBullets = new ArrayList<>();

		upperBound = 0;
		lowerBound = Main.FRAME_HEIGHT - 300;
		rightBound = Main.FRAME_WIDTH - getHb().width;
		leftBound = 0;

	}

	@Override
	public void execDefaultAI() {
		// TODO Auto-generated method stub

		// if distance between x values of enemy and player is less than 60
		// pixels
		if (Math.abs(this.hb.x - SSGameLoop.p.getHb().x) <= 60)
			shoot();

		move();

	}

	@Override
	public void tick(double dt) {
		super.tick(dt);
		totaldt += dt;
		if (totaldt >= 600) {
			execDefaultAI();
			totaldt = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
	}

	@Override
	public void shoot() {
		// Random r = new Random();
		Bullet b;

		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/lvl2bullet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		b = new Bullet(30f / 60f, 15, bimg.getWidth(), bimg.getHeight(), (int) (hb.getCenterX() - bimg.getWidth() / 2),
				(int) hb.getMaxY(), bimg, true);

		eBullets.add(b);
		// System.out.println(eBullets);
		// System.out.println("PEW");

	}

	@Override
	public void move() {
		Random r = new Random();

		int a = r.nextInt(10);

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
