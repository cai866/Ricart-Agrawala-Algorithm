package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class server1 {
	 private int port = 4406;// default port number
	 
	    public server1() {
	    }
	 
	    // initiate the server port number
	    public server1(int port) {
	        this.port = port;
	    }
	 

	    public void service() {
	        try {        
	            ServerSocket server = new ServerSocket(port);
	           
	            while (true) {
	                Socket socket = server.accept();
	            
	                new Thread(new ServerThread(socket)).start();
	            }
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	    public static void main(String[] args) {
	        new server1().service();
	    }
	}
	 
	 
	class ServerThread implements Runnable {
	    private Socket socket;
	 
	    public ServerThread(Socket socket) {
	        this.socket = socket;
	    }
	 
	    // offer a service for the client
	    @Override
	    public void run() {
	        try {
	            try {    	
                	 
	            	DataInputStream in = new DataInputStream(socket
	                        .getInputStream());
	            	String accpet = in.readUTF();
                    System.out.println("receive from client:"+ accpet);
	                
	                File file = new File("/home/010/d/dx/dxg180004/ObjectFile.txt");
	                      	              
	                   //write the file
	                  FileWriter fw = new FileWriter(file, true);
	                   PrintWriter pw = new PrintWriter(fw);
	                    pw.println(accpet);
	                    pw.flush();
	                	fw.flush();
	                	pw.close();
	                	fw.close(); 
	              
	               	DataOutputStream out = new DataOutputStream(socket
		                       .getOutputStream());
	                	ReadFile rf=new ReadFile();
	                	String s=rf.readFile(file);
		                out.writeUTF(s);
	                	
	            } finally {
	            	//close the socket;
	                socket.close();
	                
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}
	 
	 
