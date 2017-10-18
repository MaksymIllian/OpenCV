package ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import core.ParametersCalculation;

public class JStatisticFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8027881475080024389L;
	TextField tField0;
	TextField tField1;
	TextField tField2;
	TextField tField3;
	TextField tField4;
	TextField tField5;
	TextField tField6;
	TextField tField7;
	TextField tField8;
	JLabel label0;
	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;
	JLabel label6;
	JLabel label7;
	JLabel label8;
	JButton bSave,bExport;
	JButton bImport;
	String tSave = "Save to File", tExport = "Export", tImport = "Import";

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		String actionCommand = arg0.getActionCommand();
		if (actionCommand.equals(tImport)) {
			tField0.setText(ParametersCalculation.getRealh() + "");
			tField1.setText(ParametersCalculation.getRealw() + "");
			tField2.setText(ParametersCalculation.getRealz() + "");
			tField3.setText(ParametersCalculation.getH() + "");
			tField4.setText(ParametersCalculation.getW() + "");
			tField5.setText(ParametersCalculation.getZ() + "");
			tField6.setText(ParametersCalculation.getK1() + "");
			tField7.setText(ParametersCalculation.getK2() + "");
			tField8.setText(ParametersCalculation.getK3() + "");
		} else if (actionCommand.equals(tSave)) {
			if (getContentPane().getBackground() == Color.orange)
				getContentPane().setBackground(Color.green);
			else
				getContentPane().setBackground(Color.orange);

			try {
				ParametersCalculation.ReadAndWrite();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (actionCommand.equals(tExport)) {

			ParametersCalculation
					.setRealh(Double.parseDouble(tField0.getText()));
			ParametersCalculation
					.setRealw(Double.parseDouble(tField1.getText()));
			ParametersCalculation
					.setRealz(Double.parseDouble(tField2.getText()));
			ParametersCalculation.setH(Double.parseDouble(tField3.getText()));
			ParametersCalculation.setW(Double.parseDouble(tField4.getText()));
			ParametersCalculation.setZ(Double.parseDouble(tField5.getText()));
			ParametersCalculation.setK1(Double.parseDouble(tField6.getText()));
			ParametersCalculation.setK2(Double.parseDouble(tField7.getText()));
			ParametersCalculation.setK3(Double.parseDouble(tField8.getText()));
		}
		repaint();
	}
	public double getTextRealH()
	{
		String text = tField0.getText();
		if(text.length()>0)
			return Double.parseDouble(tField0.getText());
		else
			return 1;
	}
	public double getTextRealW()
	{
		String text = tField1.getText();
		if(text.length()>0)
			return Double.parseDouble(tField1.getText());
		else
			return 1;
	}
	public JStatisticFrame(String title) throws HeadlessException {
		super(title); // invoke the JFrame constructor
		setLayout(new FlowLayout()); // set the layout manager
		this.tField0 = new TextField(5);
		this.tField1 = new TextField(5);
		this.tField2 = new TextField(5);
		this.tField3 = new TextField(5);
		this.tField4 = new TextField(5);
		this.tField5 = new TextField(5);
		this.tField6 = new TextField(5);
		this.tField7 = new TextField(5);
		this.tField8 = new TextField(5);
		label0 = new JLabel("Real h");
		label1 = new JLabel("Real w");
		label2 = new JLabel("Real z");
		label3 = new JLabel("Calc h");
		label4 = new JLabel("Calc w");
		label5 = new JLabel("Calc z");
		label6 = new JLabel("Calc k1");
		label7 = new JLabel("Calc k2");
		label8 = new JLabel("Calc k3");
		bExport = new JButton(tExport);
		bImport = new JButton(tImport);
		bSave = new JButton(tSave); // construct a JButton
		bExport.addActionListener(this);
		bImport.addActionListener(this);bSave.addActionListener(this);
		add(label0);
		add(tField0);
		add(label1);
		add(tField1);
		add(label2);
		add(tField2);
		add(label3);
		add(tField3);
		add(label4);
		add(tField4);
		add(label5);
		add(tField5);
		add(label6);
		add(tField6);
		add(label7);
		add(tField7);
		add(label8);
		add(tField8);
		add(bExport);
		add(bImport);
		add(bSave); // add the button to the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
