package kr.ac.skuniv.Server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import kr.ac.skuniv.model.Model;

public class MainServer {
	public static void main(String args[]) {
		ServerSocket serverSocket = null;
		Model model= null;
		try{
			serverSocket = new ServerSocket(9999);
			model = new Model();
			while(true){
				Socket socket = serverSocket.accept();
				Thread thread = new PerClientThread(socket,model);
				thread.start();
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
