package ui;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import core.CoreLogic;
import main.Main;
import main.OutputMain;
import main.runCalibration;

public class ButtonFrame extends JFrame implements ActionListener, ItemListener {
	/**
	 * 
	 */
	public DrawJElements djes = new DrawJElements();
	private static final long serialVersionUID = 1L;
	private JButton bShowHidePanels, bShowHideFrames, bCalibRun, bStatShowHide; // reference
	private Checkbox cCalcMode, cStageMode, cNewMode; // to

	// the button
	// object

	public boolean getCalcMode() {
		return cCalcMode.getState();
	}

	public boolean getStageMode() {
		return cStageMode.getState();
	}

	public boolean getNewMode() {
		return cNewMode.getState();
	}

	String tShowHidePanels = "Show/Hide panels",
			tShowHideFrames = "Show/Hide frames",
			tCalibRun = "CalibrationRunAndSaveToFile",
			tStatShowHide = "Statistic Show/Hide";

	String tCalcMode = "CalcMode", tStageMode = "StageMode",
			tNewMode = "NewMode";

	// constructor for ButtonFrame
	public ButtonFrame(String title) {
		super(title); // invoke the JFrame constructor
		setLayout(new FlowLayout()); // set the layout manager

		bShowHidePanels = new JButton(tShowHidePanels); // construct a JButton
		bShowHideFrames = new JButton(tShowHideFrames);
		bCalibRun = new JButton(tCalibRun);
		bStatShowHide = new JButton(tStatShowHide);

		cCalcMode = new Checkbox(tCalcMode);
		cStageMode = new Checkbox(tStageMode);
		cNewMode = new Checkbox(tNewMode);

		bShowHidePanels.addActionListener(this);
		bShowHideFrames.addActionListener(this);
		bCalibRun.addActionListener(this);
		bStatShowHide.addActionListener(this);

		cCalcMode.addItemListener(this);
		cStageMode.addItemListener(this);
		cNewMode.addItemListener(this);

		add(bShowHidePanels); // add the button to the JFrame
		add(bShowHideFrames);
		add(bStatShowHide);
		add(bCalibRun);

		add(cCalcMode);
		add(cStageMode);
		add(cNewMode);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent arg0) {

		String actionCommand = arg0.getActionCommand();
		if (actionCommand.equals(tShowHidePanels)) {
			if (getContentPane().getBackground() == Color.red) {
				getContentPane().setBackground(Color.blue);

			} else
				getContentPane().setBackground(Color.red);

			/*
			 * djes.drawPanelFrameDisparityFirstPart.setVisible(!djes.
			 * drawPanelFrameDisparityFirstPart.isVisible());
			 * djes.drawPanelFrameDisparitySecondPart
			 * .setVisible(!djes.drawPanelFrameDisparitySecondPart.isVisible());
			 * djes
			 * .drawPanelFrameFilter.setVisible(!djes.drawPanelFrameFilter.isVisible
			 * ());
			 * djes.drawPanelFrameMog.setVisible(!djes.drawPanelFrameMog.isVisible
			 * ()); djes.jPanelFrameDisparityFirstPart.setVisible(djes.
			 * drawPanelFrameDisparityFirstPart.isVisible());
			 * djes.jPanelFrameDisparitySecondPart
			 * .setVisible(djes.drawPanelFrameDisparitySecondPart.isVisible());
			 * djes
			 * .jPanelFrameFilter.setVisible(djes.drawPanelFrameFilter.isVisible
			 * ());
			 * djes.jPanelFrameMog.setVisible(djes.drawPanelFrameMog.isVisible
			 * ());
			 */
			djes.jPanelFrameDisparityFirstPart
					.setVisible(!djes.jPanelFrameDisparityFirstPart.isVisible());
			djes.jPanelFrameDisparitySecondPart
					.setVisible(!djes.jPanelFrameDisparitySecondPart
							.isVisible());
			djes.jPanelFrameFilter.setVisible(!djes.jPanelFrameFilter
					.isVisible());
			djes.jPanelFrameMog.setVisible(!djes.jPanelFrameMog.isVisible());
		} else if (actionCommand.equals(tShowHideFrames)) {
			djes.jLeftFrame.setVisible(!djes.jLeftFrame.isVisible());
			djes.jRightFrame.setVisible(!djes.jRightFrame.isVisible());
			djes.jDisparityFrame.setVisible(!djes.jDisparityFrame.isVisible());
			if (djes.jDisparityFrame.isVisible()) {
				CoreLogic.om = new OutputMain();
				CoreLogic.om.start();
			}
		} else if (actionCommand.equals(tCalibRun)) {
			djes.jCalibLeftFrame.setVisible(!djes.jCalibLeftFrame.isVisible());
			djes.jCalibRightFrame
					.setVisible(!djes.jCalibRightFrame.isVisible());

			if (djes.jCalibRightFrame.isVisible()) {
				CoreLogic.rC = new runCalibration();
				CoreLogic.rC.start();
			}

		} else if (actionCommand.equals(tStatShowHide)) {
			Main.jsf.setVisible(!Main.jsf.isVisible());

		}
		repaint();
	}

	public void itemStateChanged(ItemEvent arg0) {

		System.out.println(this.getCalcMode());
		System.out.println(this.getStageMode());
		System.out.println(this.getNewMode());
		repaint();
	}
}