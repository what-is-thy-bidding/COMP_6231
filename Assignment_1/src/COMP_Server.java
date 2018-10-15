package Assignment_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Iterator;

public class COMP_Server {
	//List of courses in COMPUTER SCIENCE =  COMP 6231, COMP 6651
	//Each course has a seat capacity of 2
	//Both courses are available in FALL, WINTER and SUMMER
	
	
	
	static LinkedList<data_structure> comp_student_list = new LinkedList<data_structure>();//-----------------------> Contains a list of all the COMP SCI students and their data
	static LinkedList<courses_availability> courses = new LinkedList<courses_availability>();//--------------------->List of courses available, their terms and the number of students
	
//-------------------------------------------------HOME SERVER METHODS-------------------------------------	
	public static  void add_advisor(){	//--------------------------------> Default addition of an ADVISOR to the database
		data_structure advisor=new data_structure("COMPA");
		comp_student_list.add(advisor);
	}
	public static void add_courses(){ //--------------------------> Addition of all courses in the server
		courses_availability fall_6231 = new courses_availability("COMP 6231", "FALL");
		courses_availability winter_6231 = new courses_availability("COMP 6231", "WINTER");
		courses_availability summer_6231 = new courses_availability("COMP 6231", "SUMMER");
		courses.add(fall_6231);
		courses.add(winter_6231);
		courses.add(summer_6231);
		
		courses_availability fall_6651 = new courses_availability("COMP 6651", "FALL");
		courses_availability winter_6651 = new courses_availability("COMP 6651", "WINTER");
		courses_availability summer_6651 = new courses_availability("COMP 6651", "SUMMER");
		courses.add(fall_6651);
		courses.add(winter_6651);
		courses.add(summer_6651);

	}
	public static void add_student(){//---------------------------->Default addition of a STUDENT to the database
		data_structure node=new data_structure("COMPS");
		comp_student_list.add(node);
		
	}
	
