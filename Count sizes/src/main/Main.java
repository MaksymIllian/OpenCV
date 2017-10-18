package main;

import java.io.IOException;









import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.video.BackgroundSubtractorMOG;
import org.opencv.video.BackgroundSubtractorMOG2;

import ui.DrawJPanel;
import ui.ButtonFrame;
import ui.JStatisticFrame;
import core.CameraCalibration;
import core.CoreLogic;
import core.DepthMap;
import core.ParametersCalculation;
import static ui.JSlidersClasses.*;

//@author:mesutpiskin

public class Main extends Thread {

	public static ButtonFrame frm = new ButtonFrame("Buttons Demo");
	
	public static JStatisticFrame jsf = new JStatisticFrame("Statistic");
	@Override
	public void run() {
		
		frm.setLocation(800, 0);
	    frm.setSize( 190, 225 );     
	    frm.setVisible( true ); 
	    
	    jsf.setLocation(990, 0);
	    jsf.setSize( 60, 700 );     
	    //jsf.setVisible( true ); 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		if(initVideoCapture()==true) {
		//	CameraCalibration cb = new CameraCalibration(new Size(6, 3));
			try {
				ParametersCalculation.Read(System.getProperty("user.dir")+"\\ParametersCalculation.txt");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CoreLogic.videoCaptureLeft.retrieve(CoreLogic.leftFrame);
			CoreLogic.videoCaptureRight.retrieve(CoreLogic.rightFrame);
			
			// videoCaptureRight.retrieve(rightFrame);
			CoreLogic.leftFrame = Highgui.imread("baba_left.png");
			CoreLogic.rightFrame = Highgui.imread("baba_right.png");
			CoreLogic.disparity = new Mat();
			frm.djes.drawLeftPanel = new DrawJPanel(
					CoreLogic.toBufferedImage(CoreLogic.leftFrame));
			frm.djes.drawRightPanel = new DrawJPanel(
					CoreLogic.toBufferedImage(CoreLogic.rightFrame));
			
			
			//DrawJElements djes = new DrawJElements();
			frm.djes.initDrawJ();
			frm.djes.setLocation();
			CoreLogic.mog = new BackgroundSubtractorMOG(jssm.gethistoryI(),
					jssm.getnmixturesI(), jssm.getbackgroundRatioD(),
					jssm.getnoiseSigma()); // СЌС‚Рѕ С‚РѕР¶Рµ РІ
											// РёРЅС‚РµСЂС„РµР№СЃ
			CoreLogic.mog2 = new BackgroundSubtractorMOG2(jssm.gethistoryI(),
					jssm.getvarThreshold(), jssm.getbShadowDetectionB());// СЌС‚Рѕ
																		// С‚РѕР¶Рµ
																		// РІ
																		// РёРЅС‚РµСЂС„РµР№СЃ

			CoreLogic.JFrameInit(frm.djes.jLeftFrame, frm.djes.drawLeftPanel, CoreLogic.leftFrame, "left");

			CoreLogic.JFrameInit(frm.djes.jRightFrame, frm.djes.drawRightPanel, CoreLogic.rightFrame, "right");
			
			
			
			CoreLogic.JFrameInitWithJSladers(frm.djes.jPanelFrameDisparityFirstPart, jssd1.getjSladers(), frm.djes.drawPanelFrameDisparityFirstPart,
					"panels Disparity 1");
			CoreLogic.JFrameInitWithJSladers(frm.djes.jPanelFrameDisparitySecondPart, jssd2.getjSladers(), frm.djes.drawPanelFrameDisparitySecondPart,
					"panels Disparity 2");
			CoreLogic.JFrameInitWithJSladers(frm.djes.jPanelFrameMog, jssm.getjSladers(), frm.djes.drawPanelFrameMog,
					"panels Mog");
			CoreLogic.JFrameInitWithJSladers(frm.djes.jPanelFrameFilter, jssf.getjSladers(), frm.djes.drawPanelFrameFilter,
					"panels Filter");
			System.out.println(CoreLogic.videoCaptureLeft.toString() + "");
			System.out.println(CoreLogic.videoCaptureRight.toString() + "");
			//double calibrationError = 1;
			// int successCounter = 0;
			/*
			while(calibrationError>=0.5) {
				CoreLogic.videoCaptureLeft.retrieve(CoreLogic.leftFrame);
				CoreLogic.videoCaptureRight.retrieve(CoreLogic.rightFrame);
				calibrationError=CoreLogic.Calibration(CoreLogic.leftFrame, CoreLogic.rightFrame, CoreLogic.cb);
				drawLeftPanel.changeImage(CoreLogic.toBufferedImage(CoreLogic.leftFrame));
				drawRightPanel.changeImage(CoreLogic.toBufferedImage(CoreLogic.rightFrame)); //
			}
			System.out.println(CoreLogic.cb.getQ());
			System.out.println("Q " + CoreLogic.cb.getQ().dump());*/
			/*for(int i =0;i<CoreLogic.cb.getQ().rows();i++)
			{
				for(int j =0;i<CoreLogic.cb.getQ().cols();j++)
				{
					System.out.println(CoreLogic.cb.getQ().get(i, j));
				}
			}*/
			CoreLogic.disparity = CoreLogic.dM.startSGBM(CoreLogic.leftFrame, CoreLogic.rightFrame,
					jssd1.getminDisparity(), jssd1.getnumDisparities(),
					jssd1.getSADWindowSize(), jssd1.getpreFilterCap(),
					jssd1.getuniquenessRatio(), jssd1.getdisp12MaxDiff(),
					jssd2.getP1(), jssd2.getP2(), jssd2.getspeckleRange(),
					jssd2.getspeckleWindowSize());
			// videoCaptureLeft.retrieve(disparity);
			frm.djes.drawDisparityPanel = new DrawJPanel(
					CoreLogic.toBufferedImage(CoreLogic.disparity));
			//JFrame jDisparityFrame = new JFrame("Video Capture 3");
			CoreLogic.JFrameInit(frm.djes.jDisparityFrame, frm.djes.drawDisparityPanel, CoreLogic.disparity,
					"disparity");
			CoreLogic.runBlob.setmRgba(CoreLogic.disparity);
			
			CoreLogic.videoCalibCaptureLeft.grab(); CoreLogic.videoCalibCaptureRight.grab();
			CoreLogic.videoCalibCaptureLeft.retrieve(CoreLogic.leftCalibFrame);
			CoreLogic.videoCalibCaptureRight.retrieve(CoreLogic.rightCalibFrame);
			Main.frm.djes.drawCalibLeftPanel = new DrawJPanel(
					CoreLogic.toBufferedImage(CoreLogic.leftCalibFrame));
			Main.frm.djes.drawCalibRightPanel = new DrawJPanel(
					CoreLogic.toBufferedImage(CoreLogic.rightCalibFrame));
			CoreLogic.JFrameInit(frm.djes.jCalibLeftFrame, frm.djes.drawCalibLeftPanel, CoreLogic.leftCalibFrame, "left");

			CoreLogic.JFrameInit(frm.djes.jCalibRightFrame, frm.djes.drawCalibRightPanel, CoreLogic.rightCalibFrame, "right");
			
//			CoreLogic.rC = new runCalibration();
//			CoreLogic.rC.run();
			//CoreLogic.rC = new runCalibration();
			//CoreLogic.rC.start();
//			CoreLogic.om = new OutputMain();
//			CoreLogic.om.start();
			
		}
	}

