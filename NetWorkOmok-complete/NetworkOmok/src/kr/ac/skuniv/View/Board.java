package kr.ac.skuniv.View;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import kr.ac.skuniv.Model.Model;

@SuppressWarnings("serial")

public class Board extends JPanel {

	JButton surrenderbt;
	// ******************바둑판 시작좌표 및 바둑판 간격
	final int X_START = 75;
	final int Y_START = 75;
	final int INTERVAL = 50;

	Graphics g;
	Model model;
	ImageIcon backimage,panimage;
	ImageIcon black, white;
	ImageIcon player;
	ImageIcon reverse,item,surrender;
	ImageIcon hansu,fifteensecond;

	// **********************************보드판그리기
	public void paintComponent(Graphics g) {
		System.out.println("그리기");
		int x = 0, y = 0;
		// ********************************배경이미지 그리기
		g.drawImage(backimage.getImage(), 0, 0, 900, 1000, null);
		g.drawImage(panimage.getImage(), 80, 80, 740,740 , null);
		for (y = 100; y <= 800; y += 50)
			g.drawLine(100, y, 800, y);
		for (x = 100; x <= 800; x += 50)
			g.drawLine(x, 100, x, 800);
		// *******************************화점찍기
		g.fillOval(245, 245, 10, 10);
		g.fillOval(245, 645, 10, 10);
		g.fillOval(645, 245, 10, 10);
		g.fillOval(645, 645, 10, 10);
		g.fillOval(445, 445, 10, 10);
		// *******************************돌 그리기
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				// *******흑돌
				if (model.getArr(i, j) == 1) {
					g.drawImage(black.getImage(), (X_START + (INTERVAL * i)), (Y_START + (INTERVAL * j)), 50, 50, null);
				}
				// *******백돌
				if (model.getArr(i, j) == 2) {
					g.drawImage(white.getImage(), (X_START + (INTERVAL * i)), (Y_START + (INTERVAL * j)), 50, 50, null);
				}
			}
		}
		// *******************************플레이어,턴 인터페이스.
		g.setColor(Color.black);
		g.fillRect(50, 25, 150, 50);
		g.drawImage(player.getImage(), 50, 25, 50, 50, this);
		g.setColor(Color.white);
		g.setFont(new Font("굴림", Font.BOLD, 25));
		g.drawString(model.getPlayername1(), 100, 60);
		g.setColor(Color.white);
		g.fillRect(700, 25, 150, 50);
		g.drawImage(player.getImage(), 700, 25, 50, 50, this);
		g.setColor(Color.black);
		g.setFont(new Font("굴림", Font.BOLD, 25));
		g.drawString(model.getPlayername2(), 750, 60);
		// ---------------------------------------현재 턴알려주기
		if (model.getGame_turn()==2) {
			g.drawImage(white.getImage(), 350, 20, 50, 50, null);
			g.drawString("백돌 차례입니다.", 400, 50);
		} else if (model.getGame_turn()==1) {
			g.drawImage(black.getImage(), 350, 20, 50, 50, null);
			g.drawString("흑돌 차례입니다.", 400, 50);
		}
		// ---------------
//		--------------------------최근 놓은 바둑돌 위치찍어주기
			if (model.getMy_turn()==1) {
				if(model.getWhite_x()!=-1){
				g.setColor(Color.red);
				g.fillOval(((model.getWhite_x() + 2) * 50) - 5, ((model.getWhite_y() + 2) * 50) - 5, 10, 10);
				}
			} 
			else if(model.getMy_turn()==2) {
				if(model.getBlack_x()!=-1){
				g.setColor(Color.red);
				g.fillOval(((model.getBlack_x() + 2) * 50) - 5, ((model.getBlack_y() + 2) * 50) - 5, 10, 10);
				}
			}
			
		
		//-----------------------------------------------아이템인터페이스
		g.drawImage(surrender.getImage(), 250, 830, 200, 76, null);
		g.drawImage(reverse.getImage(), 500, 830, 200, 76, null);
		//-----------------------------------------------
		if(model.b_reverseCount==1)
			g.drawImage(hansu.getImage(), 20, 100, 50, 50, null);
		if(model.w_reverseCount==1)
			g.drawImage(hansu.getImage(), 820, 100, 50, 50, null);
		
		setOpaque(false);
	}

	
	
	//-------------------------------------------------------------------------

	public Board(Model model) {
		setLayout(null);
		this.model = model;
		backimage = new ImageIcon("boardBackground.png");
		panimage = new ImageIcon("badukpan2.png");
		black = new ImageIcon("blackstone.png");
		white = new ImageIcon("whitestone.png");
		player = new ImageIcon("player.png");
		surrender = new ImageIcon("surrender.png");
		reverse = new ImageIcon("reverse.png");
		item = new ImageIcon("item.png");
		hansu = new ImageIcon("hansu.png");
		fifteensecond = new ImageIcon("fifteensecond.png");
		
	}
}