	private static String new_course(String data) { //------------->ADVISOR ADDING A NEW COURSE
		try{
			String [] array = data.split(" ");
		
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
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
	
	private static String delete_course(String input) {//----------->ADVISOR DELETING A COURSE
		String array[] = input.split(" ");
		//array[0] -> "add"
		//array[1] -> "COMPS/COMPA" (USERID)
		//array[2] -> "COMP"  (COURSE SUBJECT NAME)
		//array[3] -> "6231"  (COURSE CODE)
		//array[4] -> "FALL"  (TERM)
		String CourseId= array[2]+" "+array[3];
		String Term=array[4];
		/*Iterator<courses_availability> it = courses.iterator();
		while(it.hasNext()){
			if(it.next().get_course_name().equals(CourseId) && it.next().get_term().equals(Term)){
				it.remove();
				return "COURSE DELETED";
			}
			
		}
		//return "Course doesn't exist";
		return input;*/
		
		/*Iterator<courses_availability> it = courses.iterator();
		String text="";
		while(it.hasNext()){
			text+= it.next().get_course_name()+"  "+it.next().get_term() + "\n";
		}*/
		
		Iterator<courses_availability> it = courses.iterator();
		//String text = "";
		while(it.hasNext()){
			courses_availability node = new courses_availability(CourseId, Term);
			courses_availability list = it.next();
			if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
				drop_course(input);
				it.remove();
				return "The course has been deleted";
			}
		}
		return it.next().get_course_name() + " this is in the list";
		
		
		//return CourseId + Term;
	}
	private static String add_course(String data) {//------------------------->add courses to the student
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
		
			Iterator<data_structure> itr = comp_student_list.iterator();
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){
				
					if(CourseId.contains("COMP")){
					
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
			//array[1] -> "COMPS" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
		
			String UserId= array[1];
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
		
		
		
			Iterator<data_structure> itr = comp_student_list.iterator();
		
		
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
		
			return "COURSE CAN'T BE DELETED- COMP SERVER";
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
	}

	//--------------------------------------------------CALLING THE INSE SERVER AND ADDITION/DROP OF INSE COURSES----------------------------------------------------------------------------------------	
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
	private static String add_INSE_course(String data){
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
			Iterator<data_structure> itr = comp_student_list.iterator();
		
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
	private static String drop_INSE_course(String data){
		
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
				
				
				
				Iterator<data_structure> itr = comp_student_list.iterator();
				
				
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
	

//---------------------------------------------------CALLING THE SOEN SERVER and ADDITION/DROP OF SOEN COURSES----------------------------------------------------------------------------------------	
	
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
			Iterator<data_structure> itr = comp_student_list.iterator();
		
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
			
				if(list.get_Id().equals(node.get_Id())){					
						if(Term.equals("FALL")){
							return list.add_courses_fall(CourseId);
						
						}else if(Term.equals("WINTER")){
							return list.add_courses_fall(CourseId);
						
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
	private static String drop_SOEN_course(String data){
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
				
				
				
				Iterator<data_structure> itr = comp_student_list.iterator();
				
				
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
	

//----------------------------------------------------REQUESTS FROM OTHER SERVER---------------------------------------------------------------------------------------	
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
	
	
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
		Iterator<data_structure> it= comp_student_list.iterator();
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
			aSocket = new DatagramSocket(6789);
			System.out.println("Comp Server Started....");
			while(true){
				byte[] buffer = new byte[1000];
				byte[] sendData=new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);// request received
				
				String input=new String(request.getData());//------->ID of the COMP student/Advisor
				input=input.trim();
				
				String data = "";
				if(input.contains("COMPA")){//----------------------->ID of an advisor
					if(input.equals("COMPA")){
						data = "Welcome Program Advisor \n";
					}else if(input.contains("add")){//--------------->Advisor adding a new course
						data=new_course(input);
					}else if(input.contains("drop")){
						data = delete_course(input);
					}
					//DELETE COURSES
				}
				
				else if(input.contains("course_availability")){//---->Returns the List of courses available
					
					data = course_availability();
					
				}
				
				
				else if(input.contains("COMPS")||input.contains("SOENS")||input.contains("INSES")){
					
					if(input.equals("COMPS")){//-------------------------->Student Information Extraction
						data=extract_student_information(input);					
					}else if(input.contains("add SOENS ") ||input.contains("drop SOENS")){//------------> FOR SOEN STUDENTS ADDING/DROPPING COURSES
						if(input.contains("add")){//-------------->adding COMP courses for SOEN students
							data = decrease_availability(input);
						}else if(input.contains("drop")){//-------->dropping COMP courses for SOEN students 
							data= increase_availability(input);
						}
					}else if(input.contains("add INSES") ||input.contains("drop INSES")){//------------> FOR INSE STUDENTS ADDING/DROPPING COURSES
						if(input.contains("add")){//-------------->adding COMP courses for SOEN students
							data = decrease_availability(input);
						}else if(input.contains("drop")){//-------->dropping INSE courses for SOEN students 
							data= increase_availability(input);
						}
				
					}else if(input.contains("add COMPS")){//----------------> FOR COMP STUDENTS ADDING COURSES(HOME SERVER)
						if(input.contains("COMP 6")){
							data=add_course(input);
						}
						else if(input.contains("INSE 6")){//-------->add INSE courses
							data=request_inse(input);
							data=add_INSE_course(input);
						}else if(input.contains("SOEN 6")){//--------> SOEN courses
							data=request_soen(input);
							data=add_SOEN_course(input);
						}
					}else if(input.contains("drop COMP")){//---------> FOR COMP STUDENTS DROPPING COURSES(HOME SERVER)
						if(input.contains("COMP 6")){
							data=drop_course(input);
						}else if(input.contains("INSE 6")){
							data=request_inse(input);//--------------->reducing the availability of the course(INSE)
							data=drop_INSE_course(input);//----------->deleting the course from the COMP student's record
						}else if(input.contains("SOEN 6")){
							data=request_soen(input);//---------------> reducing the availability of the course(SOEN)
							data=drop_SOEN_course(input);//-----------> deleting the course from the COMP student's record
						}
					
					}
					else{
						data = input +" Incorrect Input--COMP SERVER";
					}
				
				}//----------> closing of the student functions
			
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
