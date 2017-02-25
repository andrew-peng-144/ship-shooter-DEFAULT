package shipshooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import shopstuff.AMouseMotionListener;
//import listeners.AMouseMotionListener;
import utils.AUtils;

public abstract class ActionButton {

	public ActionButton(int x, int y, BufferedImage img, BufferedImage hoverImg, String desc) {
		clickArea = new Rectangle(x,y,img.getWidth(),img.getHeight());
		this.img = img;
		this.hoverImg = hoverImg;
		this.desc = desc;
	}

	public String desc;
	
	public int price;
	public boolean canAfford;
	public float inc;
	public int level;
	public int levelcap;
	public Rectangle clickArea;
	public BufferedImage img;
	public BufferedImage hoverImg;
	
	public boolean clicked;
	public boolean hovered;
	
	/**
	 * this method HAS to be overrided & invoked in order to have a functional button
	 */
	public void exec(){
		clicked = false;
		//
	}
	
	public void tick(double dt) {
		if(clickArea.contains(AMouseMotionListener.mousepos))
			hovered = true;
		else
			hovered = false;
		
		if(clicked)
			exec();
	}

	/**
	 * the superclass render just fills the button and write the msg in black
	 * onto it in the middle
	 * 
	 * @param g2d
	 */
	public void render(Graphics2D g2d) {
		//System.out.println(hovered);
		Color oldColor = g2d.getColor();
		Font oldFont = g2d.getFont();
		//System.out.println("taeset");
		if(hovered)
			g2d.drawImage(hoverImg, clickArea.x, clickArea.y, null);
		else
			g2d.drawImage(img, clickArea.x, clickArea.y, null);
		
		g2d.setFont(oldFont);
		g2d.setColor(oldColor);
	}
}
