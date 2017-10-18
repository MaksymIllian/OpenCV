package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SwingUtilities;

import ui.DrawJPanel;
import core.CoreLogic;
import static main.Main.*;

public class runCalibration extends Thread {

	private static BufferedWriter writer = null;// new
												// BufferedWriter(System.getProperty("user.dir")+"\\QCalibratioMatrixOut.txt");

	@Override
	public void run() {
		try {
			writer = new BufferedWriter(new FileWriter(
					System.getProperty("user.dir")
							+ "\\QCalibratioMatrixOut.txt"));

			// CoreLogic.videoCalibCaptureLeft.grab();
			// CoreLogic.videoCalibCaptureRight.grab();
			// CoreLogic.videoCalibCaptureLeft.retrieve(CoreLogic.leftCalibFrame);
			// CoreLogic.videoCalibCaptureRight.retrieve(CoreLogic.rightCalibFrame);
			// Main.frm.djes.drawCalibLeftPanel = new DrawJPanel(
			// CoreLogic.toBufferedImage(CoreLogic.leftCalibFrame));
			// Main.frm.djes.drawCalibRightPanel = new DrawJPanel(
			// CoreLogic.toBufferedImage(CoreLogic.rightCalibFrame));
			// CoreLogic.JFrameInit(frm.djes.jCalibLeftFrame,
			// frm.djes.drawCalibLeftPanel, CoreLogic.leftCalibFrame, "left");
			//
			// CoreLogic.JFrameInit(frm.djes.jCalibRightFrame,
			// frm.djes.drawCalibRightPanel, CoreLogic.rightCalibFrame,
			// "right");

			double calibrationError = 1;
			// int successCounter = 0;

			// while(calibrationError>=0.5) {

			while (calibrationError >= 0.5) {

				CoreLogic.videoCalibCaptureLeft.grab();
				CoreLogic.videoCalibCaptureRight.grab();
				CoreLogic.videoCalibCaptureLeft
						.retrieve(CoreLogic.leftCalibFrame);
				CoreLogic.videoCalibCaptureRight
						.retrieve(CoreLogic.rightCalibFrame);
				
				//SwingUtilities.invokeLater(Main.m);
			    
				
				System.out
						.println("-----------------------------------------------------"+CoreLogic.rC.isAlive());
				calibrationError=CoreLogic.Calibration(CoreLogic.leftCalibFrame,
				 CoreLogic.rightCalibFrame, CoreLogic.cb);
				Main.frm.djes.drawCalibLeftPanel.changeImage(CoreLogic
						.toBufferedImage(CoreLogic.leftCalibFrame));
				Main.frm.djes.drawCalibRightPanel.changeImage(CoreLogic
						.toBufferedImage(CoreLogic.rightCalibFrame));
				 //

			}
			for(int i =0;i<CoreLogic.cb.getQ().cols();i++)
			{
				for(int j =0;j<CoreLogic.cb.getQ().rows();j++)
				{
					System.out.println(CoreLogic.cb.getQ().get(i, j));
					double item = CoreLogic.cb.getQ().get(i, j)[0];
					writer.write(item+" ");
					
				}
				writer.newLine();
			}
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Main.frm.djes.jCalibLeftFrame.setVisible(false);
		 Main.frm.djes.jCalibRightFrame.setVisible(false);
		

	}
}
