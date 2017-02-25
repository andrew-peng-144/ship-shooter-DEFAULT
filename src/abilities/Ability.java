package abilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import shipshooter.SSGameLoop;

public abstract class Ability {
	/**
	 * how long in milliseconds before able to use ability
	 */
	public double cooldown;
	/**
	 * whether the ability is enabled from shop
	 */
	// public boolean enabled;

	/**
	 * whether cooldown expires
	 */
	public boolean readyToUse;
	/**
	 * whether player has inputted
	 */
	public boolean activated;

	/**
	 * how much energy it costs to use the ability
	 */
	public int energyCost;
	
	public BufferedImage icon;

	abstract void execute();

	public Ability() {

	}

	public int iconx;
	public int icony;

	public double totaldt;

	public void tick(double dt) {

		if (!readyToUse) {
			totaldt += dt;
			if (totaldt >= cooldown) {
				totaldt = 0;
				readyToUse = true;
			}
		} else { //if its ready to use
			if (activated) {
				if(SSGameLoop.p.energy >= energyCost){
				execute();
				SSGameLoop.p.energy -= energyCost;
				readyToUse = false;
				}
			}

		}
		activated = false;
	}

	public void render(Graphics2D g) {
		Color oc = g.getColor();
		Font of = g.getFont();
		int ix = iconx;
		int iy = icony;
		g.drawImage(icon, ix, iy, null);

		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		g.drawString("1", ix + 4, iy + 14);

		g.setColor(Color.white);
		if (readyToUse) {
			g.setColor(Color.GREEN);
			g.fillRect(ix, iy + icon.getHeight() - 12, icon.getWidth(), 12);
		} else {
			g.fillRect(ix, iy + icon.getHeight() - 12, (int) (icon.getWidth() * (totaldt / cooldown)), 12);
			g.setColor(new Color(0, 0, 0, 127));
			g.fillRect(ix, iy, icon.getWidth(), icon.getHeight());
		}
		g.setColor(oc);
		g.setFont(of);
	}

}
