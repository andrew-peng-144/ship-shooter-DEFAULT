package shopstuff;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class AMouseMotionListener implements MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * The current position of the mouse.
	 */
	public static Point mousepos = new Point();
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mousepos = e.getPoint();
	}

}
