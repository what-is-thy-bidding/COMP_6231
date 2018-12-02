package front_end;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import request_information.request_information;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import systemApp.*;




/***
 * 
 * @author a_isht
 *THINGS LEFT TO DO
 *
 *
 *
 */

/*
 * for doing the things mentioned above make sure that the backup in the file is machine is
 * readable.
 * 
 * */


class replica_replies{
	public int request_number;
	public boolean replica_1=false;
	public boolean replica_2=false;
	public boolean replica_3=false;
	public boolean replica_4=false;
	public request_information REPLICA1_reply;
	public request_information REPLICA2_reply;
	public request_information REPLICA3_reply;
	public request_information REPLICA4_reply;
	
}
 

public class FRONT_END {
	//A CORBA implementation that will communicate with the client (request and reply)
	//A priority queue that will contain all the java threads(client requests)
	//A UDP listener: that will get all the replies from the replicas and 
	//				  compare their results
	//A comparison of all the replies from the UDP replies 
	
	/*public static boolean pulse1=false;
	public static boolean pulse2=false;
	
	public static boolean pulse3=false;
	public static boolean pulse4=false;*/
	
	static File file1= new File("frontEnd_requests.txt");
	static int request_id=0;
	public static String result=""; 
	//public static boolean skip=true;
	public static int check_id=0; 
	public static int execution=0;
	public static int software_fail_execution=0;
	public static boolean check= true;
	public static LinkedList<replica_replies> replies= new LinkedList<replica_replies>();
	public static LinkedList<Integer> software_failure_id=new LinkedList<Integer>();

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
	

	
	public static void read_from_file(int replica_number, request_information[] array) throws IOException {
		
		BufferedReader br= new BufferedReader(new FileReader(file1));
		String st;			
		request_information request= new request_information();
		while((st=br.readLine())!=null) {
			if(st.contains("REQUEST_NUMBER=")) {
				st= st.replaceAll("REQUEST_NUMBER=", "");
				st= st.trim();
				int request_id= Integer.parseInt(st);
				System.out.println(request_id);
				request.set_request_id(request_id);
				st=br.readLine();
				st= st.replaceAll("COMMAND=", "");
				st= st.trim();
				request.set_command(st);
				System.out.println("RESULT FROM READING THE FILE "+request.get_request() + " "+request.get_request_id());				
				
				
				if(replica_number==1) {
					for(int i=0;i<array.length;i++) {
						if(array[i].get_request_id()==request.get_request_id()) {
								request.FRONT_END_INSERTION=true;
								break;
							}else {
								request.FRONT_END_INSERTION=false;
							}
					}
					send_replica1(request);

						
						
				
				}else if(replica_number==2) {
					
					
					for(int i=0;i<array.length;i++) {
						if(array[i].get_request_id()==request.get_request_id()) {
								request.FRONT_END_INSERTION=true;
								break;
							}else {
								request.FRONT_END_INSERTION=false;
							}
					}
					send_replica2(request);
					
						
				}else if(replica_number==3) {
					for(int i=0;i<array.length;i++) {
						if(array[i].get_request_id()==request.get_request_id()) {
								request.FRONT_END_INSERTION=true;
								break;
							}else {
								request.FRONT_END_INSERTION=false;
							}
					}
					send_replica3(request);
				
				}else if(replica_number==4) {
					for(int i=0;i<array.length;i++) {
						if(array[i].get_request_id()==request.get_request_id()) {
								request.FRONT_END_INSERTION=true;
								break;
							}else {
								request.FRONT_END_INSERTION=false;
							}
					}
					send_replica4(request);
				}
			
			}
		}
		
		
		br.close();
		
	}

	
	public static byte[] getBytes(Object request) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(request);
		
