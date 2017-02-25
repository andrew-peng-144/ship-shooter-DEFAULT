package main;
import java.util.Scanner;
public class MainTest {
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		Scanner sdf= new Scanner(System.in);
		System.out.println("prompt1");
		double a = s.nextDouble();
		System.out.println("prompt2");
		String b = sdf.nextLine();
		
		System.out.println(b);
		
	}
}
