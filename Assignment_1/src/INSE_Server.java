package Assignment_1;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Iterator;


public class INSE_Server {
	//List of courses in INFROMATION SECURITY =  INSE 6110, INSE 6150
	//Each course has a seat capacity of 2
	//Both courses are available in FALL, WINTER and SUMMER
	
	static LinkedList<data_structure> inse_student_list = new LinkedList<data_structure>();//-----------------------> Contains a list of all the INSE students and their data
	static LinkedList<courses_availability> courses = new LinkedList<courses_availability>();//--------------------->List of courses available, their terms and the number of students
	
	public static  void add_advisor(){	//--------------------------------> Default addition of an ADVISOR to the database
		data_structure advisor=new data_structure("INSEA");
		inse_student_list.add(advisor);
	}
	public static void add_courses(){ //--------------------------> Addition of all courses in the server
		
		courses_availability fall_6110 = new courses_availability("INSE 6110", "FALL");
		courses_availability winter_6110 = new courses_availability("INSE 6110", "WINTER");
		courses_availability summer_6110 = new courses_availability("INSE 6110", "SUMMER");
		courses.add(fall_6110);
		courses.add(winter_6110);
		courses.add(summer_6110);
		
		courses_availability fall_6150 = new courses_availability("INSE 6150", "FALL");
		courses_availability winter_6150 = new courses_availability("INSE 6150", "WINTER");
		courses_availability summer_6150 = new courses_availability("INSE 6150", "SUMMER");
		courses.add(fall_6150);
		courses.add(winter_6150);
		courses.add(summer_6150);

	}
	public static void add_student(){//---------------------------->Default addition of a STUDENT to the database
		data_structure node=new data_structure("INSES");
		inse_student_list.add(node);
	
	}
	
	private static String new_course(String data) { //------------->ADVISOR ADDING A NEW COURSE
		try{
			String [] array = data.split(" ");
		
			//array[0] -> "add"
			//array[1] -> "INSEA" (USERID)
			//array[2] -> "INSE"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
	
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			Iterator <courses_availability> it = courses.iterator();
			while(it.hasNext()){
				courses_availability node = it.next();
				if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
					return "Course already exists in that term";
				}
			}
			
			courses.add(new courses_availability(CourseId, Term));
			return "Course has been added";
			
			
			
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}
	
	
	
