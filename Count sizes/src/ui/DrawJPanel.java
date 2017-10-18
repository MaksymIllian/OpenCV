package ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawJPanel extends JPanel {

	BufferedImage image;

	public Image getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public DrawJPanel(BufferedImage image) {
		this.image = image;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	}

	public void changeImage(BufferedImage image) {
		this.image = image;
		this.repaint();

	}
}