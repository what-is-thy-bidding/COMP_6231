package com.web.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.ws.Endpoint;

import com.web.service.impl.systemObj;

import data_structures.*;


public class SOEN_server { 
	public static LinkedList<data_structure> soen_student_list = new LinkedList<data_structure>();//-----------------------> Contains a list of all the COMP SCI students and their data
	public static LinkedList<courses_availability> courses = new LinkedList<courses_availability>();//--------------------->List of courses available, their terms and the number of students 
	
//-------------------------------------------------HOME SERVER METHODS-------------------------------------	
	public static  void add_advisor(){	//--------------------------------> Default addition of an ADVISOR to the database
		data_structure advisor=new data_structure("SOENA");
		soen_student_list.add(advisor);
	}
	public static void add_courses(){ //--------------------------> Addition of all courses in the server
		courses_availability fall_6441 = new courses_availability("SOEN 6441", "FALL",3);
		courses_availability winter_6441 = new courses_availability("SOEN 6441", "WINTER",3);
		courses_availability summer_6441 = new courses_availability("SOEN 6441", "SUMMER",3);
		courses.add(fall_6441);
		courses.add(winter_6441);
		courses.add(summer_6441);

	}
	public static void add_student(){//---------------------------->Default addition of a STUDENT to the database
		data_structure node=new data_structure("SOENS");
		soen_student_list.add(node);
		
	}
	
