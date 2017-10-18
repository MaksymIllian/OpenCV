package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawJElements {

	//public JPanel drawPanelFrame;
	public JPanel drawPanelFrameDisparityFirstPart;
	public JPanel drawPanelFrameDisparitySecondPart;
	public JPanel drawPanelFrameFilter;
	public JPanel drawPanelFrameMog;
	public JFrame jLeftFrame;
	public JFrame jRightFrame;
	public JFrame jPanelFrameDisparityFirstPart;
	public JFrame jPanelFrameDisparitySecondPart;
	public JFrame jPanelFrameFilter;
	public JFrame jPanelFrameMog;
	public JFrame jDisparityFrame;
	public DrawJPanel drawDisparityPanel;
	public DrawJPanel drawLeftPanel;
	public DrawJPanel drawRightPanel;
	
	public JFrame jCalibLeftFrame;
	public JFrame jCalibRightFrame;
	public DrawJPanel drawCalibLeftPanel;
	public DrawJPanel drawCalibRightPanel;
	
	public void initDrawJ()
	{
		//drawPanelFrame = new JPanel();
		drawPanelFrameDisparityFirstPart = new JPanel();
		drawPanelFrameDisparitySecondPart = new JPanel();
		drawPanelFrameFilter = new JPanel();
		drawPanelFrameMog = new JPanel();
		jLeftFrame = new JFrame("jLeftFrame");
		jRightFrame = new JFrame("jRightFrame");
		jPanelFrameDisparityFirstPart = new JFrame("jPanelFrameDisparityFirstPart");
		jPanelFrameDisparitySecondPart = new JFrame("jPanelFrameDisparitySecondPart");
		jPanelFrameFilter = new JFrame("jPanelFrameFilter");
		jPanelFrameMog = new JFrame("jPanelFrameMog");
		jDisparityFrame = new JFrame("jDisparityFrame");
		
		jCalibLeftFrame = new JFrame("jCalibLeftFrame");
		jCalibRightFrame = new JFrame("jCalibRightFrame");
		
	}


	public void setLocation() {
		// TODO Auto-generated method stub
		jPanelFrameDisparitySecondPart.setLocation(200, 0);
		jPanelFrameFilter.setLocation(400, 0);
		jPanelFrameMog.setLocation(600, 0);
		jDisparityFrame.setLocation(600, 240);
		jRightFrame.setLocation(0,240);
	}
}
