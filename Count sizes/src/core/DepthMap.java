package core;

import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
public class DepthMap {
	
	public DepthMap()
	{
		
	}
	public Mat startSGBM(Mat left, Mat right, int minDisparity, int numDisparities, int SADWindowSize, int preFilterCap, int uniquenessRatio,
			int disp12MaxDiff, int P1, int P2,int speckleRange, int speckleWindowSize)
	{
		Mat disparity = new Mat();
		Mat disparity8 = new Mat();
		minDisparity = minDisparity*-8;
		numDisparities = numDisparities*16;
		SADWindowSize =SADWindowSize*8;
		preFilterCap = preFilterCap*8;
		P1=P1*100;
		P2 =P2*110;
		speckleWindowSize = speckleWindowSize*10;
		StereoSGBM stereo = new StereoSGBM();
		
		stereo.set_SADWindowSize(SADWindowSize );
		stereo.set_minDisparity(minDisparity);
		stereo.set_numberOfDisparities(numDisparities);
		stereo.set_preFilterCap(preFilterCap);
		stereo.set_uniquenessRatio(uniquenessRatio);
		stereo.set_disp12MaxDiff(disp12MaxDiff);
		stereo.set_P1(P1);
		stereo.set_P2(P2);
		stereo.set_speckleRange(speckleRange);
		stereo.set_speckleWindowSize(speckleWindowSize);
		/*System.out.println(minDisparity + " "+ SADWindowSize+ " " +numDisparities + " " +preFilterCap + " " + uniquenessRatio + " " +
				stereo.get_disp12MaxDiff() + " " + stereo.get_P1() + " " + stereo.get_P2() + " " + stereo.get_speckleRange() + " " +stereo.get_speckleWindowSize()
				);*/
		stereo.compute(left, right, disparity);
		
		//disparity.convertTo(disparity8, CvType.CV_8U, 255/(numDisparities));
		Core.normalize(disparity, disparity8, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
		return disparity8;
	}
	public Mat startBM(Mat left, Mat right, int minDisparity, int numDisparities, int SADWindowSize, int preFilterCap, int uniquenessRatio,
			int disp12MaxDiff, int P1, int P2,int speckleRange, int speckleWindowSize)
	{
		Mat disparity = new Mat();
		//Mat disparity8 = new Mat();
		numDisparities = numDisparities*16;
		SADWindowSize =SADWindowSize*8;

		speckleWindowSize = speckleWindowSize*10;
		StereoBM stereo = new StereoBM(StereoBM.BASIC_PRESET,numDisparities,15);
		
		stereo.compute(left, right, disparity);
	//	Core.normalize(disparity, disparity8, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
		return disparity;
	}

}

