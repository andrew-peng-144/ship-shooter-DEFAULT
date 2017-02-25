package utils;
/**
 * a 2 dimensional vector made with 2 floats
 * can represent position or movement of objects, such as the player or gravity
 * 
 * TODO position of all entities and movement should be represented by vector
 */
public class Vector {
	public double x,y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}

	
	/**
	 * get resultant
	 * @param v1
	 * @param v2
	 * @return The resultant vector
	 */
	public static Vector add(Vector v1, Vector v2){
		return new Vector(v1.x+v2.x, v1.y+v2.y);
		
	}
	public Vector scale(double scalar){
		return new Vector(x*scalar, y*scalar);
	}
	public Vector addTo(Vector other){
		return new Vector(this.x+other.x, this.y+other.y);
	}
	
	public double magnitude(){
		return Math.sqrt(x*x + y*y);
	}
	/**
	 * in radians
	 * @return
	 */
	public double getAngle(){
		return Math.atan2(y,x);
	}
	
	public String toString(){
		return "<"+x+","+y+">";
	}
}
