package sequencer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
/*
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import systemApp.*;
import front_end.corbaObj;*/

import request_information.request_information;






public class sequencer {
	
	
	static File file1= new File("frontEnd_requests.txt");
	static int request_id=0;

	public static byte[] getBytes(Object request) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(request);
		
		return bos.toByteArray();

	}
	
	
	
	
	public static void write_to_file(request_information record) throws IOException {
		try {
			Writer output= null;
			output= new BufferedWriter(new FileWriter(file1, true));
			output.write("\n");
			output.write("REQUEST_NUMBER= "+record.get_request_id()+" \n");
			output.write("COMMAND= " + record.get_request()+" \n");
			output.close();
		}catch(Exception e) {
			System.out.println("COULD NOT CREATE FILE");
		}

	}
	
	
	

	
	public static void send_multicast_request(String command, int id) {
		try {
			request_information request= new request_information(id, command);// data structure that is to be sent
			//System.out.println(request.FRONT_END_INSERTION);
			InetAddress group = InetAddress.getByName("230.1.1.5");
			MulticastSocket multicastSock= new MulticastSocket();
			byte[] data= getBytes(request);// function that returns the byte array
			DatagramPacket packet= new DatagramPacket(data, data.length, group, 3456);
			multicastSock.send(packet);
			write_to_file(request);// writing to a file, to keep a log of all the requests previously done.
			multicastSock.close();
        }catch(Exception e) {
        	e.printStackTrace(); 
        }
	}

	

	// no need to change this since it is in the same machine
	public static void udp_unicast_receiver(){
		DatagramSocket aSocket=null;
		try {
			System.out.println("UDP unicast listener Started.... SEQUENCER");
			aSocket=new DatagramSocket(2222);// receiver of port from the FRONT END
			//byte[] sendData= new byte[1000];
			while(true) {
				byte[] buffer= new byte[1000];
				DatagramPacket request= new DatagramPacket(buffer, buffer.length); 
				aSocket.receive(request);
				String input= new String(request.getData());
				input=input.trim();
				System.out.println(input+"  -REQUEST from the FRONT END / request number= "+request_id);
				send_multicast_request(input, request_id);
				request_id++;
				 
			} 
			
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage()+" SEQUENCER");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null) 
				aSocket.close(); 
		}

		
	}
	
	
	
	
	public static void main(String args[])  {
		/***
		 * A UDP UNICAST listener to receive all the messages from the FRONT END
		 * A MULTICAST sender to send messages to all the REPLICA MANAGERS 
		 */
		Runnable task1 = () -> { 
			udp_unicast_receiver();
        };
        Thread thread = new Thread(task1);
        thread.start(); 
        
        
		//-----------------------------------------------------------------
		
		
			
           
	

	}
	
}
