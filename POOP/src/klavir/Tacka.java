package klavir;

public class Tacka {
	
	private int x,y, width, height;

	public Tacka(int xx, int yy, int w, int h) {
		x = xx;
		y = yy;
		width = w;
		height = h;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String toString() {
		return "Xcoor: " + x + "Ycoor" + y + "Height: " + height + "Width: " + width;
	}
	
	
	

}
