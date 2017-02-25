package main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import listeners.AKeyListener;
import listeners.AMouseListener;
import menuscreen.MenuScreen;
import shipshooter.SSGameLoop;

public class Main {

	public static SSGameLoop loop;
	
	public static MenuScreen ms;
	
	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 700;
	
	public static JFrame frame;
	
	Main(){
		frame = new JFrame();
		frame.setTitle("SHIPSHOOTER ZETA");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		loop = new SSGameLoop();

		try {
			frame.setIconImage(ImageIO.read(getClass().getResourceAsStream("/shipshooter/playership.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frame.addKeyListener(new AKeyListener());
		frame.add(loop);
		

		frame.setVisible(true);
		
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Main();
			}
		});
	}
}
