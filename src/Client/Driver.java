package Client;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class Driver {
	//Writers
		PrintWriter w1;
		PrintWriter w2;
		PrintWriter w3;
		PrintWriter w4;
		
		//For convenience in accessing channels; will contain our writers above
		ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
		
		//Readers that will be passed to a separate thread of execution each
		BufferedReader r1;
		BufferedReader r2;
		BufferedReader r3;
		BufferedReader r4;
		
		int nodeNum;
		
		// RicartAgrawala algorithm object for the node
		RicartAgrawalaAlg me;
		
		int numberOfWrites;
		int writeLimit = 200; // number of times to try CS
		int csDelay = 200; //wait delay time

		/** Start the driver, with a number of channels specified. **/
		public Driver(String args[])
		{
			System.out.println("\n\n");
			final boolean desireToHarmHumansOrThroughInactionAllowHumansToComeToHarm = false; 

			nodeNum = Integer.parseInt(args[0]);
			
			numberOfWrites = 0;

			// Set up our sockets with our peer nodes
			try
			{
				ServerSocket ss1;
				ServerSocket ss2;
				ServerSocket ss3;
				ServerSocket ss4;
				Socket s1;
				Socket s2;
				Socket s3;	
				Socket s4;	
				
				if(nodeNum == 1)
				{				
					System.out.println("Node 1 here");
					ss1 = new ServerSocket(4400); //ServerSocket for 02
					ss2 = new ServerSocket(4401); //ServerSocket for 03
					ss3 = new ServerSocket(4402); //ServerSocket for 04
					ss4 = new ServerSocket(4403); //ServerSocket for 05
					s1 = ss1.accept();
					s2 = ss2.accept();
					s3 = ss3.accept();
					s4 = ss4.accept();
				}
				else if(nodeNum == 2)
				{
					System.out.println("Node 2 here");
					s1 = new Socket("dc01.utdallas.edu", 4400); //ClientSocket for 01
					ss2 = new ServerSocket(4401); //ServerSocket for 03
					ss3 = new ServerSocket(4402); //ServerSocket for 04
					ss4 = new ServerSocket(4403); //ServerSocket for 05
					s2 = ss2.accept();
					s3 = ss3.accept();
					s4 = ss4.accept();
				}
				else if(nodeNum == 3)
				{
					System.out.println("Node 3 here");
					s1 = new Socket("dc01.utdallas.edu", 4401); //ClientSocket for 01
					s2 = new Socket("dc02.utdallas.edu", 4401); //ClientSocket for 02
					ss3 = new ServerSocket(4402); //ServerSocket for 04
					ss4 = new ServerSocket(4403); //ServerSocket for 05
					s3 = ss3.accept();
					s4 = ss4.accept();
				}
				else if(nodeNum == 4)
				{
					System.out.println("Node 4 here");
					s1 = new Socket("dc01.utdallas.edu", 4402); //ClientSocket for 01
					s2 = new Socket("dc02.utdallas.edu", 4402); //ClientSocket for 02
					s3 = new Socket("dc03.utdallas.edu", 4402); //ClientSocket for 03
					ss4 = new ServerSocket(4403); //ServerSocket for 05
					s4 = ss4.accept();
				}
				else
				{
					System.out.println("Node 5 here");
					s1 = new Socket("dc01.utdallas.edu", 4403); //ClientSocket for 01
					s2 = new Socket("dc02.utdallas.edu", 4403); //ClientSocket for 02
					s3 = new Socket("dc03.utdallas.edu", 4403); //ClientSocket for 03
					s4 = new Socket("dc04.utdallas.edu", 4403); //ClientSocket for 04
				}
				
				System.out.println("Created all sockets");
				
				//With the sockets done, create our readers and writers
				w1 = new PrintWriter(s1.getOutputStream(), true);
				w2 = new PrintWriter(s2.getOutputStream(), true);
				w3 = new PrintWriter(s3.getOutputStream(), true);
				w4 = new PrintWriter(s4.getOutputStream(), true);
				r1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
				r2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
				r3 = new BufferedReader(new InputStreamReader(s3.getInputStream()));
				r4 = new BufferedReader(new InputStreamReader(s3.getInputStream()));
				
				
				//store the writers in a list
				outputStreams.add(w1);
				outputStreams.add(w2);
				outputStreams.add(w3);
				outputStreams.add(w4);
				
				// Create the ME object with priority of 'nodeNum' and initial sequence number 0
				me = new RicartAgrawalaAlg(nodeNum, 0, this);
				me.w[0] = w1;
				me.w[1] = w2;
				me.w[2] = w3;
				me.w[3] = w4;
				
				
				Thread t1 = new Thread(new ChannelHandler(s1));
				t1.start();
				
				Thread t2 = new Thread(new ChannelHandler(s2));
				t2.start();
				
				Thread t3 = new Thread(new ChannelHandler(s3));
				t3.start();
				
				Thread t4 = new Thread(new ChannelHandler(s4));
				t4.start();
				
			}
			catch(Exception ex){ ex.printStackTrace();}

			
			while(numberOfWrites < writeLimit)
			{
				try{
					System.out.println("Requesting critical section...");
					requestCS();
					numberOfWrites++;
					Random num = new Random();
					Thread.sleep(num.nextInt(500));
					//Thread.sleep(csDelay);
				}
				catch(InterruptedException e){
					System.out.println(e.getMessage());
				}
			}
		}

		/** Invocation of Critical Section*/
		public boolean criticalSection(int nodeNum, int numberOfWrites)
		{
			System.out.println("Node " + nodeNum + " entered critical section");
			

		      try {
		            // connect to the servers
		        	Socket socket1 = new Socket("dc06.utdallas.edu", 4406);
		            Socket socket2 = new Socket("dc07.utdallas.edu", 4406);
		            Socket socket3 = new Socket("dc08.utdallas.edu", 4406);
		        
		            try {
		                
	                    
		            	
		                // DataOutputStream
		                DataOutputStream out1 = new DataOutputStream(socket1
		                        .getOutputStream());
		                DataOutputStream out2 = new DataOutputStream(socket2
		                        .getOutputStream());
		                DataOutputStream out3 = new DataOutputStream(socket3
		                        .getOutputStream());
		               
		                out1.writeUTF(nodeNum + "," + me.seqNum);
		                out2.writeUTF(nodeNum + "," + me.seqNum);
		                out3.writeUTF(nodeNum + "," + me.seqNum);
		                
		                
		             // DataInputStream
		                DataInputStream in1 = new DataInputStream(socket1.getInputStream());
		                  String accpet1 = in1.readUTF();
		                   System.out.println("read from server:"+ accpet1); 
		                   
		                
		                
		 
		            } finally {
		                socket1.close();
		                socket2.close();
		                socket3.close();
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }



			

						return true;
		} 
		
		/**
		* Interface method between Driver and RicartAgrawalaAlg class
		*/
		public void requestCS()
		{

			me.invocation();
			
			//After invocation returns, we can safely call CS
			criticalSection(nodeNum, numberOfWrites);
			
			//Once we are done with CS, release CS
			me.releaseCS();
		}


		public void broadcast(String message)
		{
			for(int i = 0; i < outputStreams.size(); i++)
			{
				try
				{
					PrintWriter writer = outputStreams.get(i);
					writer.println(message);
					writer.flush();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		
		
		/**
		* Given a socket, it continuously reads from the 
		* socket and passes key information to the ME object.
		*/
		class ChannelHandler implements Runnable
		{
			BufferedReader reader;
			PrintWriter writer;
			Socket sock;
		
			public ChannelHandler(Socket s)
			{
				try
				{
					sock = s;
					InputStreamReader iReader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(iReader);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		
			/** Continuously runs and reads all incoming messages, passing messages to ME */

			public void run()
			{
				String message;
				
				try
				{
					//reader is open,we will take action when a message arrives.
					while(( message = reader.readLine() ) != null)
					{
						System.out.println("Node " + nodeNum + " received message: " + message);
						
						//Tokenize our message to determine RicartAgrawala step
						
						String tokens[] = message.split(",");
						String messageType = tokens[0];
						
						if(messageType.equals("REQUEST"))
						{
							/*We are receiving request(j,k) where j is a seq# and k a node#.
							  This call will decide to defer or ack with a reply. */
							me.receiveRequest(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
						}
						else if(messageType.equals("REPLY"))
						{
							me.receiveReply();
						}
					}
				
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		

		
		public static void main(String[] args) 
		{
			new Driver(args);	
		}

}
