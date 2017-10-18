package core;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import main.Main;
import ui.JStatisticFrame;

public class RunBlob extends MouseAdapter {
	// private int screen = 0;
	// private Rectangle start2 = new Rectangle(135,350,180,55);
	private boolean mIsColorSelected = false;
	private Mat mRgba;
	private Scalar mBlobColorRgba = new Scalar(255);
	private Scalar mBlobColorHsv = new Scalar(255);
	private ColorBlobDetector mDetector = new ColorBlobDetector();
	private Mat mSpectrum = new Mat();
	private Size SPECTRUM_SIZE = new Size(200, 64);
	private Scalar CONTOUR_COLOR = new Scalar(255, 0, 0, 255);
	private Rectangle mOpenCvCameraView = new Rectangle(640, 480);
	private double w,h;
	private double k = 0.2;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	public RunBlob(Mat mRgba) {
		this.mRgba = mRgba;
	}

	public RunBlob() {

	}

	@Override
	public void mousePressed(MouseEvent event) {
		/*
		 * int mx = e.getX(); int my = e.getY(); System.out.println(mx+"-"+my);
		 * System.out.println("2"); if((screen ==0) && start2.contains(mx,my)){
		 * screen=1; System.out.println("1");
		 * 
		 * }
		 */

		int cols = mRgba.cols();
		int rows = mRgba.rows();
		System.out.println("cols:" + cols + "\trows:" + rows);
		int xOffset = (int) ((mOpenCvCameraView.getWidth() - cols) / 2);
		int yOffset = (int) ((mOpenCvCameraView.getHeight() - rows) / 2);

		int x = (int) event.getX() - xOffset;
		int y = (int) event.getY() - yOffset;

		System.out.println("Click image coordinates: (" + x + ", " + y + ")");

		if ((x < 0) || (y < 0) || (x > cols) || (y > rows))
			return;

		Rect touchedRect = new Rect();

		touchedRect.x = (x > 4) ? x - 4 : 0;
		touchedRect.y = (y > 4) ? y - 4 : 0;

		touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols
				- touchedRect.x;
		touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows
				- touchedRect.y;

		Mat touchedRegionRgba = mRgba.submat(touchedRect);

		Mat touchedRegionHsv = new Mat();
		// Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv,
		// Imgproc.COLOR_RGB2HSV_FULL);
		touchedRegionHsv = touchedRegionRgba.clone();
		// Calculate average color of touched region
		mBlobColorHsv = Core.sumElems(touchedRegionHsv);
		int pointCount = touchedRect.width * touchedRect.height;
		for (int i = 0; i < mBlobColorHsv.val.length; i++)
			mBlobColorHsv.val[i] /= pointCount;

		mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

		System.out.println("Clicked rgba color: (" + mBlobColorRgba.val[0]
				+ ", " + mBlobColorRgba.val[1] + ", " + mBlobColorRgba.val[2]
				+ ", " + mBlobColorRgba.val[3] + ")");

		mDetector.setHsvColor(mBlobColorHsv);

		Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

		setmIsColorSelected(true);
		mRgba = onCameraFrame();

		touchedRegionRgba.release();
		touchedRegionHsv.release();
		return; // don't need subsequent touch events
	}

	public Mat getmRgba() {

		return mRgba;

	}

	public Mat getmSpectrum()
	{
		return mSpectrum;
	}

	public void setmRgba(Mat frame) {

		this.mRgba = frame.clone();

	}

