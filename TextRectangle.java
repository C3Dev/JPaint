import java.awt.geom.*;

public class TextRectangle extends TextShape {
	
	public TextRectangle(int startX, int startY, int w, int h){
		X = startX;
		Y = startY;
		width = w;
		height = h;
		highlight(false);
		setBorder(true);
		setText("");
		theBox = new Rectangle2D.Double(X,Y,w,h);
	}
	
	@Override
	public String saveData(){
		return "TextRectangle:"+super.saveData();
	}
}
