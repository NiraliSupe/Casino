package server;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image img;
	
	public ImagePanel(Image img){
		this.img = img;
	}
// Add background image to panels (page).	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.darkGray);
        this.setOpaque(true);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
