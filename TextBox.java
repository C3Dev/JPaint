import java.awt.*;
public interface TextBox					
{         
	// Draw the TextBox onto the Graphics2D context g
	public void draw(Graphics2D g);  

	// Move the shape's position from its previous location to location
	// (x,y).  This should not affect the size or the text contents of
	// the TextBox.  Note that (x,y) is simply a reference point for the
	// shape (ex: the upper left corner).
	public void move(int x, int y);
	
	// Set the shape to be highlighted or not.  This method can be used
	// to indicate that the shape has been selected.  There are different
	// ways to hightlight an object, but a simple way (for this assignment) is
	// to simply make the color red [note that if we could make the TextBox an
	// arbitrary color to begin with, this way of highlighting would not be
	// good].
	public void highlight(boolean b);
	
	// Check to see if the point (x,y) is within the shape.  This can
	// be implemented by testing to see if (x,y) is within the underlying
	// component or components of the shape.	
	public boolean contains(double x, double y);
	
	// Format the data necessary to reproduce the object in a single line of
	// text and return that value.  For this assignment the format must be:
	// ClassName:X:Y:width:height:textData
	public String saveData();
	
	// Set the text to be displayed within the TextBox
	public void setText(String newText);
	
	// Return the text that is within the TextBox
	public String getText();
	
	// Set the border to be visible (true) or invisible (false)
	public void setBorder(boolean b);
	
	// Return whether the border is visible (true) or invisible (false)
	public boolean getBorder();
	
	// Change the width and height of the TextBox to the values passed
	// in, without changing the position of the TextBox
	public void resize(int newWidth, int newHeight);
}
