package kr.ac.skuniv.View;
import java.awt.Graphics;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GameIntro extends JPanel {
	MainFrame frame;
	ImageIcon back;
	JTextField namefield;
	ImageIcon title;
	
	
	
	
	public void paintComponent(Graphics g){
		g.drawImage(back.getImage(), 0, 0, 900, 1000, null);
		g.drawImage(title.getImage(), 50, 50, 800, 500, null);
	}
	
	public GameIntro(MainFrame frame){
		this.frame = frame;
		setLayout(null);
		back=new ImageIcon("introBackground.jpg");
		title = new ImageIcon("networktitle.png");
		
		setOpaque(false);
	}
}
