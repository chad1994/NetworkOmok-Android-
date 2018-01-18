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
					frame.endgame(" 백승! 게임을 종료합니다.", board, board);
				}
				else if(str.equals("blackwin")){
					frame.play();
					frame.endgame(" 흑승! 게임을 종료합니다.", board, board);
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
				if(map.containsKey("Rx")){ //해쉬맵에 리버트 아이템을 썼다는 정보가 들어있을때
					int x= Integer.parseInt(map.get("Rx").toString()); //최근 놓은 좌표값을 받아옴
					int y= Integer.parseInt(map.get("Ry").toString()); // "
					model.setArr(0, x, y); //최근 놓은 좌표값의 돌을 물러줌
					model.clicksound();
					if(model.getMy_turn()==1){//내가 흑이고
						if(model.getMy_turn()==model.getGame_turn()){//리버스 아이템을 상대방이 썼을때
						model.w_reverseCount=0; //상대방 리버스 아이템 카운트 감소
						model.setGame_turn(2); //게임턴을 다시 돌려줌
						model.setWhite_x(Integer.parseInt(map.get("pastX").toString()));//최근 놓은 좌표X값을 리버스한후 가장 최근에 놓은 좌표값으로 변환
						model.setWhite_y(Integer.parseInt(map.get("pastY").toString()));//최근 놓은 좌표Y값을 리버스한후 가장 최근에 놓은 좌표값으로 변환
						}
						else if(model.getMy_turn()!=model.getGame_turn())//내가 썼을때
							model.setGame_turn(1); //내턴으로 다시 가져옴
						
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
					//turn은 나의 차례를 나타내주는 변수
					int turn = Integer.parseInt(map.get("my_turn").toString());
					System.out.println("my turn : " + turn);
					model.setMy_turn(turn);
				}
				else if (map.containsKey("game_turn")) {
					// game이 시작되었다는 것을 알리는 메세지가 오면 실행
					if (map.containsKey("player1")||map.containsKey("player2")){
						String name=String.valueOf(map.get("player1"));
						String name2=String.valueOf(map.get("player2"));
						if(name instanceof String)
							System.out.println("맞다!!!!!");
						System.out.println("이거 위에@@@@@@@@@@@@@@@");
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
					//turn은 나의 차례를 나타내주는 변수
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
	
