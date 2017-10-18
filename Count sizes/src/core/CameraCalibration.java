package core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class CameraCalibration {
    private int flagsCorner =  Calib3d.CALIB_CB_ADAPTIVE_THRESH | Calib3d.CALIB_CB_FILTER_QUADS;
    private int flagsCalib = Calib3d.CALIB_CB_ADAPTIVE_THRESH | Calib3d.CALIB_CB_FILTER_QUADS;
    private TermCriteria criteria = new TermCriteria(TermCriteria.EPS
            + TermCriteria.MAX_ITER, 100, 0.1);
    private Size winSize = new Size(5, 5), zoneSize = new Size(-1, -1);
    private Size patternSize;
    private ArrayList<Mat> objectPoints = new ArrayList<Mat>(), imagePoints = new ArrayList<Mat>();
    private ArrayList<Mat> imagePoints2 = new ArrayList<Mat>();
    private ArrayList<Mat> vCorners;
    private ArrayList<Mat> vImg;
    private Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_64F);
    private Mat distCoeffs = Mat.zeros(8, 1, CvType.CV_64F);
    private ArrayList<Mat> rvecs = new ArrayList<Mat>();
    private ArrayList<Mat> tvecs = new ArrayList<Mat>();
    private MatOfPoint2f corners;
    private MatOfPoint2f corners2;
    private int counter;
    
    private Mat CM1 = new Mat(3, 3, CvType.CV_64FC1);
    private Mat CM2 = new Mat(3, 3, CvType.CV_64FC1);
    private Mat D1= new Mat(), D2 = new Mat();
    private Mat R= new Mat(), T= new Mat(), E= new Mat(), F= new Mat();
    
    private Mat R1 = new Mat(), R2 = new Mat(), P1 = new Mat(), P2 = new Mat(), Q = new Mat();
    
    private Mat map1x = new Mat(), map1y = new Mat(), map2x = new Mat(), map2y = new Mat();
    private Mat imgU1 = new Mat(), imgU2 = new Mat();
    private Rect ROI1 = new Rect();
    private Rect ROI2 = new Rect();
    CameraCalibration() {
    }

    public CameraCalibration(Size patternSize) {
        this.patternSize = patternSize;
    }

    public boolean getCorners(Mat gray, MatOfPoint2f corners) {
        if (!Calib3d.findChessboardCorners(gray, patternSize,
                corners, flagsCorner))
            return false;
        Imgproc.cornerSubPix(gray, corners, winSize, zoneSize,
        		new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1));
        return true;
    }

    public MatOfPoint3f getCorner3f() {
        MatOfPoint3f corners3f = new MatOfPoint3f();
        double squareSize = 50;
        Point3[] vp = new Point3[(int) (patternSize.height * 
                                            patternSize.width)];
        int cnt = 0;
        for (int i = 0; i < patternSize.height; ++i)
            for (int j = 0; j < patternSize.width; ++j, cnt++)
                vp[cnt] = new Point3(j * squareSize, 
                                     i * squareSize, 0.0d);
        corners3f.fromArray(vp);
        return corners3f;
    }
    
    public boolean drawPoints(Mat rgbaFrame, MatOfPoint2f corners) {
    	
        Calib3d.drawChessboardCorners(rgbaFrame, patternSize, corners , true);
        
        return true;
    }
    
    public double StereoCalibrateAndRectify()
    {
    	//Start Stereo Calibration.
    	double err = Calib3d.stereoCalibrate(objectPoints, imagePoints, imagePoints2, 
                CM1, D1, CM2, D2, vImg.get(0).size(), R, T, E, F,
                new TermCriteria(TermCriteria.MAX_ITER+TermCriteria.EPS, 100, 1e-5), 
                Calib3d.CALIB_SAME_FOCAL_LENGTH | Calib3d.CALIB_ZERO_TANGENT_DIST);
	    	/*
	    	CM1 - Camera Matrix of first camera.
	    	CM2 - Camera Matrix of second camera.
	    	D1 - Distortion coeff matrix of first camera.
	    	D2 - Distortion coeff matrix of second camera.
	    	R - Rotation Matrix between first and second camera coordinate systems.
	    	T - Translation vector between the coordinate systems of the cameras.
	    	E - Essential matrix.
	    	F - Fundamental matrix.*/
    
	    	//Start Stereo Rectification.
    	System.out.println("done, \nerrReproj = " + err);
        Calib3d.stereoRectify(CM1, D1, CM2, D2, vImg.get(0).size(), R, T, R1, R2, P1, P2, Q,
        		Calib3d.CALIB_ZERO_DISPARITY,-1,new Size(0,0),ROI1,ROI2);
	        /*
	        R1 - 3x3 rectification transform (rotation matrix) for the first camera.
			R2 - 3x3 rectification transform (rotation matrix) for the second camera.
			P1 - 3x4 projection matrix in the new (rectified) coordinate systems for the first camera.
			P2 - 3x4 projection matrix in the new (rectified) coordinate systems for the second camera.
			Q – 4x4 disparity-to-depth mapping matrix.*/
        
        objectPoints = new ArrayList<Mat>(); 
        imagePoints=new ArrayList<Mat>();
        imagePoints2=new ArrayList<Mat>();
        return err;
    }
    
    public void Undistort(Mat img1, Mat img2,Mat img1out, Mat img2out)
    {	
    	Imgproc.undistort(img1, img1out, CM1, D1);
    	Imgproc.undistort(img2, img2out, CM2, D2);
   
    }
    public void UdistortFromRectify(Mat img1, Mat img2)
    {
    	Imgproc.initUndistortRectifyMap(CM1, D1, R1, P1, vImg.get(0).size(), CvType.CV_32FC2, map1x, map1y);
        Imgproc.initUndistortRectifyMap(CM2, D2, R2, P2, vImg.get(0).size(), CvType.CV_32FC2, map2x, map2y);

        Imgproc.remap(img1, img1, map1x, map1y, Imgproc.INTER_LINEAR, Imgproc.BORDER_CONSTANT, new Scalar(0));
        Imgproc.remap(img2, img2, map2x, map2y, Imgproc.INTER_LINEAR, Imgproc.BORDER_CONSTANT, new Scalar(0));
    }
    public int calibrate() {
    	if(vImg.size()!=0){
        double errReproj = Calib3d.calibrateCamera(objectPoints, 
                imagePoints,vImg.get(0).size(), cameraMatrix, 
                distCoeffs, rvecs, tvecs,flagsCalib);
        System.out.println("done, \nerrReproj = " + errReproj);
        System.out.println("cameraMatrix = \n" + cameraMatrix.dump());
        System.out.println("distCoeffs = \n" + distCoeffs.dump());
        setCounter(getCounter() + 1);
        System.out.println("counter = "+getCounter());
    	}
    	return getCounter();
    }
    public Mat LoadQ() throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(System.getProperty("user.dir") + "\\QCalibratioMatrixOut.txt"));
		String line;
		Mat Q = new Mat(new Size(4, 4), CvType.CV_64FC1);
		int col = 0, row = 0;
		while (((line = reader.readLine()) != null)) { // for each transaction
			String[] splitedLine = line.split(" ");
			List<Double> paramsList = new ArrayList<Double>();
			for (String parameter : splitedLine) {
				paramsList.add(Double.parseDouble(parameter));
				Q.put(row, col, Double.parseDouble(parameter));
				col++;
			}
			row++;
			col = 0;
		}
  	
    	return Q;
    }
    public void Visualization3D(Mat disparity, Mat _3dImage, int x, int y) throws IOException {
	    double[][] d = { { 1, 0, 0, -160 }, { 0, 1, 0, -120 }, 
				{ 0, 0, 0, 348.087 },
				{ 0, 0, 1.0/95, 0 } };
	    Mat Q = LoadQ();
		
/*		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++)
				Q.put(row, col, d[row][col]);
		}*/
		System.out.println("Q = " + Q.dump());
		ArrayList<Point3> pts = new ArrayList<Point3>();

		Calib3d.reprojectImageTo3D(disparity, _3dImage, Q, true);
		double[] p;
		//(0 <= roi.x && 0 <= roi.width && roi.x + roi.width <= m.cols && 
		//0 <= roi.y && 0 <= roi.height && roi.y + roi.height <= m.rows)
		Mat t = _3dImage.submat(y ,y + 1, x, x + 1);
		for (int i = 0; i < t.cols(); i++) {
			Converters.Mat_to_vector_Point3f(t.col(i), pts);
			for (Point3 point3 : pts) {
				System.out.println("coordinate light point on 3D " + point3);
				if (CoreLogic.stage == true)
					ParametersCalculation.Select(point3.z);
				else
					ParametersCalculation.setZ(point3.z);

			}
		}

	}

    public void getAllCornors(Mat matLeft, Mat matRight) {
        vImg = new ArrayList<Mat>();
        MatOfPoint3f corners3f = getCorner3f();  
        Mat grayLeft = new Mat();
        Imgproc.cvtColor(matLeft, grayLeft, Imgproc.COLOR_BGR2GRAY);
        Mat grayRight = new Mat();
        Imgproc.cvtColor(matRight, grayRight, Imgproc.COLOR_BGR2GRAY);
        setCorners(new MatOfPoint2f());
        setCorners2(new MatOfPoint2f());
        if(imagePoints.size()==0)
        {
	        if (getCorners(grayLeft, getCorners()))
	        {	            
		        objectPoints.add(corners3f);
		        imagePoints.add(getCorners());
		        vImg.add(matLeft);
		        System.out.println("Left corners");
		        drawPoints(matLeft, getCorners());
		        setCounter(getCounter() + 1);
	        }
        }
        if(imagePoints2.size()==0)
        {
	        if (getCorners(grayRight, getCorners2()))
	        {	            
		      //  objectPoints.add(corners3f);
		        imagePoints2.add(getCorners2());
		        vImg.add(matRight);
		        System.out.println("Right corners");
		        drawPoints(matRight, getCorners2());
		        setCounter(getCounter() + 1);
	        }
        }          
/*            if(imagePoints.size()==0 && imagePoints2.size()==0)
            {
	            if (getCorners(grayLeft, corners)&&getCorners(grayRight, corners2))
	            {	            
		            objectPoints.add(corners3f);
		            imagePoints.add(corners);
		            imagePoints2.add(corners2);
		            vImg.add(matLeft);
		            System.out.println("Right corners");
		            System.out.println("Left corners");
		            counter+=2;
	            }
            }*/
    }
    public Mat getQ()
    {
    	return Q;
    }
    public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public MatOfPoint2f getCorners() {
		return corners;
	}

	public void setCorners(MatOfPoint2f corners) {
		this.corners = corners;
	}

	public MatOfPoint2f getCorners2() {
		return corners2;
	}

	public void setCorners2(MatOfPoint2f corners2) {
		this.corners2 = corners2;
	}

	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}