import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import data_structures.*;
import systemApp.system;
import systemApp.systemHelper;

public class SOEN_server { 
	static LinkedList<data_structure> soen_student_list = new LinkedList<data_structure>();//-----------------------> Contains a list of all the COMP SCI students and their data
	static LinkedList<courses_availability> courses = new LinkedList<courses_availability>();//--------------------->List of courses available, their terms and the number of students 
	
//-------------------------------------------------HOME SERVER METHODS-------------------------------------	
	public static  void add_advisor(){	//--------------------------------> Default addition of an ADVISOR to the database
		data_structure advisor=new data_structure("SOENA");
		soen_student_list.add(advisor);
	}
	public static void add_courses(){ //--------------------------> Addition of all courses in the server
		courses_availability fall_6441 = new courses_availability("SOEN 6441", "FALL");
		courses_availability winter_6441 = new courses_availability("SOEN 6441", "WINTER");
		courses_availability summer_6441 = new courses_availability("SOEN 6441", "SUMMER");
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
				request_comp("drop COMPS " + CourseId + " " + Term);
				request_inse("drop INSES " + CourseId + " " + Term);
				it.remove();
				
				return "The course has been deleted"; 
			}
		}
		return it.next().get_course_name() + " this is in the list"; 
		
		
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
				courses_availability node = new courses_availability(CourseId, Term);
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
			
				
			
			Iterator<data_structure> itr = soen_student_list.iterator();
			while(itr.hasNext()){
				data_structure node = new data_structure(UserId);
				data_structure list = itr.next();
				
				if(list.get_Id().equals(node.get_Id())){
				
					if(CourseId.contains("SOEN")){
					
						if(Term.equals("FALL")){
							if(list.add_courses_fall(CourseId).equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(list.add_courses_fall(CourseId).equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
							return list.add_courses_fall(CourseId);
						}else if(Term.equals("WINTER")){
							if(list.add_courses_winter(CourseId).equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(list.add_courses_winter(CourseId).equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
							return list.add_courses_winter(CourseId);
						}else if(Term.equals("SUMMER")){
							if(list.add_courses_summer(CourseId).equals("The course has already been added to the semester")){
								increase_availability(data);
							}else if(list.add_courses_summer(CourseId).equals("Cannot add more courses to the semester")){
								increase_availability(data);
							}
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
				courses_availability node = new courses_availability(CourseId, Term);
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
			return null;
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
			
			
			return null;
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
						result +=courses.get(i).get_course_name()+" 		" +(2-courses.get(i).get_course_count()) +"/2         "+courses.get(i).get_term() +"\n";  
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
			
			
			return null;
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
			Runnable task1 = () -> { 
                recieve();
            };
            Thread thread = new Thread(task1);
            thread.start(); 

			// create and initialize the ORB //// get reference to rootpoa &amp; activate
			// the POAManager
			ORB orb = ORB.init(args, null);
			//-ORBInitialPort 1050 -ORBInitialHost localhost
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			systemObj sysobj = new systemObj();
			sysobj.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(sysobj);
			system href = systemHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("soen");
			ncRef.rebind(path, href);

			System.out.println("SOFTWARE Server ready and waiting ...");

			// wait for invocations from clients 
			for (;;) {
				orb.run();
			}
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("HelloServer Exiting ..."); 

	}
}