package ui;

import java.awt.Component;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JSlider;




public class JSlidersFilter {

	
	/*
	private JLabel minDisparitysliderLabel = new JLabel("minDisparity", JLabel.CENTER);
    JSlider minDisparitySlider = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 2);
    public int getminDisparity(){return (int)minDisparitySlider.getValue();}
    
    private JLabel numDisparitiesLabel = new JLabel("numberOfDisparity", JLabel.CENTER);
    JSlider numDisparitiesSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1, 20, 1);
    public int getnumDisparities(){return (int)numDisparitiesSlider.getValue();}
    
    private JLabel SADWindowSizeLabel = new JLabel("SADWindowSize", JLabel.CENTER);
    JSlider SADWindowSizeSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1, 25, 1);public int getSADWindowSize(){return (int)SADWindowSizeSlider.getValue();}
    
    private JLabel preFilterCapLabel = new JLabel("preFilter", JLabel.CENTER);
    JSlider preFilterCapSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1, 20, 4);public int getpreFilterCap(){return (int)preFilterCapSlider.getValue();}
    
    private JLabel uniquenessRatioLabel = new JLabel("uniquenessRatio", JLabel.CENTER);
    JSlider uniquenessRatioSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1, 128, 8);public int getuniquenessRatio(){return (int)uniquenessRatioSlider.getValue();}
    
    private JLabel disp12MaxDiffLabel = new JLabel("disp12MaxDiff", JLabel.CENTER);
    JSlider disp12MaxDiffSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getdisp12MaxDiff(){return (int)disp12MaxDiffSlider.getValue();}
    
    
    private JLabel P1Label = new JLabel("P1", JLabel.CENTER);
    JSlider P1Slider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getP1(){return (int)P1Slider.getValue();}
    
    private JLabel P2Label = new JLabel("P2", JLabel.CENTER);
    JSlider P2Slider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getP2(){return (int)P2Slider.getValue();}
    
    private JLabel speckleRangeLabel = new JLabel("speckleRange", JLabel.CENTER);
    JSlider speckleRangeSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getspeckleRange(){return (int)speckleRangeSlider.getValue();}
    
    private JLabel speckleWindowSizeLabel = new JLabel("speckleWindowSize", JLabel.CENTER);
    JSlider speckleWindowSizeSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getspeckleWindowSize(){return (int)speckleWindowSizeSlider.getValue();}
    
    private JLabel threshLabel = new JLabel("thresh", JLabel.CENTER);
    JSlider threshSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);public int getthresh(){return (int)threshSlider.getValue();}
    
    private JLabel maxvalLabel = new JLabel("maxval", JLabel.CENTER);
    JSlider maxvalSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 512, 256);public int getmaxval(){return (int)maxvalSlider.getValue();}
    
    //ImgprocTHRESH learningRateI   thresh  maxval
    private JLabel ImgprocTHRESHLabel = new JLabel("ImgprocTHRESH", JLabel.CENTER);
    JSlider ImgprocTHRESHSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 512, 9);public int getImgprocTHRESH(){return (int)ImgprocTHRESHSlider.getValue();}
    
    private JLabel learningRateILabel = new JLabel("learningRateI", JLabel.CENTER);
    JSlider learningRateISlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1, 100, 1);public int getlearningRateI(){return (int)learningRateISlider.getValue();}
    
    //historyI nmixturesI backgroundRatioI noiseSigma varThreshold bShadowDetectionI
    private JLabel historyILabel = new JLabel("historyI", JLabel.CENTER);
    JSlider historyISlider  = new JSlider(JSlider.HORIZONTAL,  
  	      1000, 1020, 1000);public int gethistoryI(){return (int)historyISlider.getValue();}
    
    private JLabel nmixturesILabel = new JLabel("nmixturesI", JLabel.CENTER);
    JSlider nmixturesISlider  = new JSlider(JSlider.HORIZONTAL,  
  	      32, 52, 32);public int getnmixturesI(){return (int)nmixturesISlider.getValue();}
    
    private JLabel backgroundRatioILabel = new JLabel("backgroundRatioI", JLabel.CENTER);
    JSlider backgroundRatioISlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 100, 5);public int getbackgroundRatioI(){
    	return (int)backgroundRatioISlider.getValue();
    }
    public double getbackgroundRatioD(){
    	return 0.01*getbackgroundRatioI();
    }
    
    ////0000000000000000000000000000000000000000000000000000000000000
    ///backgroundRatioD
    
    private JLabel noiseSigmaLabel = new JLabel("noiseSigma", JLabel.CENTER);
    JSlider noiseSigmaSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 16);
    
    public int getnoiseSigma(){
    	return (int)noiseSigmaSlider.getValue();
    }
    
    private JLabel varThresholdLabel = new JLabel("varThreshold", JLabel.CENTER);
    JSlider varThresholdSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 20, 0);
    
    public int getvarThreshold(){
    	return (int)varThresholdSlider.getValue();
    }
    
    private JLabel bShadowDetectionILabel = new JLabel("bShadowDetectionI", JLabel.CENTER);
    JSlider bShadowDetectionISlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 1, 0);
    
    public int getbShadowDetectionI(){
    	return (int)bShadowDetectionISlider.getValue();
    }
    public boolean getbShadowDetectionB(){
    	return getbShadowDetectionI()==0?false:true;
    }
 // history nmixtures backgroundRatio noiseSigma 
	  //varThreshold bShadowDetection 
	  */
	  // historyI nmixturesI backgroundRatioI noiseSigma varThreshold bShadowDetectionI
    private JLabel lowerbXLabel = new JLabel("lowerbX", JLabel.CENTER);
    JSlider lowerbXSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 0);
    public int getlowerbX(){return (int)lowerbXSlider.getValue(); }
    
    private JLabel lowerbYLabel = new JLabel("lowerbY", JLabel.CENTER);
    JSlider lowerbYSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 100);
    public int getlowerbY(){return (int)lowerbYSlider.getValue(); }
    
    private JLabel lowerbZLabel = new JLabel("lowerbZ", JLabel.CENTER);
    JSlider lowerbZSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 30);
    public int getlowerbZ(){return (int)lowerbZSlider.getValue(); }
    
    private JLabel upperbXLabel = new JLabel("upperbX", JLabel.CENTER);
    JSlider upperbXSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 256);
    public int getupperbX(){return (int)upperbXSlider.getValue(); }
    
    private JLabel upperbYLabel = new JLabel("upperbY", JLabel.CENTER);
    JSlider upperbYSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 256);
    public int getupperbY(){return (int)upperbYSlider.getValue(); }
    
    private JLabel upperbZLabel = new JLabel("upperbZ", JLabel.CENTER);
    JSlider upperbZSlider  = new JSlider(JSlider.HORIZONTAL,  
  	      0, 256, 256);
    public int getupperbZ(){return (int)upperbZSlider.getValue(); }
  //lowerbX lowerbY lowerbZ upperbX upperbY upperbZ
    public static void initJSlider(JSlider jSlider)
    {
    	jSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
    	jSlider.setMajorTickSpacing(2);
    	jSlider.setMinorTickSpacing(1);
    	jSlider.setPaintTicks(true);
    	jSlider.setPaintLabels(true);
    	
    	//jSlider.setPreferredSize(new Dimension(jSlider.getPreferredSize().height, 2*22)); 
    }

    public static void initJSliders(JSlider jSliders[])
    {
    	for(JSlider jSlider : jSliders)
    		initJSlider(jSlider);
    }
    
    public JSlidersFilter()
    {
    	initJSliders(new JSlider[]{/*minDisparitySlider,numDisparitiesSlider,SADWindowSizeSlider,
    	  		  preFilterCapSlider,uniquenessRatioSlider,disp12MaxDiffSlider,P1Slider,P2Slider,
    	  		  speckleRangeSlider,speckleWindowSizeSlider,threshSlider,maxvalSlider,ImgprocTHRESHSlider,
    	  		learningRateISlider,
    	  		historyISlider,nmixturesISlider, backgroundRatioISlider, noiseSigmaSlider, varThresholdSlider,
    	  		bShadowDetectionISlider, */
    	  	lowerbXSlider, lowerbYSlider, lowerbZSlider, upperbXSlider, upperbYSlider, upperbZSlider,
    	});
    }
    public LinkedHashMap<JLabel,JSlider> getjSladers() {
		return jSladers;
	}
	public void setjSladers(LinkedHashMap<JLabel,JSlider> jSladers) {
		this.jSladers = jSladers;
	}

	//final JSlidersClass jss = new JSlidersClass();
    private LinkedHashMap<JLabel,JSlider> jSladers = new LinkedHashMap<JLabel, JSlider>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
    /*	put(minDisparitysliderLabel,minDisparitySlider);
    	put(numDisparitiesLabel,numDisparitiesSlider); 
    	put(SADWindowSizeLabel,SADWindowSizeSlider); 
    	put(preFilterCapLabel,preFilterCapSlider); 
    	put(uniquenessRatioLabel,uniquenessRatioSlider); 
    	put(disp12MaxDiffLabel,disp12MaxDiffSlider); 
    	put(P1Label,P1Slider); 
    	put(P2Label,P2Slider); 
    	put(speckleRangeLabel,speckleRangeSlider);
    	put(speckleWindowSizeLabel,speckleWindowSizeSlider); 
    	put(threshLabel,threshSlider); 
    	put(maxvalLabel,maxvalSlider); 
    	put(ImgprocTHRESHLabel,ImgprocTHRESHSlider); 
    	put(learningRateILabel,learningRateISlider); 
    	
    	put(historyILabel,historyISlider);
    	put(nmixturesILabel,nmixturesISlider); 
    	put(backgroundRatioILabel,backgroundRatioISlider); 
    	put(noiseSigmaLabel,noiseSigmaSlider); 
    	put(varThresholdLabel,varThresholdSlider); 
    	put(bShadowDetectionILabel,bShadowDetectionISlider); 
    	*/
    	put(lowerbXLabel,lowerbXSlider);
    	put(lowerbYLabel,lowerbYSlider); 
    	put(lowerbZLabel,lowerbZSlider); 
    	put(upperbXLabel,upperbXSlider); 
    	put(upperbYLabel,upperbYSlider); 
    	put(upperbZLabel,upperbZSlider); 
    	
    }};
    
    
}

/* LinkedHashMap<JLabel,JSlider> jSladers = new LinkedHashMap<JLabel, JSlider>(){{
put(jss.minDisparitysliderLabel,jss.minDisparitySlider);
put(jss.numDisparitiesLabel,jss.numDisparitiesSlider); 
put(jss.SADWindowSizeLabel,jss.SADWindowSizeSlider); 
put(jss.preFilterCapLabel,jss.preFilterCapSlider); 
put(jss.uniquenessRatioLabel,jss.uniquenessRatioSlider); 
put(jss.disp12MaxDiffLabel,jss.disp12MaxDiffSlider); 
put(jss.P1Label,jss.P1Slider); 
put(jss.P2Label,jss.P2Slider); 
put(jss.speckleRangeLabel,jss.speckleRangeSlider);
put(jss.speckleWindowSizeLabel,jss.speckleWindowSizeSlider); 
put(jss.threshLabel,jss.threshSlider); 
put(jss.maxvalLabel,jss.maxvalSlider); 
put(jss.ImgprocTHRESHLabel,jss.ImgprocTHRESHSlider); 
put(jss.learningRateILabel,jss.learningRateISlider); 

}};*/
