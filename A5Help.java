import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;  // Needed for ActionEvent and ActionListener
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.print.*;
	
// This class will enable us to send our drawing to the printer.
// Code taken largely from "The Java Tutorial".  For details, see:
// http://download.oracle.com/javase/tutorial/2d/printing/gui.html
class thePrintPanel implements Printable
{
	JPanel panelToPrint;
	
	public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException
    {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
        panelToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    public thePrintPanel(JPanel p)
    {
    	panelToPrint = p;
    }
}

// Create enum type to keep track of the mode (for the ShapePanel below).  Look
// up enum types in your text for more info.  In this case, we are basically
// using identifiers in place of int values.
enum Mode {NONE,DRAW,MOVE};

public class A5Help
{
	private ShapePanel drawPanel;
	private JPanel buttonPanel;
	private JButton drawShape;
	private JLabel msg;
	private JMenuBar theBar;
	private JMenu fileMenu;
	private JMenuItem endProgram, printScene,newScene,openScene,saveScene,saveAsScene,saveAsJpg;
	private JPopupMenu popper;
	private JMenuItem moveIt,deleteIt,resizeIt,editText,toggleBorder;
	private JFrame theWindow;
	private JRadioButton rectShape;
	private JRadioButton ellipShape;
	private JRadioButton roundRectShape;
	private String fileName;
	private boolean modified;

	public A5Help()
	{
		drawPanel = new ShapePanel(600, 400);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		drawShape = new JButton("Draw");

		ButtonHandler bhandler = new ButtonHandler();
		drawShape.addActionListener(bhandler);
		rectShape = new JRadioButton("Rectangle");
		ellipShape = new JRadioButton("Ellipse");
		roundRectShape = new JRadioButton("RoundRect");
		ButtonGroup bg = new ButtonGroup();
		bg.add(rectShape);
		bg.add(ellipShape);
		bg.add(roundRectShape);
		
		rectShape.setSelected(true);
		
		buttonPanel.add(drawShape);
		msg = new JLabel("");
		buttonPanel.add(msg);
		buttonPanel.add(rectShape);
		buttonPanel.add(ellipShape);
		buttonPanel.add(roundRectShape);
		
		drawPanel.setMode(Mode.NONE);

		theWindow = new JFrame("Java Assig5 Help Program");
		Container c = theWindow.getContentPane();
		drawPanel.setBackground(Color.lightGray);
		c.add(drawPanel, BorderLayout.NORTH);
		c.add(buttonPanel, BorderLayout.SOUTH);

		// Note how the menu is created.  First we make a JMenuBar, then
		// we put a JMenu in it, then we put JMenuItems in the JMenu.  We
		// can have multiple JMenus if we like.  JMenuItems generate
		// ActionEvents, just like JButtons, so we just have to link an
		// ActionListener to them.
		theBar = new JMenuBar();
		theWindow.setJMenuBar(theBar);
		fileMenu = new JMenu("File");
		theBar.add(fileMenu);
		newScene = new JMenuItem("New");
		openScene = new JMenuItem("Open");
		saveScene = new JMenuItem("Save");
		saveAsScene = new JMenuItem("Save As");
		saveAsJpg = new JMenuItem("Save As JPG");
		printScene = new JMenuItem("Print");
		endProgram = new JMenuItem("Exit");
		fileMenu.add(newScene);
		fileMenu.add(openScene);
		fileMenu.add(saveScene);
		fileMenu.add(saveAsScene);
		fileMenu.add(saveAsJpg);
		fileMenu.add(printScene);
		fileMenu.add(endProgram);
		printScene.addActionListener(bhandler);
		endProgram.addActionListener(bhandler);
		newScene.addActionListener(bhandler);
		openScene.addActionListener(bhandler);
		saveScene.addActionListener(bhandler);
		saveAsScene.addActionListener(bhandler);
		saveAsJpg.addActionListener(bhandler);

		// JPopupMenu() also holds JMenuItems.  To see how it is actually
		// brought out, see the mouseReleased() method in the ShapePanel class
		// below.
		popper = new JPopupMenu();
		moveIt = new JMenuItem("Move");
		moveIt.addActionListener(bhandler);
		popper.add(moveIt);
		deleteIt = new JMenuItem("Delete");
		deleteIt.addActionListener(bhandler);
		popper.add(deleteIt);
		resizeIt = new JMenuItem("Resize");
		resizeIt.addActionListener(bhandler);
		popper.add(resizeIt);
		editText = new JMenuItem("Edit Text");
		editText.addActionListener(bhandler);
		popper.add(editText);
		toggleBorder = new JMenuItem("Toggle Border");
		toggleBorder.addActionListener(bhandler);
		popper.add(toggleBorder);
		
		theWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		theWindow.pack();
		theWindow.setVisible(true);
		fileName = "";
		modified = false;
	}

