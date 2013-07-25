import java.awt.*;
import java.awt.geom.*;

public abstract class TextShape implements TextBox {

	protected RectangularShape theBox;
	protected int X, Y;
	protected int width, height;
	private boolean highlighted;
	private boolean border;
	private String text;

	
	public boolean contains(double x, double y) {
		return theBox.contains(x, y);
	}

	public void draw(Graphics2D g) {
		if(highlighted)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		if(border)
			g.draw(theBox);
		
		g.drawString(text, X, Y+height/2);
	}

	public boolean getBorder() {
		return border;
	}

	public String getText() {
		return text;
	}

	public void highlight(boolean b) {
		highlighted = b;
	}

	public void move(int x, int y) {
		X = x;
		Y = y;
		theBox.setFrame(X,Y,width,height);
	}

	public void resize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
		theBox.setFrame(X,Y,width,height);
	}

	public String saveData() {
		return String.valueOf(X)+":"+String.valueOf(Y)+":"+String.valueOf(width)+":"+String.valueOf(height)+":"+text;
	}

	public void setBorder(boolean b) {
		border = b;
	}

	public void setText(String newText) {
		text = newText;
	}

}