	private static String add_course(String data) {//------------------------->add courses to the student
		try{
			String [] array = data.split(" ");
		
			//array[0] -> "add"
			//array[1] -> "INSES/INSEA" (USERID)
			//array[2] -> "INSE"  (COURSE SUBJECT NAME)
			//array[3] -> "6150"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
		
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
		
			Iterator<courses_availability> it = courses.iterator();
			while(it.hasNext()){
				courses_availability node = new courses_availability(CourseId, Term);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					if(list.get_course_count()<2){
						list.increase_count();
					}else{
						return "THE COURSE IS FULL IN THAT SEASON";
					}
				}
			}
	
			Iterator<data_structure> itr = inse_student_list.iterator();
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
			
				if(list.get_Id().equals(node.get_Id())){
				
					if(CourseId.contains("INSE")){
					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
						}else if(Term.equals("WINTER")){
							return list.add_courses_winter(CourseId);
						}else if(Term.equals("SUMMER")){
							return list.add_courses_summer(CourseId);
						}
					}	
				}
			}
			return null;
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
		
	}
	
	private static String drop_course(String data) {
		try{
			//Increase that increase that courses availability 
		
			String [] array = data.split(" ");
	
			//array[0] -> "drop"
			//array[1] -> "INSES/INSEA" (USERID)
			//array[2] -> "INSE"  (COURSE SUBJECT NAME)
			//array[3] -> "6150"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
	
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
	
	
	
			Iterator<data_structure> itr = inse_student_list.iterator();
	
	
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
	
	private static String decrease_availability(String data){//-------------------------------COURSE BEING ADDED FROM DIFFERENT BACKGROUD STUDENT-------------------------------------------
		try{	
			String [] array = data.split(" ");
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			Iterator<courses_availability> it = courses.iterator();
			while(it.hasNext()){
				courses_availability node = new courses_availability(CourseId, Term);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					if(list.get_course_count()<2){
						list.increase_count();
						//return "COURSE AVAILABILITY HAS BEEN DECREASES BY 1";
						return "true";
					}else{
						return "false";
					}
				}
			}
			return "true";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}

	private static String increase_availability(String data){//-------------------------------COURSE BEING DROPPED FROM DIFFERENT BACKGROUD STUDENT-----------------------------------------
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
	}
	
	//-------------------------------------------------------CALLING THE SOEN SERVER and ADDITION/DROP OF SOEN COURSE------------------------------------------------------------------------------------	
	
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
	private static String add_SOEN_course(String data){
			String [] array = data.split(" ");
			
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			Iterator<data_structure> itr = inse_student_list.iterator();
			
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
						}else if(Term.equals("WINTER")){
							return list.add_courses_winter(CourseId);
						}else if(Term.equals("SUMMER")){
							return list.add_courses_summer(CourseId);
						}
				}
			}
			
			
			return null;
			
		}
	private static String drop_SOEN_course(String data){
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
					
					
					
					Iterator<data_structure> itr = inse_student_list.iterator();
					
					
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
			
			
		}
		

	//--------------------------------------------------------CALLING THE SOEN SERVER and ADDITION/DROP OF COMP COURSE-----------------------------------------------------------------------------------	
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
	
	private static String add_COMP_course(String data){
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
			Iterator<data_structure> itr = inse_student_list.iterator();
		
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
			
				if(list.get_Id().equals(node.get_Id())){					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
	
						}else if(Term.equals("WINTER")){
							return list.add_courses_winter(CourseId);
						
						}else if(Term.equals("SUMMER")){
							return list.add_courses_summer(CourseId);

						}
				}
			}
		
		
			return null;
			
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
		
	}
	private static String drop_COMP_course(String data){
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
				
				
				
				Iterator<data_structure> itr = inse_student_list.iterator();
				
				
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
	
	
	private static String course_availability() {
		Iterator<courses_availability> it = courses.iterator();
		String data="";
		while(it.hasNext()){
			data=data+it.next().print_data()+"\n";
		}
		return data;
	}
	
	public static String extract_student_information(String UserId){    //---------------------------->Extracts Student Information
		String info="";
		Iterator<data_structure> it= inse_student_list.iterator();
		while(it.hasNext()){
			data_structure node= it.next();
			if(UserId.compareTo(node.get_Id())==0){
				info=node.print();
				return info;
			}
		}
		return UserId+" Id not found";
	}
	
	public static void main(String[] args){
		DatagramSocket aSocket=null;
		add_advisor();
		add_courses();
		add_student();
		

		
		
		try{
			aSocket = new DatagramSocket(6791);
			System.out.println("INSE Server Started....");
			while(true){
				byte[] buffer = new byte[1000];
				byte[] sendData=new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);// request received
				
				String input=new String(request.getData());//------->ID of the INSE student/Advisor
				input=input.trim();
				
				String data = "";
				
				
				if(input.contains("INSEA")){//----------------------->ID of an advisor
					if(input.equals("INSEA")){
						data = "Welcome Program Advisor \n";
					}else if(input.contains("add")){//--------------->Advisor adding a new course
						data=new_course(input);
					}
					
				}
				
				
				
				else if(input.contains("course_availability")){//---->Returns the List of courses available
					data = course_availability();				
				
				}
				
				
				else if(input.contains("COMPS")||input.contains("SOENS")||input.contains("INSES")){
					if(input.equals("INSES")){
						data=extract_student_information(input);
					}else if(input.contains("add COMP") || input.contains("drop COMP")){//-----------> FOR COMP STUDENTS ADDING/DROPPING COURSES
						if(input.contains("add")){//-------------->adding INSE courses for COMP students
							data = decrease_availability(input);
						}else if(input.contains("drop")){//------->dropping INSE course for COMP students
							data= increase_availability(input);
						}
					}else if(input.contains("add SOEN") ||input.contains("drop SOEN")){//------------> FOR SOEN STUDENTS ADDING/DROPPING COURSES
							if(input.contains("add")){//-------------->adding INSE courses for SOEN students
								data = decrease_availability(input);
						}else if(input.contains("drop")){//-------->dropping INSE courses for SOEN students 
							data= increase_availability(input);
						}
					}else if(input.contains("add INSE" )){//-------------------->(HOME SERVER) FOR INSE STUDENTS ADDING COURSES
						if(input.contains("INSE 6")){//------------------->add INSE courses
							data=add_course(input);	 
						}else if(input.contains("COMP 6")){//-------------------> add COMP courses
							data=request_comp(input);
							data=add_COMP_course(input);
						}else if(input.contains("SOEN 6")){//-------------------> add SOEN courses
							data=request_soen(input);
							data=add_SOEN_course(input);
						}
				
					}else if(input.contains("drop INSE")){//------> (HOME SERVER) FOR INSE STUDENTS DROPPING COURSES
						if(input.contains("INSE 6")){	  //------> dropping INSE courses
							data=drop_course(input);
						}else if(input.contains("COMP 6")){//------>dropping COMP courses
							data=request_comp(input);
							data=drop_COMP_course(input);
						}else if(input.contains("SOEN 6")){//------->dropping SOEN courses
							data=request_soen(input);
							data=drop_SOEN_course(input);
						}
					}
				}
				else{

					data = input +" Incorrect Input--INSE SERVER"+input.compareTo("INSES");
				}
				
				
				sendData=data.getBytes();
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
	}	

}
