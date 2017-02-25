package shipshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.DamageNumber;
import entities.DiagonalShooter;
import entities.DualShooter1;
import entities.EnemyLevel2;
import entities.Level1Boss;
import entities.NoviceEnemy;
import entities.PlayerShip;
import entities.TargetedShooter;
import entities.TripleShooter;
import entities_super.EnemyShip;
import listeners.AMouseListener;

import main.Main;
import menuscreen.HelpScreen;
import menuscreen.MenuScreen;
import shopstuff.AMouseMotionListener;
import shopstuff.ShopScreen;
import utils.AUtils;

/**
 * REPLACEMENT FOR GamePanel.class a
 * 
 * @author glomarch a
 */
public class SSGameLoop extends JPanel implements Runnable {

	public static PlayerShip p;

	public static GameState state;

	public static boolean hbIsOn;

	BufferedImage img;
	Graphics2D g2d;

	public static List<DamageNumber> dmgNums = new ArrayList<>();

	public static java.util.List<EnemyShip> eList = new ArrayList<>();
	// public static EnemyShip boss;

	static public int wave;
	static public int stage;

	public static int frames;

	/**
	 * pixels from bottom which is upper bound for player
	 */
	public static final int WALL = 270;
	/**
	 * pixels from bottom which is lower bound for player
	 */
	public static final int LOWERWALL = 150;

	static int numEnemies, numEnemiesAlive;

	public static Thread t;

	// screens
	public static MenuScreen ms;
	public static HelpScreen hs;
	public static ShopScreen ss;
	public static ScoreCalcScreen scs;

	MenuBar mb;

	public static ActionButton toMenuButton;

	// public boolean spawnedEnemies;

	public static int stageSeconds, stageMinutes;
	/**
	 * in milliseconds
	 */
	double elapsedStageTime;

	// public static ArrayList<ActionButton> inGameButtons;

	public SSGameLoop() {
		setPreferredSize(new Dimension(Main.FRAME_WIDTH, Main.FRAME_HEIGHT));
		state = GameState.MENU;
		addMouseListener(new AMouseListener());
		addMouseMotionListener(new AMouseMotionListener());
	}

