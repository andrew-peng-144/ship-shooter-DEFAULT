package shopstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//import listeners.AMouseMotionListener;
import main.Main;
import shipshooter.ActionButton;
import shipshooter.SSGameLoop;
import utils.AUtils;

public class ShopButton extends ActionButton {

	ShopButtonType type; // AN ENUM TO PREVENT CREATING LIKE 20 OTHER BUTTON
							// SUBCLASSES.
	// ALSO MAKES THIS CLASS NON-ABSTRACT.

	public ShopButton(ShopButtonType type, int x, int y, BufferedImage img, BufferedImage hoverImg, String desc) {
		super(x, y, img, hoverImg, desc);
		this.type = type;
		// this.price = price;
		// this.level = level;
		
		//switch 1st price based on type
		price = 200;

	}

	@Override
	public void exec() {

		switch (type) {
		case upgradeMaxHp:
			SSGameLoop.p.setMaxhp((float) (SSGameLoop.p.getMaxhp()+30));
			SSGameLoop.p.setHp((float) ( SSGameLoop.p.getHp()+30));
			price = 200 + 150*level;
			break;
		case upgradeMissiles:
			price = 200 + 150*level;
			break;
		case upgradeShield:
			price = 200 + 150*level;
			break;
		case upgradeCannons:
			price = 200 + 150*level;
			SSGameLoop.p.levelUpCannons();
			break;
		case upgradeDefense:
			price = 200 + 150*level;
			break;
		case upgradeEnergy:
			price = 200 + 150*level;
			break;
		default:
			break;
		}
	}

	@Override
	public void tick(double dt) {
		
		if(clickArea.contains(AMouseMotionListener.mousepos))
			hovered = true;
		else
			hovered = false;
		
		if (SSGameLoop.p.money >= price) {
			canAfford = true;

			if (clicked) {
				SSGameLoop.p.money -= price;
				System.out.println("BOUGHT");
				exec();
				clicked = false;
			}
		} else {
			canAfford = false;
		}

	}

	@Override
	public void render(Graphics2D g2d) {
		Color oc = g2d.getColor();
		Font of = g2d.getFont();
		
		int pw = Main.loop.getWidth();
		int ph = Main.loop.getHeight();
		
		if (!canAfford) {
			img = AUtils.replaceColor(img, new Color(0, 255, 33).getRGB(), new Color(255, 0, 0).getRGB());
			hoverImg = AUtils.replaceColor(img, new Color(0, 255, 33).getRGB(), new Color(255, 0, 0).getRGB());
		} else {
			img = AUtils.replaceColor(img, new Color(255, 0, 0).getRGB(), new Color(0,255,33).getRGB());
			hoverImg = AUtils.replaceColor(img, new Color(255, 0, 0).getRGB(), new Color(0,255,33).getRGB());
		}
		
		if(hovered){
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font(Font.SERIF, Font.BOLD, 18));
			g2d.drawString(desc,pw/8, ph*13/16);
			g2d.drawString("Level: "+level, pw/8, ph*14/16);
			String cost = "Cost to level up: $";
			int w = AUtils.getStringWidth(cost, g2d);
			
			g2d.drawString(cost, pw/8, ph*15/16);
			
			g2d.setColor(Color.YELLOW);
			g2d.drawString(""+(int)price, pw/8 + w, ph*15/16); //show the price a yellow color
		}
		
		super.render(g2d);
		g2d.setFont(of);
		g2d.setColor(oc);
	}

	public boolean canAfford;

	public float price;
	public int level;
}
