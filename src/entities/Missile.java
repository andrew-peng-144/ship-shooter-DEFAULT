package entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import shipshooter.SSGameLoop;
import utils.Vector;

public class Missile extends Bullet {

	public BufferedImage[] splashAnimation;
	public boolean exploded;

	public Missile(float speed, float damage, int width, int height, double x, double y, BufferedImage img,
			double theta) {
		super(speed, damage, width, height, x, y, img, theta);
		
	}

	double totaldt4splashAnimation;
	double delayBetweenFrames = 200;
	int currentAnimationFrame;

	Point missileCenter;
	public boolean explosionHitEnemy;
	
	public void tick(double dt) {
		super.tick(dt);
		if (!shouldBeRemoved) {
			if (!exploded){
				missileCenter = new Point((int) pos.x + hb.width / 2, (int) pos.y + hb.height / 2);
			}
			else { //if it exploded
				img = splashAnimation[currentAnimationFrame];
				dir = new Vector(0, 0);

				totaldt4splashAnimation += dt;

				hb = new Rectangle(missileCenter.x - img.getWidth() / 2, missileCenter.y - img.getHeight() / 2,
						img.getWidth(), img.getHeight());
				//System.out.println("hitbox change");
				
				if(explosionHitEnemy){
					isHarmless = true;
					//System.out.println("is now harmless");
				}
				
				if (totaldt4splashAnimation >= delayBetweenFrames) {
					currentAnimationFrame++;
					if (currentAnimationFrame >= splashAnimation.length) {
						System.out.println("Asdf");
						shouldBeRemoved = true;
					} else {
						img = splashAnimation[currentAnimationFrame];
						totaldt4splashAnimation -= delayBetweenFrames;
					}
				}
			}

		}
	}

	public void render(Graphics2D g) {
		// super.render(g);
		if (!shouldBeRemoved) {
			if (!exploded) {
				g.drawImage(img, (int) pos.x, (int) pos.y, null);
			} else {
				g.drawImage(img, missileCenter.x - splashAnimation[currentAnimationFrame].getWidth() / 2,
						missileCenter.y - splashAnimation[currentAnimationFrame].getHeight() / 2, null);
				// System.out.println("TESTSETEST");
			}

			if (SSGameLoop.hbIsOn && exploded)
				g.draw(hb);
		}
	}
}
