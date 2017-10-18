package main;

import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import core.CoreLogic;
import static ui.JSlidersClasses.*;

import java.io.IOException;
public class OutputMain extends Thread {
	@Override
	public void run() {              
		while (true) {
		/*	
			  CoreLogic.videoCaptureLeft.grab(); CoreLogic.videoCaptureRight.grab();
			  CoreLogic.videoCaptureLeft.retrieve(CoreLogic.leftFrame);
			  CoreLogic.videoCaptureRight.retrieve(CoreLogic.rightFrame);*/
			 /* cb.Undistort(leftFrame,
			 * rightFrame,outFrameLeft,outFrameRight);
			 */
			//System.out.println(CoreLogic.disparity);
			//CoreLogic.runBlob = new RunBlob(CoreLogic.disparity.clone());
			//System.out.println(CoreLogic.runBlob.ismIsColorSelected());
			CoreLogic.leftFrame = Highgui.imread("baba_left.png");
			CoreLogic.rightFrame = Highgui.imread("baba_right.png");
			CoreLogic.outFrameLeft = CoreLogic.leftFrame.clone();
			CoreLogic.outFrameRight = CoreLogic.rightFrame.clone();
			Imgproc.cvtColor(CoreLogic.outFrameLeft, CoreLogic.outFrameLeft,
					Imgproc.COLOR_BGR2GRAY);
			Imgproc.cvtColor(CoreLogic.outFrameRight, CoreLogic.outFrameRight,
					Imgproc.COLOR_BGR2GRAY);
			// Imgproc.cvtColor(leftFrame, leftFrame,
			// Imgproc.COLOR_BGR2GRAY);
			// leftFrame = BackgroundSubstractorMOG(leftFrame);
			// rightFrame = BackgroundSubstractorMOG(rightFrame);
			// FindObj(leftFrame, outFrameLeft);
			// Imgproc.cvtColor(outFrameLeft, outFrameLeft,
			// Imgproc.COLOR_BGR2GRAY);
			CoreLogic.disparity = CoreLogic.dM.startSGBM(CoreLogic.outFrameLeft, CoreLogic.outFrameRight,
					jssd1.getminDisparity(), jssd1.getnumDisparities(),
					jssd1.getSADWindowSize(), jssd1.getpreFilterCap(),
					jssd1.getuniquenessRatio(), jssd1.getdisp12MaxDiff(),
					jssd2.getP1(), jssd2.getP2(), jssd2.getspeckleRange(),
					jssd2.getspeckleWindowSize());
			CoreLogic.rightFrame = CoreLogic.filter(CoreLogic.disparity,
					jssf.getlowerbX(), jssf.getlowerbY(),jssf.getlowerbZ(),
					jssf.getupperbX(), jssf.getupperbY(), jssf.getupperbZ());
			CoreLogic.runBlob.setmRgba(CoreLogic.rightFrame);
			// cb.Visualization3D(disparity, _3DImage);
			// disparity = BackgroundSubstractorMOG(disparity,1);
			// Imgproc.cvtColor(rightFrame, rightFrame,
			// Imgproc.COLOR_BGR2GRAY);
			// disparity = Segmentation(disparity,jss.getthresh(),
			// jss.getmaxval(), jss.getImgprocTHRESH());
			/*
			 * disparity = FindObj2(disparity, outFrameLeft,
			 * jss.getlowerbX(), jss.getlowerbY(),jss.getlowerbZ(),
			 * jss.getupperbX(), jss.getupperbY(), jss.getupperbZ());
			 */
			//CoreLogic.runBlob.setmRgba(CoreLogic.disparity);
			//CoreLogic.leftFrame = CoreLogic.runBlob.getmRgba(CoreLogic.disparity);
			//CoreLogic.runBlobCompute = new RunBlobCompute(CoreLogic.disparity, 100,200);
			//CoreLogic.runBlobCompute.mousePressed();
			//CoreLogic.runBlobCompute.getmRgba(CoreLogic.disparity);
			Point lP = CoreLogic.lightPoint(CoreLogic.disparity.submat(new Rect((int)CoreLogic.runBlob.getXMin(),
					(int)CoreLogic.runBlob.getYMin(), (int)CoreLogic.runBlob.getW(), 
					(int)CoreLogic.runBlob.getH())));
			if(lP != null)
			{
				System.out.println("light point bf x = " + lP.x + " y = " + lP.y );
				lP.x += CoreLogic.runBlob.getXMin();
				lP.y += CoreLogic.runBlob.getYMin();
				System.out.println("light point = " + lP);
				System.out.println("CoreLogic.runBlob.getXMin() = " + CoreLogic.runBlob.getXMin());
				System.out.println("CoreLogic.runBlob.getYMin() = " + CoreLogic.runBlob.getYMin());
				System.out.println("disp size = " + CoreLogic.disparity.size());
				System.out.println("3D size = " + CoreLogic._3DImage.size());
				if(lP.x < CoreLogic.disparity.width() && lP.y < CoreLogic.disparity.height())
					try {
						CoreLogic.cb.Visualization3D(CoreLogic.disparity, CoreLogic._3DImage,(int)lP.x,(int)lP.y);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

			
			CoreLogic.rightFrame = CoreLogic.runBlob.onCameraFrame();

			System.out.println("biggest blob (object) "+ "xMin = " + (int)CoreLogic.runBlob.getXMin()+" yMin = " +
					(int)CoreLogic.runBlob.getYMin()+ " W = " + (int)CoreLogic.runBlob.getW()+" H = "+
					(int)CoreLogic.runBlob.getH());
			Main.frm.djes.drawLeftPanel.changeImage(CoreLogic.toBufferedImage(CoreLogic.leftFrame));
			//jDisparityFrame.addMouseListener(runBlob);
			Main.frm.djes.drawRightPanel.changeImage(CoreLogic.toBufferedImage(CoreLogic.rightFrame));

			CoreLogic.disparity.convertTo(CoreLogic.disparity, CvType.CV_8UC3);
			Main.frm.djes.drawDisparityPanel.changeImage(CoreLogic.toBufferedImage(CoreLogic.disparity));
			
		}         
        }
}
