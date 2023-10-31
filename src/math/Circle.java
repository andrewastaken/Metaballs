package math;

import java.awt.geom.Ellipse2D;

public class Circle extends Ellipse2D.Double
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double speed, direction;

	public Circle(double x, double y, double radius, double speed, double direction) 
	{
		super(x, y, radius * 2, radius * 2);
		this.speed = speed;
		this.direction = direction;
	}
	
	public void move()
	{
		x += speed * Math.cos(direction);
		y += speed * Math.sin(direction);
	}
	
	public void negateXVel()
	{
		direction = Math.PI - direction;
 	}
	
	public void negateYVel()
	{
		direction = 2 * Math.PI - direction;
	}
}
