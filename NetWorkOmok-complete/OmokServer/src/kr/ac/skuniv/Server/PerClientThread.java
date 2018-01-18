package kr.ac.skuniv.Server;
import java.io.*;
import java.net.Socket;
import java.util.*;

import kr.ac.skuniv.model.Model;

public class PerClientThread extends Thread {
	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
	Socket socket;
	PrintWriter writer;
	Model model;

	PerClientThread(Socket socket, Model model) {
		this.socket = socket;
		this.model = model;
		// 누군가 접속
		try {
			writer = new PrintWriter(socket.getOutputStream());
			list.add(writer);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			System.out.println("size : " + list.size());
			if (list.size() == 1) {
				map.put("my_turn", 1);
			} else if (list.size() == 2) {
				map.put("my_turn", 2);
				
			}
			writer.println(map);
			writer.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void run() {
		String name = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			name = reader.readLine();
			if (model.namestate == false) {
				model.setPlayer1name(name);
				model.namestate = true;
			}
			else {
				model.setPlayer2name(name);
				model.setStart_state(1);
				model.namestate=false;
			}
			
			// gameStart를 알림 -> 그리고 start_state 를 2로 바꾸어줌.
			if (model.getStart_state() == 1) {
				HashMap<Object, Object> map = new HashMap<Object, Object>();
				map.put("game_state", 1);
				map.put("game_turn", 1);
				map.put("player1", model.getPlayer1name());
				map.put("player2", model.getPlayer2name());
				System.out.println("Game Start");
				sendAll(map);
				model.setStart_state(2);
			} else {
				System.out.println("send msg :  " + name);
			}
			while (true) {
				String str = reader.readLine();
				str = str.substring(1, str.length() - 1);
				String[] keyValuePairs = str.split(",");
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				System.out.println("test:"+str);
				if(str.equals("whitewin")){
					sendAll("!whitewin!");
				}
				else if(str.equals("blackwin")){
					sendAll("!blackwin!");
				}
				if(str.equals("Wwinstate")){
					sendAll("!whitewin!");
				}
				else if(str.equals("Bwinstate")){
					sendAll("!blackwin!");
				}
				for (int i = 0; i < keyValuePairs.length; i++) {
					System.out.println(i);
					String pair = keyValuePairs[i].trim();
					System.out.println("pair : " + pair);
					String[] entry = pair.split("=");
					map.put(entry[0], Integer.parseInt(entry[1]));
				}
				if(map.containsKey("Rx")){
					HashMap<Object, Object> map2 = new HashMap<Object, Object>();
					map2.put("Rx",map.get("Rx"));
					map2.put("Ry", map.get("Ry"));
					map2.put("pastX",map.get("pastX"));
					map2.put("pastY", map.get("pastY"));
					sendAll(map2);
				}
				//////
				if (map.containsKey("game_turn")) {
					int game_turn = 3 - map.get("game_turn");
					System.out.println("game_turn : " +game_turn);
					HashMap<Object, Object> map2 = new HashMap<Object, Object>();
					map2.put("game_turn", game_turn);
					map2.put("x", map.get("x"));
					map2.put("y", map.get("y"));
					sendAll(map2);
				}
				System.out.println("msg : " + str);
				if (str == null)
					break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			list.remove(writer);
			sendAll("#" + name + "님이 나가셨습니다");
			try {
				socket.close();
			} catch (Exception ignored) {
			}
		}
	}

	private void sendAll(String str) {
		for (PrintWriter writer : list) {
			writer.println(str);
			writer.flush();
		}
	}

	private void sendAll(HashMap<Object, Object> map) {
		for (PrintWriter writer : list) {
			writer.println(map);
			writer.flush();
		}
	}
}
