package kr.ac.skuniv.Controller;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

import kr.ac.skuniv.Model.Model;
import kr.ac.skuniv.View.Board;
import kr.ac.skuniv.View.Controller;
import kr.ac.skuniv.View.MainFrame;

public class Viewcontroller {	
	MainFrame frame ;
	Model model;
	Socket socket;
	Board board;
	PrintWriter writer;
	public Viewcontroller(MainFrame frame, Model model,Socket socket,PrintWriter writer, Board board) {
		this.frame=frame;
		this.model = model;
		this.socket = socket;
		this.writer = writer;
		this.board = board;
/////////////////////////////////////////////////
		
		frame.contentPane.add(board);
		frame.setPreferredSize(new Dimension(900, 1000));// 프레임크기 지정
		frame.setLocation(500, 20);// 프레임 실행 위치 설정
		frame.contentPane.add(board);
		board.addMouseListener(new Controller(model, board, frame, writer));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	
}
