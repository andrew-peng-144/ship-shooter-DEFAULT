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

public class Level1Boss extends EnemyShip {

	public Level1Boss() {
		super();

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bossship1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2,  MIN_SPACE_FROM_TOP); // middle

		setHb(new Rectangle((int)pos.x, (int)pos.y, width, height));

		this.speed = 15f / 60f;
		this.hp = 250;
		maxHp = hp;
		setScoreValue(300);
		
		bSpeed = 30f/60f;
		bDamage = 20;
		
		eBullets = new ArrayList<>();

		upperBound = 0;
		lowerBound = Main.FRAME_HEIGHT - 300;
		rightBound = Main.FRAME_WIDTH;
		leftBound = 0;
		
		AIcooldown = 450;
	}
@Deprecated
	public Level1Boss(float speed, int hp, int scoreValue) {
		super(speed, hp, scoreValue);

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bossship1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = img.getWidth() * 4;
		int height = img.getHeight() * 4;
		int x = Main.FRAME_WIDTH / 2 - width / 2;
		int y = 100;

		setHb(new Rectangle(x, y, width, height));

		this.speed = speed;
		this.hp = hp;
		this.scoreValue = scoreValue;

		eBullets = new ArrayList<>();

		upperBound = 0;
		lowerBound = Main.FRAME_HEIGHT - 300;
		rightBound = Main.FRAME_WIDTH - getHb().width;
		leftBound = 0;
	}

	@Override
	public void execDefaultAI() {

		// if distance between x values of enemy and player is less than 50
		// pixels

		if (Math.abs(this.hb.x - SSGameLoop.p.getHb().x) <= 50)
			shoot();

		if (new Random().nextInt(10) == 0 && !isAngry)
			isAngry = true;

		if (totalAngryTime >= 3000) {
			isAngry = false;
			totalAngryTime = 0;
		}

		if (isAngry) {
			bSpeed = 45f/60f;
			bDamage = 30;
			AIcooldown = 300;
			this.speed = 30f/60f;
			angryShoot();
			try {
				img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bossship1ANGRY.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else { //IF NOT ANGRY
			bSpeed = 30f/60f;
			bDamage = 20;
			AIcooldown = 450;
			this.speed = 15f/60f;
			try {
				img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bossship1.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		move();
	}

	public boolean isAngry = false;

	@Override
	public void shoot() {
		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/boss1bullet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		eBullets.add(new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(),
				(int) (hb.getCenterX() - bimg.getWidth() / 2), (int) hb.getMaxY(), bimg, true));
	}

	public void angryShoot() {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/boss1bulletANGRY.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		eBullets.add(new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(),
				(int) (hb.getCenterX() - bimg.getWidth() / 2), (int) hb.getMaxY(), bimg, true));

	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

		left = false;
		right = false;

		if (hb.x <= leftBound)
			right = true;
		else if (hb.getMaxX() >= rightBound)
			left = true;
		else if (new Random().nextInt(10) == 0)
			right = true;
		else if (new Random().nextInt(10) == 0)
			left = true;
		else if (SSGameLoop.p.getHb().x > this.hb.x)
			right = true;
		else if (SSGameLoop.p.getHb().x < this.hb.x)
			left = true;

	}

	private double totaldt;

	
	private double totalAngryTime;

	@Override
	public void tick(double dt) {
		super.tick(dt);

		totaldt += dt;

		if (isAngry)
			totalAngryTime += dt;

		if (totaldt >= AIcooldown) {
			execDefaultAI();
			totaldt -= AIcooldown;
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		super.render(g2d);
		for (Bullet b : eBullets)
			b.render(g2d);

		g2d.drawImage(img, hb.x, hb.y, hb.width, hb.height, null);
	}
}
