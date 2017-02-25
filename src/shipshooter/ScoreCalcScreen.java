package shipshooter;

import java.awt.Font;
import java.awt.Graphics2D;

import main.Main;
import utils.AUtils;

public class ScoreCalcScreen {
	double delay1;
	double totaldt;

	public ScoreCalcScreen() {

		delay1 = 800;
	}

	/**
	 * represents what score calc is being displayed
	 */
	private int phase;

	public void tick(double dt) {
		totaldt += dt;

		if (totaldt >= delay1) {
			phase++;
			totaldt -= delay1;
			switch (phase) {
			case 1:
				SSGameLoop.p.score += timeToScore();
				SSGameLoop.p.money += timeToScore();
				break;
			case 2:
				SSGameLoop.p.score += remainingHpToScore();
				SSGameLoop.p.money += remainingHpToScore();
				break;
			}
		}

	}

	public void render(Graphics2D g) {
		Font oldFont = g.getFont();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		String str;

		switch (phase) { // a legit switch statement??????????????? (without
							// breaks b/c it should flood in)
		case 7:
			phase = 0;
			SSGameLoop.p.setHp(SSGameLoop.p.getMaxhp());
			SSGameLoop.state = GameState.SHOP;
			break;

		case 4:
		case 5:
		case 6:
			str = "Health Restored!";
			SSGameLoop.p.setHp(SSGameLoop.p.getMaxhp());
			g.drawString(str, Main.FRAME_WIDTH / 2 - AUtils.getStringWidth(str, g) / 2, 200);
		case 3:
			str = "Total bonus: " + (remainingHpToScore() + timeToScore());
			g.drawString(str, Main.FRAME_WIDTH / 2 - AUtils.getStringWidth(str, g) / 2, 500);
		case 2:
			str = "Remaining Health bonus: " + remainingHpToScore();
			g.drawString(str, Main.FRAME_WIDTH / 2 - AUtils.getStringWidth(str, g) / 2, 400);
		case 1:
			str = "Time bonus: " + timeToScore();
			g.drawString(str, Main.FRAME_WIDTH / 2 - AUtils.getStringWidth(str, g) / 2, 300);
		case 0:
			str = "Stage clear!";
			g.drawString(str, Main.FRAME_WIDTH / 2 - AUtils.getStringWidth(str, g) / 2, 100);
		}

		g.setFont(oldFont);
	}

	int remainingHpToScore() {
		double percent = (SSGameLoop.p.getHp() / SSGameLoop.p.getMaxhp())*100;

		if (percent == 100)
			return 1000 * SSGameLoop.stage;
		else if (percent >= 90)
			return 600 * SSGameLoop.stage;
		else if (percent >= 75)
			return 300 * SSGameLoop.stage;
		else if (percent >= 60)
			return 200 * SSGameLoop.stage;
		else if (percent >= 45)
			return 100 * SSGameLoop.stage;
		else if (percent >= 25)
			return 50 * SSGameLoop.stage;
		else
			return 10 * SSGameLoop.stage;
	}

	// int accuracyToScore(){
	// double hit = SSGameLoop.p.hitShots;
	// double total = SSGameLoop.p.totalShots;
	// double acc = hit/total;
	//
	// if (acc > .8)
	// return 500 * SSGameLoop.stage;
	// else if (acc > .7)
	// return 400 * SSGameLoop.stage;
	// else if (acc > .6)
	// return 300 * SSGameLoop.stage;
	// else if (acc > .5)
	// return 200 * SSGameLoop.stage;
	// else if (acc > .35)
	// return 100 * SSGameLoop.stage;
	// else if (acc > .2)
	// return 50 * SSGameLoop.stage;
	// else
	// return 0;
	// }
	int timeToScore() {
		int totalsec = SSGameLoop.stageSeconds + SSGameLoop.stageMinutes * 60;
		if (totalsec <= 60)
			return 500 * SSGameLoop.stage;
		else if (totalsec <= 80)
			return 400 * SSGameLoop.stage;
		else if (totalsec <= 100)
			return 300 * SSGameLoop.stage;
		else if (totalsec <= 120)
			return 200 * SSGameLoop.stage;
		else if (totalsec <= 150)
			return 100 * SSGameLoop.stage;
		else if (totalsec <= 180)
			return 50 * SSGameLoop.stage;
		else
			return 0;
	}
}