	public static Main m;

	public static void main(String[] args) throws InterruptedException, IOException {
		//CoreLogic.stage = false;
		m = new Main();
		m.start();
	}

	public static boolean initVideoCapture()
	{
		return initVideoCapture(0,0);
	}
	
	public static boolean initVideoCapture(int cam0,int cam1)
	{
		boolean isOpen = false;
		CoreLogic.videoCaptureLeft = new VideoCapture();
		CoreLogic.videoCaptureLeft.open(cam0);
		CoreLogic.videoCaptureRight = new VideoCapture();
		CoreLogic.videoCaptureRight.open(cam1);
		
		CoreLogic.videoCalibCaptureLeft = new VideoCapture();
		CoreLogic.videoCalibCaptureLeft.open(cam0);
		CoreLogic.videoCalibCaptureRight = new VideoCapture();
		CoreLogic.videoCalibCaptureRight.open(cam1);
		
		CoreLogic.outFrameLeft = new Mat();
		CoreLogic.leftFrame = new Mat();
		CoreLogic.leftCalibFrame = new Mat();
		CoreLogic.outFrameRight = new Mat();
		CoreLogic.rightFrame = new Mat();
		CoreLogic.rightCalibFrame = new Mat();
		CoreLogic.background = new Mat();
		CoreLogic.dM = new DepthMap();
		CoreLogic._3DImage = new Mat();
		CoreLogic.cb = new CameraCalibration(new Size(6, 3));

		CoreLogic.inputGray = new Mat();
		CoreLogic.foreground = new Mat();
		CoreLogic.accumulatedBackground = new Mat();
		if (!CoreLogic.videoCaptureLeft.isOpened()) {
			System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 1"); CoreLogic.videoCaptureLeft.release();
			if (!CoreLogic.videoCaptureRight.isOpened()) {
				System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 2"); CoreLogic.videoCaptureRight.release();}
		} else if (!CoreLogic.videoCaptureRight.isOpened()) {
			System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 2"); CoreLogic.videoCaptureRight.release();
		} else {
			isOpen= true;
		}
		
		if (!CoreLogic.videoCalibCaptureLeft.isOpened()) {
			System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 1"); CoreLogic.videoCalibCaptureLeft.release();
			if (!CoreLogic.videoCalibCaptureRight.isOpened()) {
				System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 2"); CoreLogic.videoCalibCaptureRight.release();}
		} else if (!CoreLogic.videoCalibCaptureRight.isOpened()) {
			System.out.println("РЅРµС‚ РєР°РјРµСЂС‹ 2"); CoreLogic.videoCalibCaptureRight.release();
		} else {
			isOpen= true;
		}
		
		return isOpen;
	}

}