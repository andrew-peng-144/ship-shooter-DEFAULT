package entities;

import static utils.BetterSOP.SOP;

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

public class NoviceEnemy extends EnemyShip {

	public static final float SPEED_DEFAULT = 15f / 60f;
	public static final int HP_DEFAULT = 50;
	public static final int SCORE_VALUE_DEFAULT = 50;


	boolean isDefault;

	public NoviceEnemy() {
		super();

		isDefault = true;

		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT;
		maxHp = hp;
		scoreValue = SCORE_VALUE_DEFAULT;

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/noviceship.png"));
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

	public NoviceEnemy(float speed, int hp, int scoreValue, float bSpeed, int bDamage) {// CREATION.

		super(speed, hp, scoreValue, bSpeed, bDamage);

		isDefault = false;

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/noviceship.png"));
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

	@Override
	public void execDefaultAI() {
//		Random r = new Random();
		
		//if distance between x values of enemy and player is less than 50 pixels
		if(Math.abs(this.hb.x - SSGameLoop.p.getHb().x) <= 50)
			shoot();
		
		move();

	}

	private int totaldt;

	@Override
	public void tick(double dt) { // TODO AI SHOULD BE A FUNCTION OF DELTA TIME
		super.tick(dt);

		totaldt += dt;

		if (totaldt >= 500) {
			execDefaultAI();
			totaldt = totaldt-500;
		}

	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
		//g.drawLine(0, lowerBound, Main.FRAME_WIDTH, lowerBound);
	}

	@Override
	public void shoot() {
		Bullet b;
		if (isDefault)
			b = new Bullet(this);
		else {
			BufferedImage img = null;
			try {
				img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/novicebullet.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);
			b = new Bullet(bSpeed, bDamage, img.getWidth(), img.getHeight(),
					(int) (hb.getCenterX() - img.getWidth() / 2), (int) hb.getMaxY(), img, true);
		}
		eBullets.add(b);
		// System.out.println(eBullets);
		// System.out.println("PEW");

	}

	@Override
	public void move() {

		Random r = new Random();

		int a = r.nextInt(6);

		up = false;
		down = false;
		left = false;
		right = false;

		 if (SSGameLoop.p.getHb().x > this.hb.x && (a == 0 || a == 1) && this != null)
			right = true;
		else if (SSGameLoop.p.getHb().x < this.hb.x && (a == 2 || a == 3))
			left = true;
		else if (a == 4)
			up = true;
		else // if a== 5
			down = true;
	}
}
