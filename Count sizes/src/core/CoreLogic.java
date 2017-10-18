package core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import main.OutputMain;
import main.runCalibration;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG;
import org.opencv.video.BackgroundSubtractorMOG2;

import ui.DrawJPanel;

public class CoreLogic {

	
	
	public static void JFrameInit(JFrame jframe, DrawJPanel drawPanel,
			Mat frame, String name) {
		runBlob = new RunBlob();
		Image img = toBufferedImage(frame);
		//jframe = new JFrame(name);
		jframe.setContentPane(drawPanel);
		jframe.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
		//jframe.setVisible(true);
		jframe.addMouseListener(runBlob);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void JFrameInitWithJSladers(JFrame jframe,
			HashMap<JLabel, JSlider> jSladers, JPanel drawPanel, String name) {
		//jframe = new JFrame(name);
		jframe.setContentPane(drawPanel);
		jframe.setSize(200, 600);
		
		// jframe.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//jframe.setVisible(true);

		// jframe.setLayout(new GridLayout(25,3));

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (Entry<JLabel, JSlider> entry : jSladers.entrySet()) {
			jframe.add(entry.getKey());
			jframe.add(entry.getValue());
		}
	}

	private static Mat negative(Mat foregroundThresh) {
		Mat result = new Mat();
		Mat white = foregroundThresh.clone();
		white.setTo(new Scalar(255.0));
		Core.subtract(white, foregroundThresh, result);
		return result;
	}

	public static double Calibration(Mat leftFrame, Mat rightFrame,
			CameraCalibration cb) {
		double err = 1;
		if (cb.getCounter() != 2)
			cb.getAllCornors(leftFrame, rightFrame);
		else {
			cb.setCounter(0);
			err = cb.StereoCalibrateAndRectify();
			cb.drawPoints(leftFrame, cb.getCorners());
			cb.drawPoints(rightFrame, cb.getCorners2());
			counter++;
			return err;
		}
		return err;
	}

	public static Mat Segmentation1(Mat inputImage, int thresh, int maxval,
			int ImgprocTHRESH) {
		/*
		 * if(inputImage.type()!=CvType.CV_8UC3) Imgproc.cvtColor(inputImage,
		 * inputImage, Imgproc.COLOR_BGR2GRAY);
		 */
		Mat segImage = new Mat();
		Imgproc.threshold(inputImage, segImage, thresh, maxval, ImgprocTHRESH);
		return segImage;
	}

	public static Mat BackgroundSubstractorMOG1(Mat inputImage,
			int learningRateI) {

		Mat foreground = new Mat();
		double learningRate = 0.1 * learningRateI;
		mog.apply(inputImage, foreground, learningRate); // СЌС‚Рѕ РІ
															// РёРЅС‚РµСЂС„РµР№СЃ
		return foreground;
	}

	public static Point lightPoint(Mat inputImage) {
		Mat hsv = new Mat();
		System.out.println("blob im W = " + inputImage.width() + " H =" + inputImage.height());
		if (inputImage.type() == CvType.CV_8U)
		{
			Imgproc.cvtColor(inputImage, hsv,Imgproc.COLOR_GRAY2BGR);
			Imgproc.cvtColor(hsv, hsv,Imgproc.COLOR_BGR2HSV);
		}
		//	hsv = inputImage;
		else if (inputImage.type() == CvType.CV_32F)
			hsv = inputImage;
		else
			Imgproc.cvtColor(inputImage, hsv, Imgproc.COLOR_BGR2HSV);
		byte[] vec3 = new byte[3];
		int max = -1000;
		Point lP = null;
		for (int i = 6; i < hsv.cols()-5 ; i++) {
			for (int j = 6; j < hsv.rows()-5; j++) {
				hsv.get(i, j, vec3);
				//System.out.println("i="+i+", j ="+j);
				if (Byte.toUnsignedInt(vec3[2]) > max) {
					System.out.println("iiiimax="+i+", jjjjjjmax ="+j);
					max = Byte.toUnsignedInt(vec3[2]);
					lP = new Point(i, j);
				}
			/*	System.out.println("1 - " + Byte.toUnsignedInt(vec3[0]));
				System.out.println("2 - " + Byte.toUnsignedInt(vec3[1]));
				System.out.println("3 - " + Byte.toUnsignedInt(vec3[2]));*/
			}
		}
		
		//System.out.println("light point = " + lP);
		return lP;
	}

	public static Mat BackgroundSubstractorMOG21(Mat inputImage,
			int learningRateI) {

		Mat foreground = new Mat();
		double learningRate = 0.1 * learningRateI;
		mog2.apply(inputImage, foreground, learningRate); // СЌС‚Рѕ РІ
															// РёРЅС‚РµСЂС„РµР№СЃ
		/*
		 * Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		 * Size(2,2)); Imgproc.dilate(foreground, foreground, dilate);
		 * Imgproc.dilate(foreground, foreground, dilate);
		 */
		return foreground;
	}

	public static Mat BackgroundSubstractorABSDIFF1(Mat inputImage,
			Mat inputBackImage) {
		Mat foregroundThresh = new Mat(); // Firstly, convert to gray-level
											// image, yields good results with
											// performance
		Mat backImage = new Mat();
		backImage = inputBackImage;
		Imgproc.cvtColor(inputImage, inputGray, Imgproc.COLOR_BGR2GRAY); // initialize
																			// background
																			// to
																			// 1st
																			// frame,
																			// convert
																			// to
																			// floating
																			// type
		if (accumulatedBackground.empty())
			inputGray.convertTo(accumulatedBackground, CvType.CV_32F); // convert
																		// background
																		// to
																		// 8U,
																		// for
																		// differencing
																		// with
																		// input
																		// image
		accumulatedBackground.convertTo(backImage, CvType.CV_8U); // compute
																	// difference
																	// between
																	// image and
																	// background
		Core.absdiff(backImage, inputGray, foreground); // apply threshold to
														// foreground image
		Imgproc.threshold(foreground, foregroundThresh, 120, 255,
				Imgproc.THRESH_BINARY_INV); // accumulate background //СЌС‚Рѕ РІ
											// РёРЅС‚РµСЂС„РµР№СЃ
		Mat inputFloating = new Mat();
		inputGray.convertTo(inputFloating, CvType.CV_32F);
		Imgproc.accumulateWeighted(inputFloating, accumulatedBackground, 0.01,
				foregroundThresh);
		return negative(foregroundThresh);
	}

	/*
	 * public static Mat FindObj2(Mat image, Mat frameForDrawing, int lowerbX,
	 * int lowerbY, int lowerbZ, int upperbX, int upperbY, int upperbZ) { Image
	 * img = toBufferedImage(image); Mat negative = new Mat(); ArrayList<Rect>
	 * objects = new ArrayList<Rect>(); if (image.type() != 0)
	 * Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
	 * Imgproc.GaussianBlur(image, image, new Size(21, 21), 0); Mat bw = new
	 * Mat(); Core.inRange(image, new Scalar(lowerbX, lowerbY, lowerbZ), new
	 * Scalar(upperbX, upperbY, upperbZ), image); List<MatOfPoint> contours =
	 * new ArrayList<MatOfPoint>(); negative = negative(image); return negative;
	 * }
	 */
	public static void FindObj2_01(Mat image, Mat frameForDrawing, int lowerbX,
			int lowerbY, int lowerbZ, int upperbX, int upperbY, int upperbZ) {
		Image img = toBufferedImage(image);
		// Mat hsv = new Mat();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
		Imgproc.GaussianBlur(image, image, new Size(21, 21), 0);
		// Mat bw = new Mat();
		Core.inRange(image, new Scalar(lowerbX, lowerbY, lowerbZ), new Scalar(
				upperbX, upperbY, upperbZ), image);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// Imgproc.findContours(image, contours, new Mat(),
		// Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
		// Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));

		for (int i = 0; i < contours.size(); i++) {
			// System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 50) {
				Rect rect = Imgproc.boundingRect(contours.get(i));

				// РЎРЊРЎвЂљР С•РЎвЂљ Р С—Р В°РЎР‚Р В°Р СР ВµРЎвЂљРЎР‚
				// Р В»РЎС“РЎвЂЎРЎв‚¬Р Вµ Р Р†РЎвЂ№Р Р…Р ВµРЎРѓРЎвЂљР С‘ Р Р†
				// Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ
				if (rect.height <= img.getHeight(null) - 100) {
					if (rect.height > 150 && rect.height != img.getHeight(null)) {
						System.out.println(rect.height + rect.width);
						// System.out.println(rect.x
						// +","+rect.y+","+rect.height+","+rect.width);
						Core.rectangle(frameForDrawing, new Point(rect.x,
								rect.y), new Point(rect.x + rect.width, rect.y
								+ rect.height), new Scalar(0, 0, 255));
						Core.putText(frameForDrawing, Integer
								.toString(rect.height + rect.width), new Point(
								rect.x, rect.y), Core.FONT_HERSHEY_COMPLEX,
								10.0, new Scalar(0, 0, 255));
					}
				}
			}
		}

	}

