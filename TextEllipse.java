import java.awt.geom.*;

public class TextEllipse extends TextShape {
	
	public TextEllipse(int startX, int startY, int w, int h){
		X = startX;
		Y = startY;
		width = w;
		height = h;
		highlight(false);
		setBorder(true);
		setText("");
		theBox = new Ellipse2D.Double(X,Y,w,h);
	}
	
	@Override
	public String saveData(){
		return "TextEllipse:"+super.saveData();
	}
}