	public static void main(String [] args)
	{
		A5Help win = new A5Help();
	}

	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// Note how the drawShape button and moveIt menu item are handled 
			// We simply set the state in the panel so that the mouse will 
			// actually do the work.  The state needs to be set back in the mouse
			// listener.  See ShapePanel for more details
			if (e.getSource() == drawShape)
			{
				drawPanel.setMode(Mode.DRAW);
				drawPanel.unSelect();
				msg.setText("Use Mouse to Position TextBox");
				drawShape.setEnabled(false);
				drawPanel.repaint();
				modified = true;
			}
			else if (e.getSource() == moveIt)
			{
				drawPanel.setMode(Mode.MOVE);
				msg.setText("Use Mouse to Move TextBox");
				modified = true;
			}
			// Print out the JPanel
			else if (e.getSource() == printScene)
			{
				 Printable thePPanel = new thePrintPanel(drawPanel); 
			     PrinterJob job = PrinterJob.getPrinterJob();
         		 job.setPrintable(thePPanel);
         		 boolean ok = job.printDialog();
         		 if (ok) 
         		 {
             	 	try {
                  		job.print();
             		} 
             		catch (PrinterException ex) {
              	/* The job did not successfully complete */
             		}
             	 }
        	}
			else if (e.getSource() == endProgram)
			{
				saveModified();
				System.exit(0);
			}
			else if(e.getSource() == deleteIt)
			{
				drawPanel.deleteIt();
				modified = true;
			}
			else if(e.getSource() == resizeIt)
			{
				drawPanel.resizeIt();
				modified = true;
			}
			else if(e.getSource() == editText)
			{
				drawPanel.editText();
				modified = true;
			}
			else if(e.getSource() == toggleBorder)
			{
				drawPanel.toggleBorder();
				modified = true;
			}
			else if(e.getSource() == newScene)
			{
				saveModified();
				drawPanel.newScene();
				modified = false;
				fileName = "";
			}
			else if(e.getSource() == openScene)
			{
				saveModified();
				JFileChooser jfcOpen = new JFileChooser("./");
				if(jfcOpen.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					drawPanel.load(jfcOpen.getSelectedFile().getAbsolutePath());
					fileName = jfcOpen.getSelectedFile().getAbsolutePath();
				}
				modified = false;
			}
			else if(e.getSource() == saveScene){
				saveScene();
			}
			else if(e.getSource() == saveAsScene){
				fileName = "";
				saveScene();
			}
			else if(e.getSource() == saveAsJpg){
				JFileChooser jfcSave = new JFileChooser("./");
				if(jfcSave.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					drawPanel.saveAsJpg(jfcSave.getSelectedFile().getAbsolutePath());
				}
			}
		}
	}

	private void saveModified(){
		if(modified){
			if(JOptionPane.showConfirmDialog(null, "Scene modified, save it?", "Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				saveScene();
			}
		}
	}
	
	private void saveScene(){
		if(fileName.compareTo("")==0){
			JFileChooser jfcSave = new JFileChooser("./");
			if(jfcSave.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
				drawPanel.save(jfcSave.getSelectedFile().getAbsolutePath(), true);
				fileName = jfcSave.getSelectedFile().getAbsolutePath();
			}
		}else{
			drawPanel.save(fileName,false);
		}
		modified = false;
	}
	
	// Here we are extending JPanel.  This way we can use all of the
	// properties of JPanel (including generating MouseEvents) and also
	// add new instance data and methods, as shown below.  Since this is
	// an inner class, it can access instance variables from the A5Help
	// class if necessary.
	private class ShapePanel extends JPanel
	{
		// This ArrayList is used to store the shapes in the program.
		// It is specified to be of type TextBox, so objects of any class
		// that implements the TextBox interface can be stored in here.
		// See Section 8.13 in your text for more info on ArrayList.
		private ArrayList<TextBox> shapeList; 

		private TextBox newShape;
		
		// Used to store the desired width and height of the Panel.  See
		// more info in getPreferredSize() below
		private int prefwid, prefht;
		
		// Store index of the selected TextBox.  This allows the TextBox
		// to be moved and updated.
		private int selindex;
		
		// Keep track of positions where mouse is moved on the display.
		// This is used by mouse event handlers when moving the shapes.		
		private int x1, y1, x2, y2;

		private Mode mode;  // since reaction to mouse is different if we are creating
							// or moving a shape, we must keep track of the Mode.

		public ShapePanel (int pwid, int pht)
		{
			shapeList = new ArrayList<TextBox>();
			selindex = -1;
			prefwid = pwid;   // values used by getPreferredSize method below (which
			prefht = pht;     // is called implicitly).  See getPreferredSize for details
			setOpaque(true);
			setBackground(Color.lightGray);
			
			// Add a MouseListener and a MouseMotionListener to the panel so that
			// it can handle MouseEvents.  See details in inner classes below.
			addMouseListener(new MyMouseListener());
			addMouseMotionListener(new MyMover()); 

		}  // end of constructor
		
		public void resizeIt(){
			int wid = Integer.parseInt(JOptionPane.showInputDialog(
					theWindow,"Enter the new width"));
			int ht = Integer.parseInt(JOptionPane.showInputDialog(
					theWindow,"Enter the new height"));
			TextBox s = shapeList.get(selindex);
			s.resize(wid,ht);
			unSelect();
			repaint();
		}
		
		public void deleteIt(){
			shapeList.remove(selindex);
			selindex = -1;
			repaint();
		}
		
		public void editText(){
			String text = (JOptionPane.showInputDialog(
					theWindow,"Enter text"));
			
			TextBox s = shapeList.get(selindex);
			s.setText(text);
			unSelect();
			repaint();
		}
		
		public void toggleBorder(){
			TextBox s = shapeList.get(selindex);
			s.setBorder(!s.getBorder());
			unSelect();
			repaint();
		}
		
		public void save(String fileName,boolean rewrite){
			try{
				File outFile = new File(fileName);
				if(outFile.exists()&&rewrite){
					if(JOptionPane.showConfirmDialog(null, "File already exists. Rewrite it?","File exits",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
						return;
				}
				FileWriter fw = new FileWriter(outFile);
				fw.write(String.valueOf(shapeList.size())+"\n");
				for(TextBox t:shapeList){
					fw.write(t.saveData()+"\n");
				}
				fw.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		public void saveAsJpg(String fileName){
			BufferedImage img = new BufferedImage(drawPanel.getWidth(),drawPanel.getHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics2D g = img.createGraphics();
			super.paintComponent(g);
			unSelect();
			for(TextBox t:shapeList){
				t.draw(g);
			}
			try{
				ImageIO.write(img, "JPG", new File(fileName));
			}catch(Exception ex){
				ex.printStackTrace();
			}
			repaint();
		}
		
		public void load(String fileName){
			try{
				Scanner input = new Scanner(new File(fileName));
				shapeList.clear();
				selindex = -1;
				int count = Integer.parseInt(input.nextLine());
				while(input.hasNext()){
					String line = input.nextLine();
					String [] params = line.split(":");
					int x = Integer.parseInt(params[1]);
					int y = Integer.parseInt(params[2]);
					int wid = Integer.parseInt(params[3]);
					int height = Integer.parseInt(params[4]);
					String text = null;
					if(params.length>5)
						text = params[5];
					if(text == null)
						text = "";
					TextBox t = null;
					if(params[0].compareTo("TextRectangle")==0){
						t = new TextRectangle(x,y,wid,height);
					}else if(params[0].compareTo("TextEllipse")==0){
						t = new TextEllipse(x,y,wid,height);
					}else if(params[0].compareTo("TextRoundRect")==0){
						t = new TextRoundRect(x,y,wid,height);
					}
					if(t!=null){
						t.setText(text);
						shapeList.add(t);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			repaint();
		}
		
		public void newScene(){
			shapeList.clear();
			selindex=-1;
			repaint();
		}
		
		// This class is extending MouseAdapter.  MouseAdapter is a predefined
		// class that implements MouseListener in a trivial way (i.e. none of
		// the methods actually do anything).  Extending MouseAdapter allows
		// a programmer to implement only the MouseListener methods that
		// he/she needs but still satisfy the interface (recall that to
		// implement an interface one must implement ALL of the methods in the
		// interface -- in this case I do not need 3 of the 5 MouseListener
		// methods)
		private class MyMouseListener extends MouseAdapter
		{
			// This method will be called when a mouse button is pressed down
			public void mousePressed(MouseEvent e)
			{
				x1 = e.getX();  // store where mouse is when clicked
				y1 = e.getY();

				if (!e.isPopupTrigger() && mode == Mode.NONE) // left click and
				{									   // not in a special mode
					int newsel = getSelected(x1, y1);  // find shape mouse is
					if(selindex>=0)
						unSelect();
					selindex = newsel;
					repaint();						 // clicked on
				}
				else if (e.isPopupTrigger() && selindex >= 0)  // if button is
				{								               // the popup menu
					popper.show(ShapePanel.this, x1, y1);      // trigger, show
				}											   // the popup menu
			}
			
			// This method will be called when a mouse button is released
			public void mouseReleased(MouseEvent e)
			{
				if (mode == Mode.DRAW) // in DRAW mode, create the new box
				{					   // and add it to the list of shapes
					int wid = Integer.parseInt(JOptionPane.showInputDialog(
							theWindow,"Enter the width"));
					int ht = Integer.parseInt(JOptionPane.showInputDialog(
							theWindow,"Enter the height"));
					if(rectShape.isSelected())
						newShape = new TextRectangle(x1,y1,wid,ht);
					else if(ellipShape.isSelected())
						newShape = new TextEllipse(x1,y1,wid,ht);
					else if(roundRectShape.isSelected())
						newShape = new TextRoundRect(x1,y1,wid,ht);
					else
						newShape = null;
					if(newShape!=null)
						addShape(newShape);
					drawShape.setEnabled(true);  // Set interface back to
					mode = Mode.NONE;			 // "base" state
					msg.setText("");
				}
				else if (mode == Mode.MOVE) // in MOVE mode, set mode back to
				{							// to NONE and unselect shape (since
					mode = Mode.NONE;		// the move is now finished)
					unSelect();
					selindex = -1;     
					drawShape.setEnabled(true);
					msg.setText("");
					repaint();
				}
				else if (e.isPopupTrigger() && selindex >= 0) // if button is
				{							// the popup menu trigger, show the
					popper.show(ShapePanel.this, x1, y1); // popup menu
				}
			}
		}

		// the MouseMotionAdapter has the same idea as the MouseAdapter
		// above, but with only 2 methods.  The method not implemented
		// here is mouseMoved
		private class MyMover extends MouseMotionAdapter
		{
			// This method will be called when a mousebutton is pressed
			// and moved while it is being held down
			public void mouseDragged(MouseEvent e)
			{
				x2 = e.getX();   // store where mouse is now
				y2 = e.getY();

				// Note how easy moving the shapes is, since the "work"
				// is done within the shape class.  All we do
				// here is call the appropriate method.  In your program
				// you will have more than one type of object here, so
				// the polymorphic idea will be more clear.
				if (mode == Mode.MOVE)
				{
					TextBox s = shapeList.get(selindex);
					s.move(x2, y2);
				}
				repaint();	// Repaint screen to show updates
			}
		}

		// Check to see if point (x,y) is within any of the shapes.  If
		// so, select that shape and highlight it so user can see.
		// This version of getSelected() always considers the shapes from
		// beginning to end of the ArrayList.  Thus, if a shape is "under"
		// or "within" a shape that was previously created, it will not
		// be possible to select the "inner" shape.  In your assignment you
		// must redo this method so that it allows all shapes to be selected.
		// Think about how you would do this.
		private int getSelected(double x, double y)
		{                                             
			for (int i = selindex+1; i < shapeList.size(); i++)
			{
				if (shapeList.get(i).contains(x, y))
				{
					shapeList.get(i).highlight(true); 
					return i;
				}
			}
			return -1;
		}

		// Unselect the current selected shape
		public void unSelect()
		{
			if (selindex >= 0)
			{
				shapeList.get(selindex).highlight(false);
				selindex = -1;
			}
		}

		public void setMode(Mode newMode)          // set Mode
		{
			mode = newMode;
		}

		private void addShape(TextBox newshape)      // Add new shape to list
		{
			shapeList.add(newshape);
			repaint();
		}

		// This method is used by the JFrame housing this JPanel to determine
		// how much space it needs.  We set the values of prefwid and prefht
		// in the constructor, and then when the JFrame calls pack() the correct
		// amount of space is alloced for this JPanel
		public Dimension getPreferredSize()
		{
			return new Dimension(prefwid, prefht);
		}


		// This method enables the shapes to be seen.  Note the parameter,
		// which is implicitly passed.  To draw the shapes, we in turn
		// call the draw() method for each shape.
		public void paintComponent (Graphics g) 
		{
			super.paintComponent(g);         // don't forget this line!
			Graphics2D g2d = (Graphics2D) g;
			for (int i = 0; i < shapeList.size(); i++)
			{
				shapeList.get(i).draw(g2d);
			}
		}
	} // end of ShapePanel
}