	public static void FindObj1(Mat inOriginalFrame, Mat frameForDrawing) {
		// Р РЋР С”Р В°Р В»РЎРЏРЎР‚РЎвЂ№ Р Р…РЎС“Р В¶Р Р…Р С•
		// Р Р†РЎвЂ№Р Р…Р ВµРЎРѓРЎвЂљР С‘ Р Р†
		// Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ, Р С‘РЎвЂ¦
		// Р Р…РЎС“Р В¶Р Р…Р С• Р Р…Р В°РЎРѓРЎвЂљРЎР‚Р В°Р С‘Р Р†Р В°РЎвЂљРЎРЉ
		Mat originalFrame = new Mat();
		Mat outFrame = new Mat();
		if (inOriginalFrame.type() != 0)
			Imgproc.cvtColor(inOriginalFrame, inOriginalFrame,
					Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(inOriginalFrame, originalFrame, new Size(3, 3));
		Image img = toBufferedImage(originalFrame);
		// Core.inRange(originalFrame, new Scalar(0, 100, 30), new
		// Scalar(256,256,256), originalFrame);

		// Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		// Size(3,3));
		Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
				new Size(15, 15));
		// Canny(..,.., trash, maxTrash
		Imgproc.Canny(originalFrame, outFrame, 10, 150, 3, false);
		// Imgproc.erode(outFrame, outFrame, erode);
		Imgproc.dilate(outFrame, outFrame, dilate);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(outFrame, contours, new Mat(), Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_SIMPLE);

		for (int i = 0; i < contours.size(); i++) {
			// System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 50) {
				Rect rect = Imgproc.boundingRect(contours.get(i));

				System.out.println(rect.height);
				// РЎРЊРЎвЂљР С•РЎвЂљ Р С—Р В°РЎР‚Р В°Р СР ВµРЎвЂљРЎР‚
				// Р В»РЎС“РЎвЂЎРЎв‚¬Р Вµ Р Р†РЎвЂ№Р Р…Р ВµРЎРѓРЎвЂљР С‘ Р Р†
				// Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ
				if (rect.height <= img.getHeight(null) - 100) {
					if (rect.height > 150 && rect.height != img.getHeight(null)) {
						System.out.println(rect.height + rect.width);
						// System.out.println(rect.x
						// +","+rect.y+","+rect.height+","+rect.width);
						Core.rectangle(frameForDrawing, new Point(rect.x,
								rect.y), new Point(rect.x + rect.width, rect.y
								+ rect.height), new Scalar(0, 0, 255));
						Core.putText(frameForDrawing,
								Integer.toString(rect.height / 5) + "sm",
								new Point(rect.x, rect.y),
								Core.FONT_HERSHEY_COMPLEX, 1.0, new Scalar(0,
										0, 255));
					}

				}

			}
		}
	}

	public static Mat Segmentation(Mat inputImage, int thresh, int maxval,
			int ImgprocTHRESH) {
		/*
		 * if(inputImage.type()!=CvType.CV_8UC3) Imgproc.cvtColor(inputImage,
		 * inputImage, Imgproc.COLOR_BGR2GRAY);
		 */
		Mat segImage = new Mat();
		Imgproc.threshold(inputImage, segImage, thresh, maxval, ImgprocTHRESH);
		return segImage;
	}

	public static Mat BackgroundSubstractorMOG(Mat inputImage, int learningRateI) {

		Mat foreground = new Mat();
		double learningRate = 0.1 * learningRateI;
		mog.apply(inputImage, foreground, learningRate); // СЌС‚Рѕ РІ
															// РёРЅС‚РµСЂС„РµР№СЃ
		return foreground;
	}

	public static Mat BackgroundSubstractorMOG2(Mat inputImage,
			int learningRateI) {

		Mat foreground = new Mat();
		double learningRate = 0.1 * learningRateI;
		mog2.apply(inputImage, foreground, learningRate); // СЌС‚Рѕ РІ
															// РёРЅС‚РµСЂС„РµР№СЃ
		/*
		 * Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		 * Size(2,2)); Imgproc.dilate(foreground, foreground, dilate);
		 * Imgproc.dilate(foreground, foreground, dilate);
		 */
		return foreground;
	}

	public static Mat BackgroundSubstractorABSDIFF(Mat inputImage,
			Mat inputBackImage) {
		Mat foregroundThresh = new Mat(); // Firstly, convert to gray-level
											// image, yields good results with
											// performance
		Mat backImage = new Mat();
		backImage = inputBackImage;
		Imgproc.cvtColor(inputImage, inputGray, Imgproc.COLOR_BGR2GRAY); // initialize
																			// background
																			// to
																			// 1st
																			// frame,
																			// convert
																			// to
																			// floating
																			// type
		if (accumulatedBackground.empty())
			inputGray.convertTo(accumulatedBackground, CvType.CV_32F); // convert
																		// background
																		// to
																		// 8U,
																		// for
																		// differencing
																		// with
																		// input
																		// image
		accumulatedBackground.convertTo(backImage, CvType.CV_8U); // compute
																	// difference
																	// between
																	// image and
																	// background
		Core.absdiff(backImage, inputGray, foreground); // apply threshold to
														// foreground image
		Imgproc.threshold(foreground, foregroundThresh, 120, 255,
				Imgproc.THRESH_BINARY_INV); // accumulate background //СЌС‚Рѕ РІ
											// РёРЅС‚РµСЂС„РµР№СЃ
		Mat inputFloating = new Mat();
		inputGray.convertTo(inputFloating, CvType.CV_32F);
		Imgproc.accumulateWeighted(inputFloating, accumulatedBackground, 0.01,
				foregroundThresh);
		return negative(foregroundThresh);
	}

	public static Mat filter(Mat imageIn, int lowerbX, int lowerbY, int lowerbZ, int upperbX,
			int upperbY, int upperbZ) {
		Mat image = imageIn.clone();
		//Image img = toBufferedImage(image);
		Mat negative = new Mat();
		//ArrayList<Rect> objects = new ArrayList<Rect>();
		if (image.type() != 0)
			Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
		Imgproc.GaussianBlur(image, image, new Size(21, 21), 0);
		//Mat bw = new Mat();
		Core.inRange(image, new Scalar(lowerbX, lowerbY, lowerbZ), new Scalar(upperbX, upperbY, upperbZ), image);
		//List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		negative = negative(image.clone());
		return negative;
	}
	public static void FindObj2_0(Mat image, Mat frameForDrawing, int lowerbX,
			int lowerbY, int lowerbZ, int upperbX, int upperbY, int upperbZ) {
		Image img = toBufferedImage(image);
		// Mat hsv = new Mat();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
		Imgproc.GaussianBlur(image, image, new Size(21, 21), 0);
		// Mat bw = new Mat();
		Core.inRange(image, new Scalar(lowerbX, lowerbY, lowerbZ), new Scalar(
				upperbX, upperbY, upperbZ), image);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// Imgproc.findContours(image, contours, new Mat(),
		// Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
		// Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));

		for (int i = 0; i < contours.size(); i++) {
			// System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 50) {
				Rect rect = Imgproc.boundingRect(contours.get(i));

				if (rect.height <= img.getHeight(null) - 100) {
					if (rect.height > 150 && rect.height != img.getHeight(null)) {
						System.out.println(rect.height + rect.width);
						// System.out.println(rect.x
						// +","+rect.y+","+rect.height+","+rect.width);
						Core.rectangle(frameForDrawing, new Point(rect.x,
								rect.y), new Point(rect.x + rect.width, rect.y
								+ rect.height), new Scalar(0, 0, 255));
						Core.putText(frameForDrawing, Integer
								.toString(rect.height + rect.width), new Point(
								rect.x, rect.y), Core.FONT_HERSHEY_COMPLEX,
								10.0, new Scalar(0, 0, 255));
					}
				}
			}
		}

	}

	public static void FindObj(Mat inOriginalFrame, Mat frameForDrawing) {
		// Р РЋР С”Р В°Р В»РЎРЏРЎР‚РЎвЂ№ Р Р…РЎС“Р В¶Р Р…Р С•
		// Р Р†РЎвЂ№Р Р…Р ВµРЎРѓРЎвЂљР С‘ Р Р†
		// Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ, Р С‘РЎвЂ¦
		// Р Р…РЎС“Р В¶Р Р…Р С• Р Р…Р В°РЎРѓРЎвЂљРЎР‚Р В°Р С‘Р Р†Р В°РЎвЂљРЎРЉ
		Mat originalFrame = new Mat();
		Mat outFrame = new Mat();
		if (inOriginalFrame.type() != 0)
			Imgproc.cvtColor(inOriginalFrame, inOriginalFrame,
					Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(inOriginalFrame, originalFrame, new Size(3, 3));
		Image img = toBufferedImage(originalFrame);
		// Core.inRange(originalFrame, new Scalar(0, 100, 30), new
		// Scalar(256,256,256), originalFrame);

		// Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		// Size(3,3));
		Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
				new Size(15, 15));
		// Canny(..,.., trash, maxTrash
		Imgproc.Canny(originalFrame, outFrame, 10, 150, 3, false);
		// Imgproc.erode(outFrame, outFrame, erode);
		Imgproc.dilate(outFrame, outFrame, dilate);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(outFrame, contours, new Mat(), Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_SIMPLE);

		for (int i = 0; i < contours.size(); i++) {
			// System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 50) {
				Rect rect = Imgproc.boundingRect(contours.get(i));

				System.out.println(rect.height);
				// РЎРЊРЎвЂљР С•РЎвЂљ Р С—Р В°РЎР‚Р В°Р СР ВµРЎвЂљРЎР‚
				// Р В»РЎС“РЎвЂЎРЎв‚¬Р Вµ Р Р†РЎвЂ№Р Р…Р ВµРЎРѓРЎвЂљР С‘ Р Р†
				// Р С‘Р Р…РЎвЂљР ВµРЎР‚РЎвЂћР ВµР в„–РЎРѓ
				if (rect.height <= img.getHeight(null) - 100) {
					if (rect.height > 150 && rect.height != img.getHeight(null)) {
						System.out.println(rect.height + rect.width);
						// System.out.println(rect.x
						// +","+rect.y+","+rect.height+","+rect.width);
						Core.rectangle(frameForDrawing, new Point(rect.x,
								rect.y), new Point(rect.x + rect.width, rect.y
								+ rect.height), new Scalar(0, 0, 255));
						Core.putText(frameForDrawing,
								Integer.toString(rect.height / 5) + "sm",
								new Point(rect.x, rect.y),
								Core.FONT_HERSHEY_COMPLEX, 1.0, new Scalar(0,
										0, 255));
					}

				}

			}
		}
	}
	public static runCalibration rC;
	public static OutputMain om;
	static int counter = 0;
	public static Mat inputGray;
	public static Mat foreground;
	public static Mat accumulatedBackground;
	public static BackgroundSubtractorMOG mog;
	public static BackgroundSubtractorMOG2 mog2;
	public static Mat outFrameLeft;
	public static Mat leftFrame;
	public static Mat outFrameRight;
	public static Mat rightFrame;
	public static Mat leftCalibFrame;
	public static Mat rightCalibFrame;
	public static Mat background;
	public static DepthMap dM;
	public static VideoCapture videoCaptureLeft;
	public static VideoCapture videoCaptureRight;
	public static VideoCapture videoCalibCaptureLeft;
	public static VideoCapture videoCalibCaptureRight;
	public static Mat disparity;
	public static RunBlob runBlob;
	public static boolean stage = false;
	public static CameraCalibration cb;
	public static Mat _3DImage;
	public static BufferedImage toBufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;

	}
}
