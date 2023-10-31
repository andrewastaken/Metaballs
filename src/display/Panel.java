package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import math.Circle;
import math.Square;
import util.Table;

public class Panel extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//general screen stuff
	private final int WIDTH = 700;
	private final int HEIGHT = 500;
	
	private final int SQUARE_SIZE = 20;
	
	private final int ROWS = HEIGHT / SQUARE_SIZE;
	private final int COLUMNS = WIDTH / SQUARE_SIZE;
	
	//circles
	private final int NUM_CIRCLES = 8;
	
	private final double MAX_RADIUS = 60;
	private final double MIN_RADIUS = 20;
	
	private final double MAX_SPEED = 2;
	private final double MIN_SPEED = 1;
	
	private Circle[] circles;
	
	//squares
	private Square[] squares;

	//misc
	private Thread thread;
	
	public Panel() 
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		setBackground(Color.BLACK);
		
		generateCircles();
		generateSquares();
		
		thread = new Thread(this);
		thread.start();
	}
	
	private void generateCircles()
	{
		circles = new Circle[NUM_CIRCLES];
		
		for(int i = 0; i < circles.length; i++)
		{
			double radius = MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * Math.random();
			double x = (WIDTH - 2 * radius) * Math.random();
			double y = (HEIGHT - 2 * radius) * Math.random();
			double direction = 2 * Math.PI * Math.random(); //degrees radians
			double speed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * Math.random();	
			
			circles[i] = new Circle(x, y, radius, speed, direction);
		}
	}
	
	private void generateSquares()
	{
		squares = new Square[ROWS * COLUMNS];

		for(int row = 0; row < ROWS; row++)
		{
			for(int col = 0; col < COLUMNS; col++)
			{
				double x = SQUARE_SIZE * col;
				double y = SQUARE_SIZE * row;
				squares[COLUMNS * row + col] = new Square(x, y, SQUARE_SIZE);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		super.paintComponent(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawGrid(g);
		drawEdges(g);
		//drawCircles(g);
		
		g.dispose();
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawGrid(Graphics2D g)
	{
		g.setColor(Color.GRAY);
		
		//horizontal lines
		for(int i = 0; i <= ROWS; i++)
		{
			int y = i * SQUARE_SIZE;
			g.drawLine(0, y, WIDTH, y);
		}
		
		//vertical lines
		for(int i = 0; i <= COLUMNS; i++)
		{
			int x = i * SQUARE_SIZE;
			g.drawLine(x, 0, x, HEIGHT);
		}
		
	}
	
	private void drawEdges(Graphics2D g)
	{
		//coords + edges
		ArrayList<Point2D> vertices = new ArrayList<>();
		
		for(int row = 0; row < ROWS; row++)
		{
			for(int col = 0; col < COLUMNS; col++)	
			{
				Square square = squares[COLUMNS * row + col];
				
				String squareIndexString = "";
				
				for(Point2D vertex : square.getVertices())
				{
					double x = vertex.getX();
					double y = vertex.getY();
					squareIndexString += f(x, y) >= 1 ? "1" : "0";
				}
				
				int squareIndex = Integer.parseInt(squareIndexString, 2);
				
				for(int edgeIndex : Table.EDGE_INDICIES[squareIndex])
				{
					int index1 = Table.CORNER_INDICIES_FROM_EDGE[edgeIndex][0];
					int index2 = Table.CORNER_INDICIES_FROM_EDGE[edgeIndex][1];
					Point2D vertex1 = square.getVertices()[index1];
					Point2D vertex2 = square.getVertices()[index2];
					double v1x = vertex1.getX(); 
					double v1y = vertex1.getY();
					double v2x = vertex2.getX();
					double v2y = vertex2.getY();
					
					double midX;
					double midY;
					
					if(edgeIndex == 0 || edgeIndex == 2) //top and bottom edges
					{
						double mu = (1 - f(v2x, v2y)) / (f(v1x, v1y) - f(v2x, v2y)); 
						midX = v2x + (v1x - v2x) * mu;
						midY = vertex1.getY();
						
					}
					else //left and right edges
					{
						double mu = (1 - f(v2x, v2y)) / (f(v1x, v1y) - f(v2x, v2y)); 
						midX = vertex1.getX();
						midY = v2y + (v1y - v2y) * mu;
					}
					
					vertices.add(new Point2D.Double(midX, midY));
				}
			}
		}
		
		g.setColor(Color.GREEN);
		
		for(int i = 0; i < vertices.size() - 1; i += 2)
		{
			Point2D vertex1 = vertices.get(i);
			Point2D vertex2 = vertices.get(i + 1);
			
			if(vertex1.equals(vertex2)) continue;
			
			Line2D line = new Line2D.Double(vertex1, vertex2);
			g.draw(line);
		}
	}
	
	private double f(double x, double y)
	{
		double sum = 0;
		
		for(Circle c : circles)
		{
			double r_sqrd = 0.25 * c.getWidth() * c.getWidth();
			double x_sqrd = (x - c.getCenterX()) * (x - c.getCenterX());
			double y_sqrd = (y - c.getCenterY()) * (y - c.getCenterY());
			sum += r_sqrd / (x_sqrd + y_sqrd);
		}
		
		return sum;
	}
	
	private void drawCircles(Graphics2D g)
	{
		g.setColor(Color.RED);
		for(Circle c : circles)
			g.draw(c);
	}
	
	public void update()
	{
		for(Circle c : circles)
			c.move();
		checkCollisions();
	}
	
	private void checkCollisions()
	{
		for(Circle c : circles)
		{
			double leftX = c.getX();
			double rightX = c.getX() + c.getWidth();
			double upperY = c.getY();
			double lowerY = c.getY() + c.getHeight();
			
			if(leftX <= 0 || rightX >= WIDTH)
				c.negateXVel();
			if(upperY <= 0 || lowerY >= HEIGHT)
				c.negateYVel();
		}
	}
	
	@Override
	public void run() 
	{
		long last = System.nanoTime();	
		double ticks = 60;
		double ns = 1000000000 / ticks;
		double delta = 0;

		while(true)
		{
			long now = System.nanoTime();
			delta += (now - last) / ns;
			last = now;
			
			if(delta >= 1)
			{
				repaint();
				update();
				delta--;
			}
		}
	}
}
