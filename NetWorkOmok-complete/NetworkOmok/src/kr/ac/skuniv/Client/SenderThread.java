package kr.ac.skuniv.Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import kr.ac.skuniv.Model.Model;

public class SenderThread extends Thread{
	Socket socket;
	PrintWriter writer;
	String name;
	Model model;
	public SenderThread(Socket socket,String name, Model model,PrintWriter writer){
		this.socket=socket;
		this.name=name;
		this.model = model;
		this.writer = writer;
	}
	public void run(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			writer.println(name);
			writer.flush();
			
			while(true){
				String str= reader.readLine();
				if(str.equals("bye"))
					break;
				writer.println(str);
				writer.flush();
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally{
			try{
				socket.close();
			}
			catch(Exception ignored){}
		}
	}
}