	// public Mat onCameraFrame(Mat inputFrame) {
	// mRgba = inputFrame.rgba();
	// return mRgba;
	public Mat onCameraFrame() {
		// if (ismIsColorSelected()) {
		double w, h, k1, k2;
		if (Main.frm.getCalcMode() == true) {
			k1 = ParametersCalculation.getK1();
			k2 = ParametersCalculation.getK2();
		} else
			k1 = k2 = 1;

		double xMin;
		double xMax;
		double yMin;
		double yMax;
		this.w = 0;
		this.h = 0;
		mDetector.process(mRgba);
		List<MatOfPoint> contours = mDetector.getContours();
		System.out.println("Contours count: " + contours.size());
		if (contours.size() > 0) {
			for (int i = 0; i < contours.size(); i++) {
				double wReal, hReal;
				MatOfPoint contour = contours.get(i);
				xMin = 10000;
				xMax = -10000;
				yMin = 10000;
				yMax = -10000;
				for (int j = 0; j < contour.toArray().length; j++) {
					if (contour.toArray()[j].x < xMin)
						xMin = contour.toArray()[j].x;
					if (contour.toArray()[j].x > xMax)
						xMax = contour.toArray()[j].x;
					if (contour.toArray()[j].y < yMin)
						yMin = contour.toArray()[j].y;
					if (contour.toArray()[j].y > yMax)
						yMax = contour.toArray()[j].y;
				}
				System.out.println("MinX " + i + " :" + xMin + " MaxX " + i + " :" + xMax + " MinY " + i + " :" + yMin
						+ " MaxY " + i + " :" + yMax);
				w = xMax - xMin;
				h = yMax - yMin;
				if (w + h > this.w + this.h) {
					this.w = w;
					this.h = h;
					this.xMin = xMin;
					this.xMax = xMax;
					this.yMin = yMin;
					this.yMax = yMax;
				}
				System.out.println("Width " + i + " :" + w);
				System.out.println("Height " + i + " :" + h);
				System.out.println("k1 " + i + " :" + k1);
				System.out.println("k2 " + i + " :" + k2);
				
				if (Main.frm.getCalcMode() == false) {//save parameters to static ParametersCalculation
					

					wReal = k1 * w;
					hReal = k2 * h;
				} else {
					wReal = k1 * w;
					hReal = k2 * h;
				}
				Core.putText(mRgba, "H = " + Double.toString(hReal) + " W = " + Double.toString(wReal),
						new Point(xMin + 5, yMin + 5), Core.FONT_HERSHEY_COMPLEX, 0.7, new Scalar(0, 0, 0));
				System.out.println("Real_Width " + i + " :" + wReal);
				System.out.println("Real_Height " + i + " :" + hReal);
				// Core.rectangle(mRgba, new Point(xMin, yMin), new Point(xMax,
				// yMax), new Scalar(0, 0, 255));
			}
			List<Double> listK = new ArrayList<Double>();
			if (Main.jsf.getTextRealH() != 1 && Main.jsf.getTextRealW() != 1) {
				listK = ParametersCalculation.CalculateK(Main.jsf.getTextRealH(), Main.jsf.getTextRealW());
				ParametersCalculation.setK1(listK.get(0));
				ParametersCalculation.setK2(listK.get(1));
				ParametersCalculation.setRealh(Main.jsf.getTextRealH());
				ParametersCalculation.setRealw(Main.jsf.getTextRealW());
				ParametersCalculation.setH(this.h);
				ParametersCalculation.setW(this.w);
			}
		}
		Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

		Mat colorLabel = mRgba.submat(4, 68, 4, 68);
		colorLabel.setTo(mBlobColorRgba);

		Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70,
				70 + mSpectrum.cols());
		mSpectrum.copyTo(spectrumLabel);
		// }
		return mRgba.clone();
	}

	private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
		Mat pointMatRgba = new Mat();
		Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
		Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL,
				4);

		return new Scalar(pointMatRgba.get(0, 0));
	}

	public boolean ismIsColorSelected() {
		return mIsColorSelected;
	}
	public double getH()
	{
		return h;
	}
	public double getXMin()
	{
		return xMin;
	}
	public double getYMin()
	{
		return yMin;
	}
	public double getW()
	{
		return w;
	}
	public void setK(double k)
	{
		this.k = k;
	}
	public void setmIsColorSelected(boolean mIsColorSelected) {
		this.mIsColorSelected = mIsColorSelected;
	}
}
