package shipshooter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;

public class RespawnButton {
	public BufferedImage img;
	public Rectangle btn;
	public RespawnButton(){
	try {
		img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/buttons/respawnbutton.png"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int width = img.getWidth()*4;
	int height = img.getHeight()*4;
	int x = Main.FRAME_WIDTH/2 - width/2;
	int y = Main.FRAME_HEIGHT/2 - height/2 + 100;
	btn = new Rectangle(x,y, width, height);
	}
}
