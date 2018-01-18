package kr.ac.skuniv.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import kr.ac.skuniv.Controller.Viewcontroller;
import kr.ac.skuniv.Model.Model;
import kr.ac.skuniv.View.Board;
import kr.ac.skuniv.View.GameIntro;
import kr.ac.skuniv.View.MainFrame;

public class ReceiverThread extends Thread {
	Socket socket;
	Model model;
	Board board;
	MainFrame frame;
	PrintWriter writer;
	GameIntro intro;
	public ReceiverThread(Socket socket, Model model, Board board, MainFrame frame, PrintWriter writer){
		this.socket=socket;
		this.model = model;
		this.board = board;
		this.frame = frame;
		this.writer = writer;
	}
	public void run(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				String str = reader.readLine();
				str = str.substring(1, str.length()-1);
				System.out.println("str : " +str);
				String[] keyValuePairs = str.split(",");              
				HashMap<Object,Object> map = new HashMap<Object,Object>();
				
				if(str.equals("whitewin")){
					frame.play();
					frame.endgame(" ���! ������ �����մϴ�.", board, board);
				}
				else if(str.equals("blackwin")){
					frame.play();
					frame.endgame(" ���! ������ �����մϴ�.", board, board);
				}
				
/////////////////////////////////////////////////
				
				for(int i = 0 ; i < keyValuePairs.length; i++)                        
				{
					String pair = keyValuePairs[i].trim();
				    String[] entry = pair.split("=");
			    	map.put(entry[0], entry[1]);
				}
/////////////////////////////////////////////////
				System.out.println("check : " + map.get("game_turn"));
				if(map.containsKey("Rx")){ //�ؽ��ʿ� ����Ʈ �������� ��ٴ� ������ ���������
					int x= Integer.parseInt(map.get("Rx").toString()); //�ֱ� ���� ��ǥ���� �޾ƿ�
					int y= Integer.parseInt(map.get("Ry").toString()); // "
					model.setArr(0, x, y); //�ֱ� ���� ��ǥ���� ���� ������
					model.clicksound();
					if(model.getMy_turn()==1){//���� ���̰�
						if(model.getMy_turn()==model.getGame_turn()){//������ �������� ������ ������
						model.w_reverseCount=0; //���� ������ ������ ī��Ʈ ����
						model.setGame_turn(2); //�������� �ٽ� ������
						model.setWhite_x(Integer.parseInt(map.get("pastX").toString()));//�ֱ� ���� ��ǥX���� ���������� ���� �ֱٿ� ���� ��ǥ������ ��ȯ
						model.setWhite_y(Integer.parseInt(map.get("pastY").toString()));//�ֱ� ���� ��ǥY���� ���������� ���� �ֱٿ� ���� ��ǥ������ ��ȯ
						}
						else if(model.getMy_turn()!=model.getGame_turn())//���� ������
							model.setGame_turn(1); //�������� �ٽ� ������
						
					}
					else if(model.getMy_turn()==2){
						if(model.getMy_turn()==model.getGame_turn()){
							model.b_reverseCount=0;
							model.setGame_turn(1);
							model.setBlack_x(Integer.parseInt(map.get("pastX").toString()));
							model.setBlack_y(Integer.parseInt(map.get("pastY").toString()));
						}
						else if(model.getMy_turn()!=model.getGame_turn())
							model.setGame_turn(2);
						
					}
					
					board.repaint();
				}
/////////////////////////////////////////////////
				if (map.containsKey("my_turn")) {
					//turn�� ���� ���ʸ� ��Ÿ���ִ� ����
					int turn = Integer.parseInt(map.get("my_turn").toString());
					System.out.println("my turn : " + turn);
					model.setMy_turn(turn);
				}
				else if (map.containsKey("game_turn")) {
					// game�� ���۵Ǿ��ٴ� ���� �˸��� �޼����� ���� ����
					if (map.containsKey("player1")||map.containsKey("player2")){
						String name=String.valueOf(map.get("player1"));
						String name2=String.valueOf(map.get("player2"));
						if(name instanceof String)
							System.out.println("�´�!!!!!");
						System.out.println("�̰� ����@@@@@@@@@@@@@@@");
						model.setPlayername1(name);
						model.setPlayername2(name2);
					}
					if (map.containsKey("game_state")) {
						frame.contentPane.removeAll();
						new Viewcontroller(frame, model,socket, writer,board);
						frame.introbgmstop();
						model.playdaeguk();
						model.playstartdarguk();
					}
					//turn�� ���� ���ʸ� ��Ÿ���ִ� ����
					int game_turn = Integer.parseInt(map.get("game_turn").toString());
					model.setGame_turn(game_turn);
					if (map.containsKey("x")) {						
						int x = Integer.parseInt(map.get("x").toString());
						int y = Integer.parseInt(map.get("y").toString());
						if ((3 - game_turn) != model.getMy_turn()) {
							if(model.getMy_turn()==1){
								model.setWhite_x(x);
								model.setWhite_y(y);
							}
							else if(model.getMy_turn()==2){
								model.setBlack_x(x);
								model.setBlack_y(y);
							}
							model.setArr(3-game_turn, x, y);
							model.stonesound();
							board.repaint();
						}
					}
				}
				else {					
					System.out.println("receive msg:"+str);
				}
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
	
