import java.awt.geom.*;

public class TextRoundRect extends TextShape {
	
	public TextRoundRect(int startX, int startY, int w, int h){
		X = startX;
		Y = startY;
		width = w;
		height = h;
		highlight(false);
		setBorder(true);
		setText("");
		theBox = new RoundRectangle2D.Double(X,Y,w,h,10,10);
	}
	
	@Override
	public String saveData(){
		return "TextRoundRect:"+super.saveData();
	}
}
