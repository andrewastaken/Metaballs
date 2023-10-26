package math;

public class Ball {
    public int x, y;
    public int xSpeed, ySpeed;

    public Ball() {
        x = y = 0;
        xSpeed = ySpeed = 0;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
    }
}
