package menuscreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import listeners.AMouseListener;
import shipshooter.GameState;
import shipshooter.SSGameLoop;

public class HelpScreen {

	static public Rectangle backBtn;
	static public boolean backBtnClicked;
	
	public HelpScreen(){
		System.out.println("helpscreen made");
		backBtn = new Rectangle(100, 400, 200, 50);
	}
	public void tick(double dt){

		if(backBtnClicked){
			SSGameLoop.state = GameState.MENU;

			backBtnClicked = false;
		}
	}
	public void render(Graphics2D g2d){
		Font originalf = g2d.getFont();
		Color originalc = g2d.getColor();

		
		g2d.setFont(new Font("ARIAL", Font.BOLD, 40));
		g2d.drawString("HELP", 100, 100);
		
		g2d.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
		g2d.drawString("Arrow keys or WASD to move", 100, 200);
		g2d.drawString("Hold Spacebar to shoot", 100, 225);
		g2d.drawString("Press the number keys to use abilities", 100, 250);
		g2d.drawString("Defeat all enemies to advance to the next wave", 100, 275);
		g2d.drawString("Clear all waves to advance to the next stage", 100, 300);
		g2d.drawString("Run out of hp and you lose", 100, 325);
		g2d.setColor(Color.green);
		g2d.drawString("Green bar is HP. When it falls at or below 0, you automatically lose! It is regenerated after every stage.", 100, 350);
		g2d.setColor(Color.yellow);
		g2d.drawString("Yellow bar is Energy, which regenerates over time and is consumed when an ability is used", 100, 375);

		g2d.setColor(Color.RED);
		g2d.fill(backBtn);
		
		g2d.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
		g2d.setColor(Color.WHITE);
		
		FontMetrics fm = g2d.getFontMetrics();
		String back = "BACK";
		g2d.drawString(back, (int) backBtn.getCenterX() - fm.stringWidth(back)/2, (int) backBtn.getCenterY()+7);
		g2d.setFont(originalf);
		g2d.setColor(originalc);
	}
}
