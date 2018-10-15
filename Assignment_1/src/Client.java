package Assignment_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * COURSE ADDED BY THE ADVISOR MUST BE OF THE 6000 LEVEL
 * 
 * 
 * List of things left to do:
 * 
 *COURSES TO BE DELETED BY THE ADVISOR. 
 * 
 * */

/*
 * Port number for COMP_Server=6789
 * Port number for SOEN_Server=6790
 * Port number for INSE_Server=6791 
 * 
 * */


public class Client {
	

	
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

	
	public static void new_course(String userId, String courseId){
		String information = "add "+userId+" "+courseId;
		if(userId.contains("COMPA")){
			System.out.println(request_comp(information));
		}else if(userId.contains("SOENA")){
			System.out.println(request_soen(information));
		}else if(userId.contains("INSEA")){
			System.out.println(request_inse(information));
		}
	}
	
	public static void delete_course(String userId, String courseId){
		String information = "drop " + userId+" "+courseId;
		if(userId.contains("COMPA")){
			System.out.println(request_comp(information));
		}else if(userId.contains("SOENA")){
			System.out.println(request_soen(information));
		}else if(userId.contains("INSEA")){
			System.out.println(request_inse(information));
		}
	}
	
	
	
	public static void add_course(String student_Id, String course_Id ){
		String information = "add "+student_Id+" "+course_Id;
		if(student_Id.contains("COMP")){
			System.out.println(request_comp(information));
		}else if(student_Id.contains("SOEN")){
			System.out.println(request_soen(information));
		}else if(student_Id.contains("INSE")){
			System.out.println(request_inse(information));
		}
	}
	
	public static void drop_course(String student_Id, String course_Id ){
		String information = "drop "+student_Id+" "+course_Id;
		
		if(student_Id.contains("COMP")){
			System.out.println(request_comp(information));
		}else if(student_Id.contains("INSE")){
			System.out.println(request_inse(information));
		}else if(student_Id.contains("SOEN")){
			System.out.println(request_soen(information));
		}
		
	}


	
	public static void course_availability() {
		System.out.println("COURSES AVAILABLE");
		System.out.println("------COURSE NAME -----" + "----TERM ----"+"SEATS----");
		System.out.println(request_comp("course_availability"));
		System.out.println(request_inse("course_availability"));
		System.out.println(request_soen("course_availability"));


		
	}
	
	
	public static void student_menu(String userId){
		
		Scanner sc=new Scanner(System.in);
		System.out.println("ENTER CHOICE : ");
		System.out.println("1. ADD COURSE.");
		System.out.println("2. DROP COURSE.");
		System.out.println("3. Check Availability");
		System.out.println("0. LOGOUT.");

	    int ch = sc.nextInt();
	    while(ch!=0){
	    	switch(ch){
	    		case 1: course_availability();// A Method to Print all the courses that are available 
	    									  //with the terms in which they are available in
	    			
	    				System.out.println("------------------------------ \n");
	    				System.out.print("WHICH COURSE_ID TO ADD AND WHICH TERM(eg. COMP 6231 FALL): ");
	    				sc=new Scanner(System.in);
	    				String course_Id = sc.nextLine();
	    				add_course(userId, course_Id );
	    			    ch=4;
	    			    break;
	    		
	    		case 2: 
	    			
	    				if(userId.contains("COMP")){
	    					System.out.print(request_comp(userId));
	    				}else if(userId.contains("SOEN")){
	    					System.out.print(request_soen(userId));
	    				}else if(userId.contains("INSE")){
	    					System.out.print(request_inse(userId));
	    				}
    					
    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE_ID TO DROP AND WHICH TERM(eg. COMP 6231 FALL): ");
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				drop_course(userId, course_drop);
	    				ch=4;
	    				break;
	    		
	    		case 3: course_availability();
	    		
	    		default:
	    				if(userId.contains("COMP")){
	    					System.out.print(request_comp(userId));
	    				}else if(userId.contains("SOEN")){
	    					System.out.print(request_soen(userId));
	    				}else if(userId.contains("INSE")){
	    					System.out.print(request_inse(userId));
	    				}
	    					
	    				
	    				System.out.println("ENTER CHOICE : ");
	    				System.out.println("1. ADD COURSE.");
	    				System.out.println("2. DROP COURSE.");
	    				System.out.println("3. Check Availability");
	    				System.out.println("0. LOGOUT.");
						sc=new Scanner(System.in);

	    				ch = sc.nextInt();	
	    				break;
	    		
	    	}
	    	
	    }
	    System.out.println(" LOGGING OUT ");
	    
	    sc.close();

	  

		
	}
	
