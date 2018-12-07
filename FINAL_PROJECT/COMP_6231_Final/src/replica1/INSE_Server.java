package replica1;
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
		
		/*courses_availability fall_6110 = new courses_availability("INSE 6110", "FALL",2);
		courses_availability winter_6110 = new courses_availability("INSE 6110", "WINTER",2);
		courses_availability summer_6110 = new courses_availability("INSE 6110", "SUMMER",3);
		courses.add(fall_6110);
		courses.add(winter_6110);
		courses.add(summer_6110);
		
		courses_availability fall_6150 = new courses_availability("INSE 6150", "FALL",1);
		courses_availability winter_6150 = new courses_availability("INSE 6150", "WINTER",2);
		courses_availability summer_6150 = new courses_availability("INSE 6150", "SUMMER",1);
		courses.add(fall_6150);
		courses.add(winter_6150);
		courses.add(summer_6150);*/

	}
	public static void add_student(){//---------------------------->Default addition of a STUDENT to the database
		data_structure node=new data_structure("INSES1010");
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
			//array[5] -> "2"     (CAPACITY)
			int capacity = Integer.parseInt(array[5]);
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			if(!CourseId.contains("INSE")) {
				return array[1]+": ("+array[2]+" "+array[3]+" "+array[4] + ") " +": Invalid Course";
			}
			Iterator <courses_availability> it = courses.iterator();
			while(it.hasNext()){
				courses_availability node = it.next();
				if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
					return array[1]+": ("+array[2]+" "+array[3]+" "+array[4] + ") " +": Course already exists in that term";
				}
			}
		
			courses.add(new courses_availability(CourseId, Term,capacity));
			return array[1]+": ("+array[2]+" "+array[3]+" "+array[4] + ") " +": Course has been added";
		
			
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
			int check=0;
			while(it.hasNext()){
				courses_availability node = new courses_availability(CourseId, Term,0);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					check=1;
					if(list.get_course_count()<list.get_course_capacity()){
						list.increase_count();
					}else{
						return "THE COURSE IS FULL IN THAT SEASON";
					}
				}
			}
			if(check==0){
				return "Course doesn't exist";
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
	private static String delete_course(String input) {//----------->ADVISOR DELETING A COURSE
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
				int i=0;
				while(i<inse_student_list.size()){
					drop_course("drop "+ inse_student_list.get(i).get_Id()+ " "+CourseId+" "+Term);
					i++;
				}
				request_comp(input);
				request_soen(input);
				
				it.remove();
				
				return " The course has been deleted"; 
			}
		}
		return " COURSE DOESN'T EXIST"; 
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
				courses_availability node = new courses_availability(CourseId, Term,0);
				courses_availability list = it.next();
				if(node.get_course_name().equals(list.get_course_name()) && node.get_term().equals(list.get_term())){
					if(list.get_course_count()<list.get_course_capacity()){
						list.increase_count();
						return "true";
					}else {
						return "false";//the course is full
					}
				}
			}
			
			return "false";// the course doesn't exist
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
		
				Iterator<courses_availability> it = courses.iterator();
				while(it.hasNext()){
					courses_availability node = it.next();
					if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
						node.decrease_count();
						return "true";
					}
					
				}
				return "false"; // the course asked for doesn't exist

		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
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
				System.out.println(input);
				//----------------------ADVISORS------------------------------------------------
				String data = "";
				if(input.contains("COMPA")||input.contains("SOENA")||input.contains("INSEA")) {
					String array[]= input.split(" ");
					int length= array.length;
				
					if(length==1) {//just the user Id
						String command= array[0];   // "COMPA"/"SOENA"/"INSEA"
						data="WELCOME "+command;

					}else if(length==5) {//delete function
						String command=array[0];    // "drop"
						String userId=array[1];     // "INSEA"
						String department=array[2]; // "INSE"
						String code=array[3];       // "6110"
						String term=array[4];       // "FALL"
						String CourseId= department + " "+ code;
						if(userId.contains("INSEA") && command.equals("delete")){
							if(!department.equals("INSE")){
								data=array[1]+"("+CourseId+" "+array[4]+ "): cannot delete that course- INCORRECT DEPARTMENT ";
							}else{
								data=delete_course(input);
							}
						}else if((userId.contains("SOENA") || userId.contains("COMPA")) && command.equals("delete")){
							int i=0;
							if(department.equals("SOEN")){
								while(i<inse_student_list.size()){
									drop_SOEN_course("drop "+inse_student_list.get(i).get_Id()+" "+CourseId+" "+term);
									i++;
								}
							}else if(department.equals("COMP")){
								while(i<inse_student_list.size()){
									drop_COMP_course("drop "+inse_student_list.get(i).get_Id()+" "+CourseId+" "+term);
									i++;
								}
							}
						}else {
							data="INCORRECT INPUT";
						}
					}else if(length==6) {//new course function
						String command=array[0];    // "add"
						String userId=array[1];     // "INSEA"
						String department=array[2]; // "INSE"
						String code=array[3];       // "6115"
						String term=array[4];       // "FALL"
						String capacity=array[5];   // "2"
						data=new_course(input);	

					}
				}
				
				//--------------------STUDENTS------------------------------------------
				if(input.contains("COMPS")||input.contains("SOENS")||input.contains("INSES")) {
					String array[]= input.split(" ");
					int length= array.length;
				
					if(length==2) {//just the list course_availability && the ID check of the student
						String command= array[0];
						String function= array[1];
						//data=(input + " "+length);  // "COMPS"/"SOENS"/"INSES"/"course_avaiability"
						//System.out.println(command);
						
							
						if(command.contains("INSES")) {
							data=extract_student_information(command);
						}
						
						
					}else if(length==5) {//adding a course, dropping a course
						String command=array[0];    // "add"/"drop"
						String userId=array[1];     // "COMPS"/"INSES"/"SOENS"
						String department=array[2]; // "COMP" /"INSE" /"SOEN"
						String code=array[3];       // "6231" 
						String term=array[4];       // "FALL"/"WINTER/"SUMMER"
						
						if(userId.contains("INSES")) {
							if(department.contains("INSE") && command.equals("enroll")) {
								int i=0;
								while(i<inse_student_list.size()) {
									inse_student_list.get(i).print();
									i++;
								}
								data=add_course(input);
								i=0;
							}else if(department.contains("INSE")&& command.equals("drop")) {
								data=drop_course(input);
							}else if(department.contains("COMP")&& command.equals("enroll")) {
								data=request_comp(input);
								data=data.trim();
								if(data.equals("true")) {
									data=add_COMP_course(input);
									data=data.trim();
									if(!data.equals("Course Added to the Fall semester")&&!data.equals("Course Added to the Winter semester")&&!data.equals("Course Added to the Summer semester")) {
										input=input.replace("enroll", "drop");
										request_comp(input);
									}
								}
							}else if(department.contains("COMP")&& command.equals("drop")) {
								data=drop_COMP_course(input);
								if(data.equals("COURSE DELETED")) {
									request_comp(input);
								}
							}else if(department.contains("SOEN")&& command.equals("enroll")) {
								data=request_soen(input);
								data=data.trim();
								if(data.equals("true")) {
									data=add_SOEN_course(input);
									data=data.trim();
									if(!data.equals("Course Added to the Fall semester")&& !data.equals("Course Added to the Winter semester")&& !data.equals("Course Added to the Summer semester")) {
										input=input.replace("enroll", "drop");
										request_soen(input);
									}
								}
							}else if(department.contains("SOEN")&& command.equals("drop")) {
								
								data=drop_SOEN_course(input);
								if(data.equals("COURSE DELETED")) {
									request_soen(input);
								}
									
							}
						}else if(userId.contains("COMPS")) {
							if(command.equals("enroll")) {
								data=decrease_availability(input);
							}else if(command.equals("drop")){
								data=increase_availability(input);
							}
							
						}else if(userId.contains("SOENS"))
							if(command.equals("enroll")) {
								data=decrease_availability(input);
							}else if(command.equals("drop")){
								data=increase_availability(input);
							}
						
					}
				}
				
				if(input.contains("availability")) {
					data=course_availability();				
				}
					
				if(input.contains("schedule")) {
					String array[]= input.split(" ");
					String command= array[0]; // "COMPS"/"SOENS"/"INSES"/"course_avaiability"
					String function= array[1];// "list_courses"
						
					if(command.contains("INSES")) {
						data=extract_student_information(command);
					}
				}
				
				
				System.out.println(data);

				
				
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
