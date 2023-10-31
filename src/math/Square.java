package math;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Square extends Rectangle2D.Double
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Point2D[] vertices;
	
	public Square(double x, double y, double size)
	{
		super(x, y, size, size);
		vertices = new Point2D.Double[4];
		vertices[0] = new Point2D.Double(x, y); //top left
		vertices[1] = new Point2D.Double(x + size, y); //top right
		vertices[2] = new Point2D.Double(x + size, y + size); //bottom right
		vertices[3] = new Point2D.Double(x, y + size); //bottom left
	}
	
	public Point2D[] getVertices()
	{
		return vertices;
	}
}
