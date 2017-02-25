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

public class DualShooter1 extends EnemyShip {

	public static final float SPEED_DEFAULT = 20f / 60f;
	public static final int HP_DEFAULT = 80;
	public static final int SCORE_VALUE_DEFAULT = 110;

	public DualShooter1() {
		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT; 		maxHp = hp;
		scoreValue = SCORE_VALUE_DEFAULT;
		bDamage = 10;
		bSpeed = 50f/100f;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/dualshooter1.png"));
		} catch (IOException e1) {
		}

		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2,  MIN_SPACE_FROM_TOP); // middle

		setHb(new Rectangle((int)pos.x, (int)pos.y, width, height));
		eBullets = new ArrayList<>();

//		upperBound = 0;
		lowerBound = Main.FRAME_HEIGHT - 400;
//		rightBound = Main.FRAME_WIDTH - getHb().width;
//		leftBound = 0;
	}

	@Deprecated // WOWOWLWOLO
	public DualShooter1(float speed, int hp, int scoreValue) {
		super(speed, hp, scoreValue);
		this.speed = speed;
		this.hp = hp;
		this.scoreValue = scoreValue;

	}
	
	public DualShooter1(float speed, int hp, int scoreValue, int bSpeed, int bDamage){
		super(speed, hp, scoreValue, speed, bDamage);
	}

	@Override
	public void execDefaultAI() {
		if(Math.abs(this.hb.x - SSGameLoop.p.getHb().x) <= 100)
			shoot();
		
		move();
	}

	@Override
	public void shoot() {
		Bullet b;

		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/dualshooter1bullet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth()*4, bimg.getHeight()*4);
		
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.x, (int) hb.getMaxY(), bimg, true);
		eBullets.add(b);
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), (int) (hb.getMaxX()-12), (int) hb.getMaxY(), bimg, true);
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

	private int totaldt;

	@Override
	public void tick(double dt) {
		super.tick(dt);
		totaldt += dt;
		if (totaldt >= 500) {
			execDefaultAI();
			totaldt = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
	}

}