		return bos.toByteArray();

	}	
	
	 
	
	// if the size of the list has gotten more than 3, then there is an error
	public static boolean check_server_response() throws IOException {
		
		
		if(replies.size()>=3) {// one of the servers is not responding
			int replica_1=0, replica_2=0,replica_3=0, replica_4=0;
			for(int i=0; i<replies.size();i++) {
				if(!replies.get(i).replica_1) {// replica 1 has not sent a message
					replica_1++;
				}else if(!replies.get(i).replica_2) {//replica 2 has not sent a message
					replica_2++;
				}else if(!replies.get(i).replica_3) {
					replica_3++;
				}else if(!replies.get(i).replica_4) {
					replica_4++;
				}
				
				if(replica_1>=3) {
					
					System.out.println("REPLICA 1 IS NOT RESPONSIVE");
					String check=pulse_receiver();
					while(true) {
						System.out.println(check+ " this is the response "+check.contains("REPLICA 1"));
						check=pulse_receiver();
						check=check.trim();
						if(check.contains("REPLICA 1")) {
							request_information [] array= new request_information[replies.size()];
							for(int j=0;j<array.length;j++) {
								request_information req= new request_information();
								req.set_request_id(replies.get(j).REPLICA2_reply.get_request_id());
								array[j]=req;// array containing all the failed requests
								//System.out.println(array[j].get_request_id()+"********************");
							}
							if(execution<1)	{
								read_from_file(1, array);
								execution=1;
							}
								
							
							break;
						}
					}
					System.out.println("THE REPLICA 1 HAS BEEN REACTIVATED");
					break;
					
				}else if(replica_2>=3) {
					System.out.println("REPLICA 2 IS NOT RESPONSIVE");
					String check=pulse_receiver();
					
					while(true) {
						System.out.println(check+ " this is the response "+check.contains("REPLICA 2"));
						check=pulse_receiver();
						check=check.trim();
						if(check.contains("REPLICA 2")) {
							request_information [] array= new request_information[replies.size()];
							for(int j=0;j<array.length;j++) {
								request_information req= new request_information();
								req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
								array[j]=req;
								//System.out.println(array[j].get_request_id()+"********************");
							}
							if(execution<1)	{
								System.out.println("THE CODE HAS REACHED HERE");
								read_from_file(2, array);
								execution =1;
							}
							break;
						}
						
					}
					System.out.println("THE REPLICA 2 HAS BEEN REACTIVATED");
					
					
					
					break;
				}else if(replica_3>=3) {
					System.out.println("REPLICA 3 IS NOT RESPONSIVE");
					String check=pulse_receiver();
					while(true) {
						System.out.println(check+ " this is the response "+check.contains("REPLICA 3"));
						check=pulse_receiver();
						check=check.trim();
						if(check.contains("REPLICA 3")) {
							request_information [] array= new request_information[replies.size()];
							for(int j=0;j<array.length;j++) {
								request_information req= new request_information();
								req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
								array[j]=req;
								//System.out.println(array[j].get_request_id()+"********************");
							}
							if(execution<1)	{
								read_from_file(3, array);
								execution=1;

							}
							break;
						}
						
					}
					System.out.println("THE REPLICA 3 HAS BEEN REACTIVATED");
					break;
				
				
				}else if(replica_4>=3) {
					System.out.println("REPLICA 4 IS NOT RESPONSIVE");
					String check=pulse_receiver();
					while(true) {
						System.out.println(check+ " this is the response "+check.contains("REPLICA 4"));
						check=pulse_receiver();
						check=check.trim();
						if(check.contains("REPLICA 4")) {
							request_information [] array= new request_information[replies.size()];
							for(int j=0;j<array.length;j++) {
								request_information req= new request_information();
								req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
								array[j]=req;
								//System.out.println(array[j].get_request_id()+"********************");
							}
							if(execution<1)	{
								read_from_file(4, array);
								execution=1;
							}
							break;
						}
						
					}
					System.out.println("THE REPLICA 4 HAS BEEN REACTIVATED");
					break;
				}
			}
			//execution=0;// CHANGE THIS CODE IF NOT WORKING FROM THE CRASH
			
		}
		
		return false;
	}
	 
	
	 
	
	public static  void check_replies(request_information reply) throws IOException {
		System.out.println("CODE HAS REACHED HERE");
		System.out.println(reply.get_replica_id()+" ----THE REPLICA ID" );
		System.out.println(reply.get_request()+ " ----THE REQUEST");
		System.out.println(reply.get_request_id()+" -----THE REQUEST NUMBER");
		System.out.println(replies.size()+" ----INITIAL SIZE OF THE LIST");
		System.out.println(replies.isEmpty()+" ----CHECKING IF THE LIST IS EMPTY");
		System.out.println(reply.FRONT_END_INSERTION+ " -------- checking insertion");
		System.out.println(reply.get_result() + "---------- checking the result boolean");
		
		
		if(replies.isEmpty()) { // if the list is empty
			replica_replies node=new replica_replies();
			if(reply.get_replica_id()==1 && reply.FRONT_END_INSERTION==true) {
				node.REPLICA1_reply= reply;
				node.request_number= reply.get_request_id();
				node.replica_1=true;
				replies.add(node);
				System.out.println("1st insertion");
			}else if(reply.get_replica_id()==2 && reply.FRONT_END_INSERTION==true) {
				node.REPLICA2_reply= reply;
				node.request_number= reply.get_request_id();
				node.replica_2=true;
				replies.add(node);
				System.out.println("1st insertion");
			}else if(reply.get_replica_id()==3 && reply.FRONT_END_INSERTION==true) {
				node.REPLICA3_reply= reply;
				node.request_number= reply.get_request_id();
				node.replica_3=true;
				replies.add(node);
				System.out.println("1st insertion");
			}else if(reply.get_replica_id()==4 && reply.FRONT_END_INSERTION==true) {
				node.REPLICA4_reply= reply;
				node.request_number= reply.get_request_id();
				node.replica_4=true;
				replies.add(node);
				System.out.println("1st insertion");
			}/**/
			
		}else {// if the list contains at least 1 node
			boolean check=false;
			for(int i=0;i<replies.size(); i++) {
				if(replies.get(i).request_number==reply.get_request_id() && reply.FRONT_END_INSERTION==true) {// reply has a request number matches then it means that REPLICA1_reply or REPLICA2_reply is already inserted
					check=true;
					if(reply.get_replica_id()==1) {
						replies.get(i).REPLICA1_reply=reply;
						replies.get(i).replica_1=true;
						System.out.println("2nd insertion");
						break;
					}else if(reply.get_replica_id()==2 && reply.FRONT_END_INSERTION==true) {
						replies.get(i).REPLICA2_reply=reply;
						replies.get(i).replica_2=true;
						System.out.println("2nd insertion");
						break;
					}else if(reply.get_replica_id()==3 && reply.FRONT_END_INSERTION==true) {
						replies.get(i).REPLICA3_reply=reply;
						replies.get(i).replica_3=true;
						System.out.println("2nd insertion");
						break;
					}else if(reply.get_replica_id()==4 && reply.FRONT_END_INSERTION==true) {
						replies.get(i).REPLICA4_reply=reply;
						replies.get(i).replica_4=true;
						System.out.println("2nd insertion");
						break;
					}/**/
					
					
				}
			
			}
			if(check==false && reply.FRONT_END_INSERTION==true) {// this is the 1st copy of the reply
				System.out.println(" new node is being inseted");
				replica_replies node=new replica_replies();
				
				
				if(reply.get_replica_id()==1 && reply.FRONT_END_INSERTION==true) {
					node.REPLICA1_reply= reply;
					node.request_number= reply.get_request_id();
					node.replica_1=true;
					replies.addLast(node);
				}else if(reply.get_replica_id()==2 && reply.FRONT_END_INSERTION==true) {
					node.REPLICA2_reply= reply;
					node.request_number= reply.get_request_id();
					node.replica_2=true;
					replies.addLast(node);
				}else if(reply.get_replica_id()==3 && reply.FRONT_END_INSERTION==true) {
					node.REPLICA3_reply= reply;
					node.request_number= reply.get_request_id();
					node.replica_3=true;
					replies.addLast(node);
				}else if(reply.get_replica_id()==4 && reply.FRONT_END_INSERTION==true) {
					node.REPLICA4_reply= reply;
					node.request_number= reply.get_request_id();
					node.replica_4=true;
					replies.addLast(node);
				}/**/
					
			}
			
		}
		
		
		print_list();
		final_result();
		
		if(replies.size()>3 && software_failure_id.size()!=replies.size()) { // CRASH FAILURE
			check_server_response();
		}else if(replies.size()>3 && software_failure_id.size()!=replies.size()) {
			// SOFTWARE FAILURE
			int replica_id=software_failure_id.get(0);
			check_software_failure(replica_id);
		}
			
		
	}
	
	
	public static void check_software_failure(int replica_id) throws IOException {
		request_information  request= new request_information();
		request.set_replica(replica_id);
		request.set_command("false");
		if(replica_id==1) {
			send_replica1(request);
			
			request_information [] array= new request_information[replies.size()];
			for(int j=0;j<array.length;j++) {
				request_information req= new request_information();
				req.set_request_id(replies.get(j).REPLICA2_reply.get_request_id());
				array[j]=req;// array containing all the failed requests
				//System.out.println(array[j].get_request_id()+"********************");
				if(software_fail_execution<1) {
					read_from_file(replica_id,array);
					software_fail_execution=1;

				}
			
			}
		}else if(replica_id==2) {
			send_replica2(request);
			request_information [] array= new request_information[replies.size()];
			for(int j=0;j<array.length;j++) {
				request_information req= new request_information();
				req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
				array[j]=req;// array containing all the failed requests
				//System.out.println(array[j].get_request_id()+"********************");
				if(software_fail_execution<1) {
					read_from_file(replica_id,array);
					software_fail_execution=1;

				}

			}
		}else if(replica_id==3) {
			send_replica3(request);
			request_information [] array= new request_information[replies.size()];
			for(int j=0;j<array.length;j++) {
				request_information req= new request_information();
				req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
				array[j]=req;// array containing all the failed requests
				//System.out.println(array[j].get_request_id()+"********************");
				if(software_fail_execution<1) {
					read_from_file(replica_id,array);
					software_fail_execution=1;

				}

			}
		}else if(replica_id==4) {
			send_replica4(request);
			request_information [] array= new request_information[replies.size()];
			for(int j=0;j<array.length;j++) {
				request_information req= new request_information();
				req.set_request_id(replies.get(j).REPLICA1_reply.get_request_id());
				array[j]=req;// array containing all the failed requests
				//System.out.println(array[j].get_request_id()+"********************");
				if(software_fail_execution<1) {
					read_from_file(replica_id,array);
					software_fail_execution=1;
				}

			}
		}
		
		
		
		
		
	}



	private static void print_list() {
		System.out.println("---------------PRINTING THE DATA IN THE LIST---------------------");
		for(int i=0; i<replies.size();i++) {
			System.out.println(replies.get(i).request_number+ " ***REQUEST NUMBER");
			System.out.println(i+" loop");
			if(replies.get(i).replica_1) {
				System.out.println("REPLICA 1 DATA: REQUEST- "+replies.get(i).REPLICA1_reply.get_request()+", REPLICA ID- "+ replies.get(i).REPLICA1_reply.get_replica_id()+", REPLICA BOOLEAN- " +replies.get(i).replica_1);

			}
			if(replies.get(i).replica_2) {
				System.out.println("REPLICA 2 DATA: REQUEST- "+replies.get(i).REPLICA2_reply.get_request()+", REPLICA ID- "+ replies.get(i).REPLICA2_reply.get_replica_id()+", REPLICA BOOLEAN- " +replies.get(i).replica_2);
			}
			System.out.println();	
			if(replies.get(i).replica_3) {
				System.out.println("REPLICA 3 DATA: REQUEST- "+replies.get(i).REPLICA3_reply.get_request()+", REPLICA ID- "+ replies.get(i).REPLICA3_reply.get_replica_id()+", REPLICA BOOLEAN- " +replies.get(i).replica_3);
			}
			System.out.println();	
			if(replies.get(i).replica_4) {
				System.out.println("REPLICA 4 DATA: REQUEST- "+replies.get(i).REPLICA4_reply.get_request()+", REPLICA ID- "+ replies.get(i).REPLICA4_reply.get_replica_id()+", REPLICA BOOLEAN- " +replies.get(i).replica_4);
			}
			System.out.println();/*	*/
		}
		
		System.out.println("---------------end of list---------------------");

	}


	

	//-----------------RECEIVING FUNCTIONS-----------------------------
	
	//Receive form the REPLICAS
	public static void udp_unicast_receiver(){
		
		MulticastSocket aSocket=null;
		try {
			aSocket=new MulticastSocket(3333);// The REPLICA port numbers
			aSocket.joinGroup(InetAddress.getByName("230.1.1.6"));
			byte[] buffer= new byte[10000];
			//byte[] sendData= new byte[1000];
			while(true) {
				DatagramPacket request= new DatagramPacket(buffer, buffer.length); 
				aSocket.receive(request);
				buffer=request.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream is = new ObjectInputStream(in);
				try {
					request_information reply = (request_information) is.readObject();
					
					/*if(reply.get_request_id()!=check_id) {// a new request_id is there------> fail for multi-threaded test cases
							if(replies.size()>=3) {
								check_server_response();
								check_id =reply.get_request_id();
							}	
					}*/
					
					check_replies(reply);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}				

			}
			
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage()+" FRONT END");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage()+" FRONT END");
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
	
	
	
	
	//Receive from the REPLICAS
	public static String pulse_receiver() {
		MulticastSocket aSocket=null;
		try {
			InetAddress group= InetAddress.getByName("230.1.1.5");
			aSocket=new MulticastSocket(5555);// The REPLICA port numbers
			aSocket.joinGroup(group);
			byte[] buffer= new byte[1000];
				DatagramPacket request= new DatagramPacket(buffer, buffer.length); 
				aSocket.receive(request);
				buffer=request.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream is = new ObjectInputStream(in);
				return (String)is.readObject();
			
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage()+" FRONT END");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		return "false";
	}
	
	
	
	//------------------SENDING FUNCTIONS------------------------------

	//Send to the SEQUENCER
	
	//send_sequencer_request: sends requests to the sequencer
	public static void send_sequencer_request(String input) {
		
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=input.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 2222;// The SEQUENCER PORT NUMBER	
			DatagramPacket request = new DatagramPacket(message, input.length(), aHost, serverPort);
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
	
	
	
	
	
	
	//Send to the REPLICAS
	public static void send_replica1(request_information backup) {// IN THE CASE THAT replica1 has Crashed!
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=getBytes(backup);
			InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
			int serverPort = 7777;//FRONT END PORT NUMBER
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
	
	public static void send_replica2(request_information backup) {// IN THE CASE THAT replica2 has Crashed!
		System.out.println("SEND TO REPLICA 2");
		MulticastSocket aSocket = null;
		try{		

			aSocket = new MulticastSocket();
			byte [] message=getBytes(backup);
			InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
			aSocket.joinGroup(aHost);
			int serverPort = 6666;//FRONT END PORT NUMBER
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
	
	public static void send_replica3(request_information backup) {// IN THE CASE THAT replica1 has Crashed!
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=getBytes(backup);
			InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
			int serverPort = 8888;//FRONT END PORT NUMBER
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
	
	public static void send_replica4(request_information backup) {// IN THE CASE THAT replica1 has Crashed!
		DatagramSocket aSocket = null;
		try{		

			aSocket = new DatagramSocket();
			byte [] message=getBytes(backup);
			InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
			int serverPort = 9999;//FRONT END PORT NUMBER
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
	
	public static  String final_result() {
		//if both the booleans are true then compare the results and send the common answer
		// find the size of the list and if the ones in the beginning aren't getting a reply then send error messages for the previous requests
		for(int i=0; i<replies.size(); i++) {
			if(replies.get(i).replica_1==true && replies.get(i).replica_2==true && replies.get(i).replica_3==true && replies.get(i).replica_4==true) {
				//COUNTING THE NUMBER OF MAJORITY results
				int count=1;
				if(replies.get(i).REPLICA1_reply.result==replies.get(i).REPLICA2_reply.result) {
					count++;
				}else {
					software_failure_id.add(2);
				}
				
				if(replies.get(i).REPLICA1_reply.result==replies.get(i).REPLICA3_reply.result) {
					count++;
				}else {
					software_failure_id.add(3);
				}
				
				if(replies.get(i).REPLICA1_reply.result==replies.get(i).REPLICA4_reply.result) {
					count++;
				}else {
					software_failure_id.add(4);
				}
				
				
				if(count==4) {
					//if(replies.get(i).REPLICA1_reply.get_request().equals(replies.get(i).REPLICA2_reply.get_request())) {// this is if the reply that have arrived match each other
					System.out.println(" THE REQUEST REPLIES CAN BE SENT TO THE CLIENT");
					System.out.println(replies.get(i).REPLICA1_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA1_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA2_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA2_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA3_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA3_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA4_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA4_reply.get_replica_id());

					result= replies.get(i).REPLICA1_reply.get_request();					
				//}
					replies.remove(i);
					System.out.println(replies.size()+ " the size of the list after deletion");
				}else if(count==3) {
					System.out.println(" THE REQUEST REPLIES CAN BE SENT TO THE CLIENT");
					System.out.println(replies.get(i).REPLICA1_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA1_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA2_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA2_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA3_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA3_reply.get_replica_id());
					System.out.println(replies.get(i).REPLICA4_reply.get_request() + " - REPLICA NUMBER "+replies.get(i).REPLICA4_reply.get_replica_id());

					result= replies.get(i).REPLICA1_reply.get_request();
				}
				
				//String final_result=result;
				//result="&&&&";
				//System.out.println(" final_result -> "+ final_result+ " new res -> "+ result);
				//return final_result;
				//break; 
			}
		}
		
		return result;
	}

	
	public static void send_replica3_special_requests(String backup) {// IN THE CASE THAT replica1 has Crashed!
		DatagramSocket aSocket = null;
		try{		
			
			aSocket = new DatagramSocket();
			byte [] message=getBytes(backup);
			InetAddress aHost = InetAddress.getByName("230.1.1.6"); 
			int serverPort = 1010;//FRONT END PORT NUMBER
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
	
	public static void execute_request() {
		send_replica3_special_requests("COMPS1010");		
		
	}
	
	
	public static void main(String[] args) throws IOException {
		//execute_request();
		System.out.println("THE frontEnd_requests.txt is being deleted");
		    PrintWriter writer= new PrintWriter(file1);
		    writer.print("");
		    writer.close();
		
		System.out.println("UDP unicast listener Started.... FRONT_END");
		Runnable task1 = () -> { 
			udp_unicast_receiver();
        };
        Thread thread = new Thread(task1);
        thread.start(); 
        	
        	
        		
            
        
       
       
        
         System.out.println("input a request? ");
        Scanner sc= new Scanner(System.in);
        String input=sc.nextLine();
        
        while(input!="END") {
        	
        	request_information information= new request_information();
    		information.set_command(input);
    		information.set_request_id(request_id);
    		request_id++;
    		//write_to_file(information);
        	send_sequencer_request(input);
            System.out.println("input a request? ");
             input=sc.nextLine();
        	 
        } 
        sc.close();
  
   
        /**/
       
        /* ORB orb = ORB.init(args, null);
		// get reference to rootpoa &amp; activate
		try {
		 	
			// the POAManager
			//-ORBInitialPort 1050 -ORBInitialHost localhost
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			systemObj sysobj =  new systemObj();
			sysobj.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(sysobj);
			system href = systemHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("FRONT_END");
			ncRef.rebind(path, href);

			System.out.println("FRONT END CORBA ready and waiting ...");

			// wait for invocations from clients 
				orb.run(); 
			
			 
		}
		  

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out); 
		}*/
        
		System.out.println("HelloServer Exiting ..."); 
		
	}
}