	public static synchronized String delete_course(String input) {//----------->ADVISOR DELETING A COURSE
		String array[] = input.split(" ");
		//array[0] -> "add"
		//array[1] -> "COMPS/COMPA" (USERID)
		//array[2] -> "COMP"  (COURSE SUBJECT NAME)
		//array[3] -> "6231"  (COURSE CODE)
		//array[4] -> "FALL"  (TERM)
		String CourseId= array[2]+" "+array[3];
		String Term=array[4];
		
		Iterator<courses_availability> it = courses.iterator(); 
		while(it.hasNext()){
			courses_availability node = new courses_availability(CourseId, Term,0);
			courses_availability list = it.next();
			if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
				drop_course(input);
				request_comp("drop COMPS " + CourseId + " " + Term);
				request_inse("drop INSES " + CourseId + " " + Term);
				it.remove();
				
				return " The course has been deleted"; 
			}
		}
		return "COURSE DOESN'T EXIST"; 
		
		
	}
	
	
	public static synchronized String add_course(String data) {//------------------------->add courses to the student
		try{
			String [] array = data.split(" ");
		
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
		
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			int check =0;
			Iterator<courses_availability> it = courses.iterator();
			while(it.hasNext()){
				courses_availability node = new courses_availability(CourseId, Term,0);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					check=1;
					if(list.get_course_count()<list.get_course_capacity()){
						list.increase_count();
					}else{
						return UserId+": THE COURSE IS FULL IN THAT SEASON";
					}
				}
			}
			if(check==0){
				return UserId+": Course doesn't exist";
			}
			
				
			
			Iterator<data_structure> itr = soen_student_list.iterator();
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){
				
					if(CourseId.contains("SOEN")){
					
						if(Term.equals("FALL")){
							String result=list.add_courses_fall(CourseId);
							if(result.equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(result.equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
							return result;
						}else if(Term.equals("WINTER")){
							String result=list.add_courses_winter(CourseId);
							if(result.equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(result.equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
							return result;
						}else if(Term.equals("SUMMER")){
							String result=list.add_courses_summer(CourseId);
							if(result.equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(result.equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
							return result;
						}
					}
				}
			}
			return "HELLO";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}
	public static synchronized String drop_course(String data) {
		try{	
			//Increase that increase that courses availability 
	
			String [] array = data.split(" ");

			//array[0] -> "drop"
			//array[1] -> "SOENS/SOENA" (USERID)
			//array[2] -> "SOEN"  (COURSE SUBJECT NAME)
			//array[3] -> "6441"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];



			Iterator<data_structure> itr = soen_student_list.iterator();


			while(itr.hasNext()){
				data_structure list= itr.next();
				if(list.get_Id().equals(UserId)){
					if(Term.equals("FALL")){
						String check=list.drop_course_fall(CourseId);
						
						if(check.equals("COURSE DELETED")){
							Iterator<courses_availability> it = courses.iterator();
							while(it.hasNext()){
								courses_availability node = it.next();
								if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
									node.decrease_count();
								}
							}
						}
			
						return check;
			
			
					}else if(Term.equals("SUMMER")){
						String check=list.drop_course_summer(CourseId);
						if(check.equals("COURSE DELETED")){
							Iterator<courses_availability> it = courses.iterator();
							while(it.hasNext()){
								courses_availability node = it.next();
								if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
									node.decrease_count();
								}
							}
					}
			
						return check;
					}else if(Term.equals("WINTER")){
						String check=list.drop_course_winter(CourseId);
						if(check.equals("COURSE DELETED")){
							Iterator<courses_availability> it = courses.iterator();
							while(it.hasNext()){
								courses_availability node = it.next();
								if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
									node.decrease_count();
								}
							}
						}
			
						return check;
					}
					else{
						return "TERM ENTERED IS INCORRECT";
					}
				}
			}	

			return "COURSE CAN'T BE DELETED";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}


	public static synchronized String increase_availability(String data){//-------------------------------COURSE BEING DROPPED FROM DIFFERENT BACKGROUD STUDENT-----------------------------------------
		try{	
			String [] array = data.split(" ");
		
			//array[0] -> "drop"
			//array[1] -> "INSES/INSEA" (USERID)
			//array[2] -> "INSE"  (COURSE SUBJECT NAME)
			//array[3] -> "6150"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
	
			//String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			if(Term.equals("FALL")){
		
				Iterator<courses_availability> it = courses.iterator();
				while(it.hasNext()){
					courses_availability node = it.next();
					if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
						node.decrease_count();
					}
				}		
		
			}else if(Term.equals("SUMMER")){
					Iterator<courses_availability> it = courses.iterator();
					while(it.hasNext()){
						courses_availability node = it.next();
						if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
							node.decrease_count();
						}
					}

			}else if(Term.equals("WINTER")){
			
					Iterator<courses_availability> it = courses.iterator();
					while(it.hasNext()){
						courses_availability node = it.next();
						if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
							node.decrease_count();
						}
					}
			}
			else{
				return "false";
			}
			return "true";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}
	public static synchronized String decrease_availability(String data){//-------------------------------COURSE BEING ADDED FROM DIFFERENT BACKGROUD STUDENT-------------------------------------------
		try{
			String [] array = data.split(" ");
			String CourseId= array[2]+" "+array[3];// Course SUBJECT NAME+ subject code
			String Term=array[4];//Course Term
			Iterator<courses_availability> it = courses.iterator();
			int check =0;
			while(it.hasNext()){
				courses_availability node = new courses_availability(CourseId, Term,0);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					check=1;
					if(list.get_course_count()<list.get_course_capacity()){
						list.increase_count();
						return "true";
					}else{
						return "false";
					}
				}
			}
			if(check==0){
				return "course does not exist";
			}
			return "HELLO";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
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
			
			return new String(reply.getData()).trim();
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
		return "HELLO";
	}
	
	public static synchronized String add_INSE_course(String data){
		try{
			String [] array = data.split(" ");
			
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			Iterator<data_structure> itr = soen_student_list.iterator();
			
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
		
						}else if(Term.equals("WINTER")){
							return 	list.add_courses_winter(CourseId);
							
						}else if(Term.equals("SUMMER")){
							return list.add_courses_summer(CourseId);

						}
				}
			}
			
			
			return "HELLO";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}
	
	public static synchronized String drop_INSE_course(String data){
		try{	
			//Increase that increase that courses availability 
			
					String [] array = data.split(" ");
					
					//array[0] -> "drop"
					//array[1] -> "COMPS" (USERID)
					//array[2] -> "COMP"  (COURSE SUBJECT NAME)
					//array[3] -> "6231"  (COURSE CODE)
					//array[4] -> "FALL"  (TERM)
					
					String UserId= array[1];
					String CourseId= array[2]+" "+array[3];
					String Term=array[4];
					
					
					
					Iterator<data_structure> itr = soen_student_list.iterator();
					
					
					while(itr.hasNext()){
						data_structure list= itr.next();
						if(list.get_Id().equals(UserId)){
							if(Term.equals("FALL")){
								String check=list.drop_course_fall(CourseId);
								return check;							
							}else if(Term.equals("SUMMER")){
								String check=list.drop_course_summer(CourseId);
								return check;
							}else if(Term.equals("WINTER")){
								String check=list.drop_course_winter(CourseId);				
								return check;
							}
							else{
								return "TERM ENTERED IS INCORRECT";
							}
						}
					}
					
					return "COURSE CAN'T BE DELETED- COMP SERVER";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}


	public static String recieve() { 
		DatagramSocket aSocket=null;

		try{
			aSocket = new DatagramSocket(6790);
			System.out.println("SOEN Server Started....");
			while(true){
				byte[] buffer = new byte[1000];
				byte[] sendData=new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);// request received
				String input=new String(request.getData());//------->ID of the COMP student/Advisor 
				input=input.trim();			
				
				String result = "";
 
				if(input.equals("course_availability")) { 
					int i=0;
					while(i<courses.size()) {
						result+=courses.get(i).print_data()+"\n";
						i++;
					}
					sendData=result.getBytes();

				}else if((input.contains("COMPS")||input.contains("INSES")) && input.contains("add")) {
					result=decrease_availability(input);
					sendData=result.getBytes();

				}else if(input.contains("COMPS")|| input.contains("INSES") && input.contains("drop")) {
					result= increase_availability(input);
					sendData= result.getBytes();
				}else if(input.contains("SOENS") && input.contains("drop")) {
					result= drop_course(input);
				}
				
				DatagramPacket reply = new DatagramPacket(sendData, sendData.length, request.getAddress(),request.getPort());
				aSocket.send(reply);// reply sent
			}
			
			
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		return "HELLO";	
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
			
			return new String(reply.getData()).trim();
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
		return "HELLO";
	}
	
	public static synchronized String add_COMP_course(String data){
		try{
			String [] array = data.split(" "); 
			
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			Iterator<data_structure> itr = soen_student_list.iterator();
			
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
		
						}else if(Term.equals("WINTER")){
							return 	list.add_courses_winter(CourseId);
							
						}else if(Term.equals("SUMMER")){
							return list.add_courses_summer(CourseId);

						}
				}
			}
			
			
			return "HELLO";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}
	
	public static synchronized String drop_COMP_course(String data){
		try{	
			//Increase that increase that courses availability 
			String [] array = data.split(" ");
			
			//array[0] -> "drop"
			//array[1] -> "COMPS" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			
			
			Iterator<data_structure> itr = soen_student_list.iterator();
			
			
			while(itr.hasNext()){
				data_structure list= itr.next();
				if(list.get_Id().equals(UserId)){
					if(Term.equals("FALL")){
						String check=list.drop_course_fall(CourseId);
						return check;							
					}else if(Term.equals("SUMMER")){
						String check=list.drop_course_summer(CourseId);
						return check;
					}else if(Term.equals("WINTER")){
						String check=list.drop_course_winter(CourseId);				
						return check;
					}
					else{
						return "TERM ENTERED IS INCORRECT";
					}
				}
			}
			
			return "COURSE CAN'T BE DELETED- SOEN SERVER";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	
	}


	public static void main(String args[]) { 
		add_advisor(); 
		add_courses(); 
		add_student();
		try {
			systemObj obj= new systemObj();
			Endpoint endpoint = Endpoint.publish("http://localhost:9000/soen", obj);
            recieve();
            
           

			
			//System.out.println("SOFTWARE Server ready and waiting ...");

		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("SOEN Server Exiting ..."); 

	}
}