	@Override
	public void addNotify() {
		super.addNotify();

		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public void init() {
		eList.clear();
		img = new BufferedImage(Main.FRAME_WIDTH, Main.FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) img.getGraphics();

		wave = 0;
		stage = 1;
		p = new PlayerShip();
		this.setBackground(Color.BLACK);

		try {
			ms = new MenuScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hs = new HelpScreen();
		ss = new ShopScreen();
		scs = new ScoreCalcScreen();

		mb = new MenuBar();
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/shipshooter/buttons/toMenuButton.png"));
		} catch (IOException e) {
		}
		img = AUtils.getResizedImage(img, img.getWidth()*4, img.getHeight()*4);
		toMenuButton = new ToMenuButton(500, 375, img, img, "");

	}

	void tick(double dt) { // tick all objects
		// System.out.println(dt);
		switch (state) {
		case MENU:
			// TODO TICK MENU
			ms.tick(dt);
			break;

		case IN_GAME:
			p.tick(dt);
			if (p.isDead)
				toMenuButton.tick(dt);
			mb.tick(dt);
			elapsedStageTime += dt;

			handleTime();

			for (int i = 0; i < eList.size(); i++)
				eList.get(i).tick(dt);

			for (int i = 0; i < dmgNums.size(); i++) {
				DamageNumber d = dmgNums.get(i);
				if (d.isVisible())
					d.tick(dt);
				else
					dmgNums.remove(d);
			}

			if (eList.size() == 0) { // if all enemies are dead
				if (isLastWave) {
					state = GameState.SHOP;
					p.setHp(p.getMaxhp());
					p.energy = 0;
				} else {
					// spawnedEnemies = false;
					nextWave();
				}
			}
			break;

		case SHOP:
			ss.tick(dt);
			break;

		case HELP:
			hs.tick(dt);
			break;


		default:

			break;

		}
	}

	private void handleTime() {

		if (elapsedStageTime >= 1000) {
			stageSeconds++;
			elapsedStageTime -= 1000;
		}

		if (stageSeconds == 60) {
			stageSeconds = 0;
			stageMinutes++;
		}
	}

	void render() { // render all objects after clearRect and before clear
		g2d.clearRect(0, 0, Main.FRAME_WIDTH, Main.FRAME_HEIGHT);

		g2d.drawString("Game State: " + GStoString(), 10, 10);
		switch (state) {
		case MENU:
			g2d.drawString("PRESS 'b' TO KILL ALL ENEMIES (GOTO NEXT WAVE)", 50, 50);
			g2d.drawString("PRESS 'm' TO GIVE YOURSELF $10", 600, 50);
			g2d.drawString("PRESS 'n' TO GIVE YOURSELF $1000", 600, 100);
			g2d.drawString("PRESS 'h' TO SHOW HITBOXES", 600, 150);
			ms.render(g2d);
			break;

		case IN_GAME:
			mb.render(g2d);

			p.render(g2d);

			for (int i = 0; !eList.isEmpty() && i < eList.size(); i++)
				eList.get(i).render(g2d);
			
			for (DamageNumber d : dmgNums)
				d.render(g2d);
			// if(boss != null)
			// boss.render(g2d);

			if (p != null && p.isDead) {
				Font original = g2d.getFont();
				g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
				g2d.drawString("YOU DIED!", 300, 300);
				g2d.setFont(original);
				toMenuButton.render(g2d);
				p.render(g2d);
				for (int i = 0; i < eList.size(); i++)
					eList.get(i).render(g2d);

				break;
			}
			break;

		case HELP: // draw all the text in the help screen
			hs.render(g2d);
			break;

		case SHOP:

			ss.render(g2d);

			break;

		default:
			break;

		}

		if (debugEnabled)
			renderDebugText(g2d);

		finalRender();
	}

	public boolean debugEnabled;

	private void renderDebugText(Graphics2D g2d) {
		g2d.drawString("# Player bullets on screen: " + p.pBullets.size(), 20, Main.FRAME_HEIGHT - 50);

		g2d.drawString("time elapsed this stage: " + stageMinutes + ":" + stageSeconds, Main.FRAME_WIDTH - 200,
				Main.FRAME_HEIGHT - 300);

		// g2d.drawString("accuracy this stage: " + p.hitShots + "/" +
		// p.totalShots, Main.FRAME_WIDTH - 200,
		// Main.FRAME_HEIGHT - 200);

		g2d.drawString("Your hp: " + p.getHp() + "/" + p.getMaxhp(), Main.FRAME_WIDTH - 200, Main.FRAME_HEIGHT - 150);
		g2d.drawString("Bullet Damage: " + p.bDamage, Main.FRAME_WIDTH - 200, Main.FRAME_HEIGHT - 130);

		g2d.drawString("Bullet Fire Rate: " + (float) (1 / (p.bFiringDelay / 1000)) + " per second",
				Main.FRAME_WIDTH - 200, Main.FRAME_HEIGHT - 110);

		String pBulletDirection = "90 deg";
		g2d.drawString("Main Bullet Velocity: " + p.bSpeed + " " + pBulletDirection, Main.FRAME_WIDTH - 200,
				Main.FRAME_HEIGHT - 90);

		g2d.drawString("Score: " + p.score, 50, 500);
		g2d.drawString("MONEY: " + p.money, 50, 520);
		g2d.drawString("Stage: " + stage, 50, 540);
		g2d.drawString("Wave: " + wave, 50, 560);
		g2d.drawString("bulletLevel: " + SSGameLoop.p.bulletLevel, 50, 580);
		g2d.drawString("# bullet shots: " + SSGameLoop.p.numBullets, 50, 600);

		if (state == GameState.IN_GAME)
			g2d.drawString("# Enemies Left: " + eList.size(), 300, 500);
	}

	/**
	 * displays the image which has all the graphics drawings
	 */
	void finalRender() {
		Graphics g2 = getGraphics();
		if (img != null) {
			g2.drawImage(img, 0, 0, null);
		}
		g2.dispose();
	}

	/**
	 * THE GAME LOOP
	 */
	@Override
	public void run() {
		init();

		double deltatime;
		long currentTime;
		long lastTime = System.currentTimeMillis();

		while (true) {
			currentTime = System.currentTimeMillis();
			deltatime = currentTime - lastTime;
			if (deltatime > 500) {
				deltatime = 500;
			}
			tick(deltatime);
			render();
			// System.out.println(deltatime);
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastTime = currentTime;
			frames++;
		}
	}

	public void reset() {
		init();

	}

	boolean isLastWave;

	private void spawnEnemies() {
		isLastWave = false;
		EnemyShip e;
		if (p != null)
			p.pBullets.clear();

		if (wave == 1) { // reset all abilities
			System.out.println("aaaaaaaaaaaa");
			p.ml.totaldt = 0;
			p.ml.readyToUse = false;
		}

		switch (stage) { // TODO should define the stages(levels) and waves in
							// separate class file
		case 1:

			switch (wave) {
			case 1:
				for (int i = 0; i < 20; i++) {
					e = new NoviceEnemy(); // DEFAULT
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 2:
				for (int i = 0; i < 2; i++) {
					e = new NoviceEnemy();
					randomizeESP(e);
					eList.add(e);
				}
				e = new EnemyLevel2();
				eList.add(e);
				break;
			case 3:
				for (int i = 0; i < 4; i++) {
					e = new EnemyLevel2();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 4:

				for (int i = 0; i < 4; i++) {
					e = new NoviceEnemy(30f / 60f, 80, 15, 0.5f, 20);
					randomizeESP(e);
					eList.add(e);
				}
				e = new DualShooter1();

				eList.add(e);

				break;
			case 5:
				isLastWave = true;
				e = new Level1Boss();
				eList.add(e);
				break;
			// case 6:
			// state = GameState.SHOP;
			// break;
			// default:
			// nextStage();
			// break;
			}
			break;

		case 2: // stage 2

			switch (wave) {
			case 1:
				for (int i = 0; i < 3; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 2:
				e = new DualShooter1();
				randomizeESP(e);
				eList.add(e);
				for (int i = 0; i < 2; i++) {
					e = new EnemyLevel2();
					randomizeESP(e);
					eList.add(e);
					e = new NoviceEnemy();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 3:
				for (int i = 0; i < 5; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 4:
				for (int i = 0; i < 4; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < 4; i++) {
					e = new EnemyLevel2();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 5:
				isLastWave = true;
				for (int i = 0; i < 3; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				e = new Level1Boss();

				eList.add(e);
				break;
			// case 6:
			// state = GameState.SHOP;
			// break;
			// default:
			// nextStage();
			// break;
			}

			break;

		case 3: // stage 3
			switch (wave) {
			case 1:
				for (int i = 0; i < 2; i++) {
					e = new TargetedShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < 5; i++) {
					e = new NoviceEnemy();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 2:
				for (int i = 0; i < 4; i++) {
					e = new TargetedShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < 5; i++) {
					e = new EnemyLevel2();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 3:
				e = new Level1Boss();
				eList.add(e);
				for (int i = 0; i < 3; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				e = new TargetedShooter();
				randomizeESP(e);
				eList.add(e);

				break;
			case 4:
				e = new TripleShooter();
				eList.add(e);
				for (int i = 0; i < 3; i++) {
					e = new DiagonalShooter();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 5:
				isLastWave = true;
				for (int i = 0; i < 4; i++) {
					e = new TargetedShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < 2; i++) {
					e = new TripleShooter();
					randomizeESP(e);
					eList.add(e);
				}
				break;
			case 6:
				state = GameState.SHOP;
				break;
			default:
				nextStage();
				break;
			}
			break;
		case 4:
			switch (wave) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				isLastWave = true;
				break;
			case 6:
				state = GameState.SHOP;
				break;
			default:
				nextStage();
			}
			break;
		default:
			// System.out.println("atest");
			switch (wave) {
			default:
				for (int i = 0; i < wave + 2; i++) {
					e = new DualShooter1();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < wave + 2; i++) {
					e = new DiagonalShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < wave + 1; i++) {
					e = new TargetedShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < wave + 1; i++) {
					e = new TripleShooter();
					randomizeESP(e);
					eList.add(e);
				}
				for (int i = 0; i < wave; i++) {
					e = new Level1Boss();
					randomizeESP(e);
					eList.add(e);
				}
			}
		}

		if (wave == 1) { // this is all the stuff that is reset after every
							// stage (at the start of every stage)
			// p.hitShots = 0;
			// p.totalShots = 0;
			stageSeconds = 0;
			stageMinutes = 0;
		}
	}

	/**
	 * Randomize enemy's spawn point
	 * 
	 * @param e
	 *            The enemy's spawn point that will be randomized
	 */

	private void randomizeESP(EnemyShip e) {
		Random r = new Random();
		e.pos.x = r.nextInt(Main.FRAME_WIDTH); // randomize the enemy's
												// spawn point
		e.pos.y = r.nextInt(e.lowerBound - e.hb.height);
	}

	private void setESP(EnemyShip e, int x, int y) {
		e.pos.x = x;
		e.pos.y = y;
	}

	// int numWaves;

	public void nextWave() {
		wave++;
		spawnEnemies();
		dmgNums.clear();
	}

	public void nextStage() {
		stage++;
		wave = 1;
		spawnEnemies();
		p.setLeft(false);
		p.setRight(false);
		p.setUp(false);
		p.setDown(false);
		p.isShooting = false;
		dmgNums.clear();
	}

	public static String GStoString() {
		switch (state) {
		case IN_GAME:
			return "In Game";
		case MENU:
			return "menu";
		case HELP:
			return "help";
		case SHOP:
			return "shop";
		case HIGH_SCORES:
			return "highscores";
		default:
			return null;
		}
	}

}