	public static void advisor_menu(String userId){
		
		Scanner sc=new Scanner(System.in);
		System.out.println("ENTER CHOICE : ");
		System.out.println("1. ADD A NEW COURSE.");
		System.out.println("2. DELETE A COURSE.");
		System.out.println("3.CHECK AVAILABILITY");
		System.out.println("0. LOGOUT.");

	    int ch = sc.nextInt();
	    while(ch!=0){
	    	switch(ch){
	    		case 1:	    			
	    				System.out.println("------------------------------ \n");
	    				System.out.print("WHICH COURSE WOULD YOU LIKE TO ADD AND WHICH TERM(eg. COMP 6231 FALL): ");
	    				sc=new Scanner(System.in);
	    				String course_Id = sc.nextLine();
	    				new_course(userId, course_Id );
	    			    ch=4;
	    			    break;
	    		
	    		case 2: 

    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE WOULD YOU LIKE TO DELETE AND WHICH TERM(eg. COMP 6231 FALL): ");
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				delete_course(userId, course_drop);
	    				ch=4;
	    				break;
	    				
	    		case 3: 
	    				course_availability();
	    				ch=4;
    					break;
	    		
	    		default:
	    				/*if(userId.contains("COMP")){
	    					System.out.print(request_comp(userId));
	    				}else if(userId.contains("SOEN")){
	    					System.out.print(request_soen(userId));
	    				}else if(userId.contains("INSE")){
	    					System.out.print(request_inse(userId));
	    				}*/
	    					
	    				
	    				System.out.println("ENTER CHOICE : ");
	    				System.out.println("1. ADD A NEW COURSE.");
	    				System.out.println("2. DELETE A COURSE.");
	    				System.out.println("3.CHECK AVAILABILITY");
	    				System.out.println("0. LOGOUT.");
						sc=new Scanner(System.in);

	    				ch = sc.nextInt();	
	    				break;
	    		
	    	}
	    	
	    }
	    System.out.println(" LOGGING OUT ");
	    
	    sc.close();

	  

		
	}
	


	//-------------------CALLING THE INSE SERVER------------------------------------------------------
	
	public static void inse_server_call(String user){
		
		
		if(user.contains("INSEA")){	//If the user is an ADVISOR
			System.out.println(request_inse(user));
			advisor_menu(user);
		}

		else if(user.compareTo("INSES")==0){//If the user is a STUDENT
			String check = request_inse(user);
			
			if(check.compareTo("Incorrect Input")!=0){
				System.out.println("Welcome "+user);	// "WELCOME USER MESSAGE"
				System.out.println(request_inse(user));//The student's current list of courses appear
				student_menu(user); // Student gets a menu of what he wants to do, add or drop a course
				
			}else{
				System.out.print("Incorrect Input");
			}
		}		
		
	}
	
	//-------------------CALLING THE SOEN SERVER------------------------------------------------------
	public static void soen_server_call(String user){
		
		
		if(user.contains("SOENA")){	//If the user is an ADVISOR
			System.out.println(request_soen(user));
			advisor_menu(user);
		}

		else if(user.compareTo("SOENS")==0){//If the user is a STUDENT
			String check = request_soen(user);
			
			if(check.compareTo("Incorrect Input")!=0){
				System.out.println("Welcome "+user);	// "WELCOME USER MESSAGE"
				System.out.println(request_soen(user));//The student's current list of courses appear
				student_menu(user); // Student gets a menu of what he wants to do, add or drop a course
				
			}else{
				System.out.print("Incorrect Input");
			}
		}

		
		
		
	}

	//-------------------CALLING THE COMP SERVER------------------------------------------------------
	public static void comp_server_call(String user){
		
		
		if(user.contains("COMPA")){	//If the user is an ADVISOR
			System.out.println(request_comp(user));
			advisor_menu(user);
		}

		else if(user.compareTo("COMPS")==0){//If the user is a STUDENT
			String check = request_comp(user);
			
			if(check.compareTo("Incorrect Input")!=0){
				System.out.println("Welcome "+user);	// "WELCOME USER MESSAGE"
				System.out.println(request_comp(user));//The student's current list of courses appear
				student_menu(user); // Student gets a menu of what he wants to do, add or drop a course
				
			}else{
				System.out.print("Incorrect Input");
			}
		}

		
		
		
	}
	
	
	
	//-------------------MAIN FUNCTION----------------------------------------------------------------
	public static void main(String[] args) {
		System.out.print("Enter Id : ");
		Scanner sc = new Scanner(System.in);
		String user= sc.nextLine();
		
		if(user.contains("COMP")) {//COMP 
			 comp_server_call(user);			 
			 sc.close();
			
		}else if(user.contains("INSE")) {//INSE 
			inse_server_call(user);
			sc.close();
		}
		else if(user.contains("SOEN")) {// SOEN 
			soen_server_call(user);
			sc.close();
		}else {
			System.out.print("Incorrect Id");
			sc.close();
		}

		
		
		
		
	}
}
