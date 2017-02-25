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
 * shoots 3 bullets, one 240 deg, one 270 deg, one 300 deg
 * @author ADNREW
 *
 */
public class TripleShooter extends EnemyShip{
	public static final float SPEED_DEFAULT = 20f / 60f;
	public static final int HP_DEFAULT = 120;
	public static final int SCORE_VALUE_DEFAULT = 225;
	
	public TripleShooter(){

		speed = SPEED_DEFAULT;
		hp = HP_DEFAULT;
		maxHp = hp;
		scoreValue = SCORE_VALUE_DEFAULT;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/missingimg.png"));
		} catch (IOException e) {}
		img = AUtils.getResizedImage(img, img.getWidth() * 4, img.getHeight() * 4);
		int width = img.getWidth();
		int height = img.getHeight();
		pos = new Vector(Main.FRAME_WIDTH / 2 - width / 2, MIN_SPACE_FROM_TOP); // middle
		setHb(new Rectangle((int) pos.x, (int) pos.y, width, height));
		eBullets = new ArrayList<>();
		bSpeed = 20f / 60f;
		bDamage = 20;
		
		AIcooldown = 1000;
	}
	public void tick(double dt){
		super.tick(dt);
		totaldt += dt;
		
		if(totaldt >= AIcooldown){
			execDefaultAI();
			totaldt -= AIcooldown;
		}
	}
	public void render(Graphics2D g){
		super.render(g);
	}
	@Override
	public void execDefaultAI() {
		shoot();
		move();
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub
		Bullet b;

		BufferedImage bimg = null;

		try {
			bimg = ImageIO.read(getClass().getResourceAsStream("/shipshooter/bullets/rotatable1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bimg = AUtils.getResizedImage(bimg, bimg.getWidth() * 4, bimg.getHeight() * 4);
		
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.getCenterX()-bimg.getWidth()/2, hb.getMaxY(), bimg, 240, true);
		eBullets.add(b);
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.getCenterX()-bimg.getWidth()/2, hb.getMaxY(), bimg, 270, true);
		eBullets.add(b);
		b = new Bullet(bSpeed, bDamage, bimg.getWidth(), bimg.getHeight(), hb.getCenterX()-bimg.getWidth()/2, hb.getMaxY(), bimg, 300, true);
		eBullets.add(b);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
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
