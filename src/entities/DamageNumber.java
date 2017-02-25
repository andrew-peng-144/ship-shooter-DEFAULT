package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import utils.Vector;

public class DamageNumber {

	public float value;
	public float speed;
	public Vector dir;
	public Vector pos;
	/**
	 * in milliseconds, how long the damage number lasts
	 */
	public int duration;
	private Font font;
	private Color color;
	/**
	 * 
	 * @param value
	 * @param speed
	 * @param theta
	 *            IN DEGREES!
	 * @param x
	 * @param y
	 * @param duration
	 */

	public DamageNumber(float value, float speed, double theta, int x, int y, int duration, Font font, Color color) {
		setVisible(true);
		this.value = value;
		this.speed = speed;
		this.duration = duration;
		this.font = font;
		this.color = color;
		
		pos = new Vector(x, y);
		dir = new Vector(speed * Math.cos(Math.toRadians(theta)), -speed * Math.sin(Math.toRadians(theta)));
	}
	
	double totaldt;

	private boolean visible;

	public void tick(double dt) {
		pos = pos.addTo(dir.scale(dt));
		totaldt += dt;
		if (totaldt >= duration) {
			setVisible(false);
		}
	}

	public void render(Graphics2D g) {
		if (visible) {
			Color oc = g.getColor();
			Font of = g.getFont();
			
			g.setColor(color);
			g.setFont(font);
			g.drawString("" + (int) value, (int) pos.x, (int) pos.y);

			g.setColor(oc);
			g.setFont(of);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
