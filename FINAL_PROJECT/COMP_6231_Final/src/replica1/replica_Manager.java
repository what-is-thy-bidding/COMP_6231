package replica1;




/**
 * ASSUMPTIONS:
 * REPLICA 1 - WILL NEVER FAIL
 * REPLICA 2 - CAN FAIL
 */



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import request_information.request_information;



/*
 * we have to assume that one of the servers will never crash and the data within it will always be correct
 * 
 * */
public class replica_Manager {

	public static String request_inse(String command){
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=command.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 6791;	
			DatagramPacket request = new DatagramPacket(message, command.length(), aHost, serverPort);
			aSocket.send(request);
			byte [] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			return new String(reply.getData());
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close();
		}
		return null;
	}
	public static String request_soen(String command){
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=command.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 6790;	
			DatagramPacket request = new DatagramPacket(message, command.length(), aHost, serverPort);
			aSocket.send(request);
			byte [] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			return new String(reply.getData());
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close();
		}
		return null;
	}
	public static String request_comp(String command){
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=command.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 6789;	
			DatagramPacket request = new DatagramPacket(message, command.length(), aHost, serverPort);
			aSocket.send(request);
			byte [] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			return new String(reply.getData());
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close();
		}
		return null;
	}
	
	
	
	
	public static byte[] getBytes(Object request) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(request);
		
		return bos.toByteArray();

	}	
		

	
	//-----------------RECEIVING FUNCTIONS-----------------------------

	//multicast_recieve: Receives replies from the the SEQUENCER
	public static void multicast_recieve() {

			
			try {
				System.out.println("REPLICA 1 started......");
				
				byte[] incomingData = new byte[1024];

				while(true) {
					DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
					InetAddress group= InetAddress.getByName("230.1.1.5");
					MulticastSocket multicastSock= new MulticastSocket(3456);
					multicastSock.joinGroup(group);
					multicastSock.receive(incomingPacket);
					byte[] data = incomingPacket.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					try {
						request_information request = (request_information) is.readObject();
						request.set_replica(1);
						//request.set_command(request_comp("course_availability")+"\n"+request_soen("course_availability")+"\n"+request_inse("course_availability")+"\n");
						System.out.println(request.get_request()+ " this is the request sent");
						System.out.println(request.get_request_id()+ " this is the ID ");
						String result="";
						if(request.get_request().contains("COMPS") || request.get_request().contains("COMPA")) {
							
							if(request.get_request().contains("COMPA")) {
								// new course, delete course, course_availability
								
								result = request_comp(request.get_request());								
								request.set_command(result);
								

							}else if(request.get_request().contains("COMPS")) {
								// add course, drop course, course_availability, list_courses, swap course
								if(request.get_request().contains("add")) {
									result = request_comp(request.get_request());	
									if(result.contains("Course Added to the Fall semester")||result.contains("Course Added to the Winter semester")||result.contains("Course Added to the Summer semester")) {
										request.set_result(true);
									}else {
										request.set_result(false);
									}
								}
								
								if(request.get_request().contains("drop")) {
									result = request_comp(request.get_request());	
									if(result.contains("COURSE DELETED")) {
										request.set_result(true);
									}else {
										request.set_result(false);
									}
								}
								
															
								request.set_command(result);

							}
						
						}else if(request.get_request().contains("INSES")||request.get_request().contains("INSEA")) {
							
							request.set_command(request_soen(request.get_request()));

						}else if(request.get_request().contains("SOENS")|| request.get_request().contains("SOENA")) {
							request.set_command(request_soen(request.get_request()));

						}
						
						//System.out.println(request_comp("course_availability")+"\n"+request_soen("course_availability")+"\n"+request_inse("course_availability")+"\n");
						//request.set_command(request_comp("course_availability")+"\n"+request_soen("course_availability")+"\n"+request_inse("course_availability")+"\n".trim());
						//System.out.println(request.get_request()+" **************");
						udp_unicast_send(request);//send reply to the  FRONT END
						
						/*request_information test= new request_information();
						test.set_replica(1);
						test.set_command(request_comp("course_availability")+"\n"+request_soen("course_availability")+"\n"+request_inse("course_availability")+"\n");
						udp_unicast_send(test);*/
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					
					
					//request_information request= (request_information)deseralize(buffer);				
					
					/**/
					multicastSock.close();
				}
					
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	
		
	//frontEnd_receive: FRONT_END to replica
	public static void frontEnd_receive() {
		MulticastSocket aSocket=null;
		try {
			InetAddress group= InetAddress.getByName("230.1.1.6");
			aSocket=new MulticastSocket(7777);// The REPLICA port numbers
			aSocket.joinGroup(group);
			byte[] buffer= new byte[1000];
			//byte[] sendData= new byte[1000];
			while(true) {
				DatagramPacket request= new DatagramPacket(buffer, buffer.length); 
				aSocket.receive(request);
				buffer=request.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream is = new ObjectInputStream(in);
				try {
					request_information reply = (request_information) is.readObject();
					reply.set_replica(1);
					System.out.println("-reply from the FRONT END "+reply.get_request_id() +" "+reply.get_request() + " "+reply.get_replica_id());
					if(reply.FRONT_END_INSERTION) {
						udp_unicast_send(reply);//send reply to the  FRONT END
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}				
				
				
				//input="";
				//return input;
				//sendData=input.getBytes();
				//DatagramPacket reply = new DatagramPacket(sendData, sendData.length, request.getAddress(),request.getPort());
				//aSocket.send(reply);// reply sent
				
			}
			
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage()+" FRONT END");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
		

	//------------------SENDING FUNCTIONS------------------------------

	
	//pulse: sends message of replica being alive to the FRONT_END
	public static void pulse() {
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=getBytes("I AM STILL ALIVE - REPLICA 1");
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 5555;//FRONT END PORT NUMBER
			DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);
			aSocket.send(request);
			//byte [] buffer = new byte[1000];
			//DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			//aSocket.receive(reply);
			//System.out.print(new String(reply.getData()));
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close(); 
		}
		
	}
	
	
	//udp_unicast_send: sends replies to the FRONT_END
	public static void udp_unicast_send(request_information reply) {
			DatagramSocket aSocket = null;
			try{

				aSocket = new DatagramSocket();
				byte [] message=getBytes(reply);
				
					
				InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
				int serverPort = 3333;//FRONT END PORT NUMBER
				DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);
				aSocket.send(request);
				//byte [] buffer = new byte[1000];
				//DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				//aSocket.receive(reply);
				//System.out.print(new String(reply.getData()));
			}
			catch(SocketException e){
				System.out.println("Socket: "+e.getMessage());
			}
			catch(IOException e){
				e.printStackTrace();
				System.out.println("IO: "+e.getMessage());
			}
			finally{
				if(aSocket != null) aSocket.close(); 
			}
		}
		
		
	
	
	
	public static void main(String args[]) {
	    System.out.println("SEQUENCER end receiver has been started.....");
		Runnable task1 = () -> {
            multicast_recieve();//Receive a call from the sequencer
        };
        Thread thread = new Thread(task1);
        thread.start();
       
        
        System.out.println("FRONT end receiver has been started.....");
	    Runnable task2 = () -> {
	    	frontEnd_receive();//Receive a call from the front end
        };
        Thread thread2 = new Thread(task2);
        thread2.start();
        
		// the pulse thread
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleAtFixedRate(new Runnable() {
	        @Override
	        public void run() {
	            pulse();
	        }
	    }, 0, 1, TimeUnit.SECONDS);
        //Receive a call from the SERVERS
	   
	    
	    
	}
}
