package front_end;

import systemApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;


import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class systemObj extends systemPOA { 
	private ORB orb;
	public void setORB(ORB orb_val) {
		orb = orb_val;  
	}

	public String course_availability(String command) {
		/*String result="";
		int i=0;
		if(command.contains("COMP")) {
			result = "COURSE NAME            SEATS AVAIABLE          TERM\n";
			while(i<COMP_server.courses.size()) {
				result +=COMP_server.courses.get(i).get_course_name()+"               " +(COMP_server.courses.get(i).get_course_capacity()-COMP_server.courses.get(i).get_course_count()) +"/"+COMP_server.courses.get(i).get_course_capacity()+"      		   "+COMP_server.courses.get(i).get_term()+" \n";  
				i++;
			}
			result+="\n";
			result+=COMP_server.request_inse("course_availability");
			result+="\n";
			result+=COMP_server.request_soen("course_availability");
			return result;
		}
		
		if(command.contains("INSE")) {
			int j=0;
			while(j<INSE_server.courses.size()) {
				result += INSE_server.courses.get(j).get_course_name()+"               " +(INSE_server.courses.get(j).get_course_capacity()-INSE_server.courses.get(j).get_course_count()) +"/"+INSE_server.courses.get(j).get_course_capacity() +"     		   "+INSE_server.courses.get(j).get_term() + "\n";
				j++;
			}
			result+="\n";
			result+=INSE_server.request_comp("course_availability");
			result+="\n";
			result+=INSE_server.request_soen("course_availability"); 
			return result; 

		} 
		
		if(command.contains("SOEN")) {
			int k=0;
			while(k<SOEN_server.courses.size()) {
				result += SOEN_server.courses.get(k).get_course_name()+"               " +(SOEN_server.courses.get(k).get_course_capacity()-SOEN_server.courses.get(k).get_course_count()) +"/"+ SOEN_server.courses.get(k).get_course_capacity()+"     		    "+SOEN_server.courses.get(k).get_term() +"\n";
				k++;
			}
			result+="\n";
			result+=SOEN_server.request_inse("course_availability");
			result+="\n";
			result+=SOEN_server.request_comp("course_availability");
			return result;*/
		
		FRONT_END.send_sequencer_request(command);
		return FRONT_END.final_result();
		
	} 
	
	public String add_course(String command){
	/*	String []array=command.split(" ");
		String department = array[2];
		if(command.contains("add COMPS")){//----------------> FOR COMP STUDENTS ADDING COURSES(HOME SERVER) 
				if(department.contains("COMP")){//---->COMP COURSE
					String command_copy=command;
					command=COMP_server.add_course(command);
				}else if(department.contains("SOEN")) {//---->SOEN COURSE
					String command_copy=command;
					command=COMP_server.request_soen(command);
					if(command.contains("true")) {
						command=COMP_server.add_SOEN_course(command_copy);
						if(!command.contains("Course Added")) { 
							command_copy=command_copy.replace("add", "drop");
							COMP_server.request_soen(command_copy);
						}
						
					}
				}else if(department.contains("INSE")) {
					String command_copy=command;
					command=COMP_server.request_inse(command);
					if(command.contains("true")) {
						command=COMP_server.add_INSE_course(command_copy);
						if(!command.contains("Course Added")) {
							command_copy=command_copy.replace("add", "drop");
							COMP_server.request_inse(command_copy); 
						}

					}
				}
		 }
		 
		 if(command.contains("add SOENS")) {//SOFTWARE STUDENT 
			
			 if(department.contains("SOEN")) {//----> SOFTWARE COURSE
				 String command_copy=command;
				 command= SOEN_server.add_course(command);
			 }else if(department.contains("COMP")) {//-----> COMP COURSE
				 
				 String command_copy=command;
					command=SOEN_server.request_comp(command);
					if(command.contains("true")) {
						command=SOEN_server.add_COMP_course(command_copy);
						if(!command.contains("Course Added")) {
							command_copy=command_copy.replace("add", "drop");
							SOEN_server.request_comp(command_copy);
						}

					}
			 }else if(department.contains("INSE")) {//-----> INSE COURSE
				
				 String command_copy=command; 
					command=SOEN_server.request_inse(command); 
					if(command.contains("true")) {
						command=SOEN_server.add_INSE_course(command_copy);
						if(!command.contains("Course Added")) {
							command_copy=command_copy.replace("add", "drop");
							SOEN_server.request_inse(command_copy); 
						}

					}
			 }
		 }
		 
		 if(command.contains("add INSES")) {//INSE STUDENT
			 
			 if(department.contains("INSE")) {//---->INSE COURSE
				 String command_copy=command;
				 command=INSE_server.add_course(command);
			 }else if(department.contains("COMP")) {//---->COMP COURSE
				 String command_copy=command;
					command=INSE_server.request_comp(command);
					if(command.contains("true")) {
						command=INSE_server.add_COMP_course(command_copy);
						if(!command.contains("Course Added")) {
							command_copy=command_copy.replace("add", "drop");
							INSE_server.request_comp(command_copy);
						}

					}
			 }else if(department.contains("SOEN")) {//---->SOEN COURSE
				 String command_copy=command;
					command=INSE_server.request_soen(command);
					if(command.contains("true")) {
						command=INSE_server.add_SOEN_course(command_copy);
						if(!command.contains("Course Added")) {
							command_copy=command_copy.replace("add", "drop");
							INSE_server.request_soen(command_copy);
						}
						
					}
			 }
		 }
		return command;*/ 
		//String result="";
		//FRONT_END.send_sequencer_request(command);
		FRONT_END.send_sequencer_request(command);	
		return FRONT_END.final_result(); 
	}

	public String drop_course(String command) {
		/*String []array=command.split(" ");
		String department = array[2]; 
		if(command.contains("drop COMPS")) { 
			if(department.contains("COMP")) {
				command=COMP_server.drop_course(command);
			}else if(department.contains("INSE")) {
				String command_copy=command;
				command=COMP_server.drop_INSE_course(command);
				if(command.contains("COURSE DELETED")) {
					COMP_server.request_inse(command_copy);
				}
 
			}else if(department.contains("SOEN")) {
				String command_copy= command;
				command=COMP_server.drop_SOEN_course(command);
				if(command.contains("COURSE DELETED")) {
					COMP_server.request_soen(command_copy);
					
				}
				
			}
			
		}else if(command.contains("drop INSES")) {
			if(department.contains("INSE")) {
				command= INSE_server.drop_course(command);
			}else if(department.contains("COMP")) {
				String command_copy= command;
				command=INSE_server.drop_COMP_course(command);
				if(command.contains("COURSE DELETED")) {
					INSE_server.request_comp(command_copy);
				}
			}else if(department.contains("SOEN")) {
				String command_copy=command;
				command=INSE_server.drop_SOEN_course(command);
				if(command.contains("COURSE DELETED")) {
					INSE_server.request_soen(command_copy);
				}
			}
			
		}else if(command.contains("drop SOENS")) {
			if(department.contains("SOEN")) {
				command=SOEN_server.drop_course(command);
			}else if(department.contains("COMP")){
				String command_copy = command;
				command=SOEN_server.drop_COMP_course(command);
				if(command.contains("COURSE DELETED")) {
					SOEN_server.request_comp(command_copy);
				}
			}else if(department.contains("INSE")) {
				String command_copy=command;
				command=SOEN_server.drop_INSE_course(command);
				if(command.contains("COURSE DELETED")) {
					SOEN_server.request_inse(command_copy);
				}
			}
		}
		return command;*/
		FRONT_END.send_sequencer_request(command);
		return FRONT_END.final_result();
	}

	public String new_course(String command) {
		/*try{
			String [] array = command.split(" ");
		
			//array[0] -> "add"
			//array[1] -> "COMPS/COMPA" (USERID)
			//array[2] -> "COMP"  (COURSE SUBJECT NAME)
			//array[3] -> "6231"  (COURSE CODE)
			//array[4] -> "FALL"  (TERM)
			//array[5] -> course capacity
			
			int capacity = Integer.parseInt(array[5]);
			String CourseId= array[2]+" "+array[3];
			String Term=array[4];
			
			if(array[1].equals("COMPA")) {
				if(!CourseId.contains("COMP")) {
					return "Invalid Course";
				}
				Iterator <courses_availability> it = COMP_server.courses.iterator();
				while(it.hasNext()){
					courses_availability node = it.next();
					if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){ 
						return "Course already exists in that term";
					}
				}
				COMP_server.courses.add(new courses_availability(CourseId, Term,capacity));
				return "Course has been added"; 
			}
			if(array[1].equals("SOENA")) {
				if(!CourseId.contains("SOEN")) {
					return "Invalid Course";
				}
				Iterator <courses_availability> it = SOEN_server.courses.iterator();
				while(it.hasNext()){
					courses_availability node = it.next();
					if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
						return "Course already exists in that term";
					}
				}
			
				SOEN_server.courses.add(new courses_availability(CourseId, Term,capacity));
				return "Course has been added";
			}
			if(array[1].equals("INSEA")) {
				if(!CourseId.contains("INSE")) {
					return "Invalid Course";
				}
				Iterator <courses_availability> it = INSE_server.courses.iterator();
				while(it.hasNext()){
					courses_availability node = it.next();
					if(node.get_course_name().equals(CourseId) && node.get_term().equals(Term)){
						return "Course already exists in that term";
					}
				}
			
				INSE_server.courses.add(new courses_availability(CourseId, Term,capacity));
				return "Course has been added";
			}

			
		}catch(IndexOutOfBoundsException er){
			return "Input is  Incorrect or Insufficient";
		}
		return command;*/
			
			
		FRONT_END.send_sequencer_request(command);
		return FRONT_END.final_result();
	}

	public String delete_course(String command) {
		/*
		String []array=command.split(" ");
		String department = array[2];
		if(command.contains("COMPA")) {
			if(department.contains("COMP")) {
				command=COMP_server.delete_course(command);
			}else {
				return "cannot delete that course";
			}
		}else if(command.contains("SOENA")) {
			if(department.contains("SOEN")) {
				command=SOEN_server.delete_course(command);
			}else {
				return "cannot delete that course"; 
			}
			
			
		}else if(command.contains("INSEA")) {
			if(department.contains("INSE")) {
				command=INSE_server.delete_course(command);
			}else {
				return "cannot delete that course";
			}
		}
			
		return command;*/
		
		
		FRONT_END.send_sequencer_request(command);
		return FRONT_END.final_result();
	}


	public void shutdown() {
		orb.shutdown(false);
	
	}

	public String list_course(String command) {
		/*if(command.contains("COMPS")) {
			int i=0;
			while(i<COMP_server.comp_student_list.size()){
				if(COMP_server.comp_student_list.get(i).get_Id().equals(command)) {
					return COMP_server.comp_student_list.get(i).print();
				}
				i++;
			}
			
		}if(command.contains("SOENS")) {
			int i=0;
			while(i<SOEN_server.soen_student_list.size()){
				if(SOEN_server.soen_student_list.get(i).get_Id().equals(command)) {
					return SOEN_server.soen_student_list.get(i).print();
				}
				i++;
			}
		}if(command.contains("INSES")) {
			int i=0;
			while(i<INSE_server.inse_student_list.size()){
				if(INSE_server.inse_student_list.get(i).get_Id().equals(command)) {
					return INSE_server.inse_student_list.get(i).print();
				}
				i++;
			}
		}
		return null;*/
		
		
		FRONT_END.send_sequencer_request(command);
		return FRONT_END.final_result();
	}

	
	public String final_result() {
		return FRONT_END.final_result();
	}
	
	public String swap_course(String enrolled, String swap) {
		//array[0] -> "add"
		//array[1] -> "COMPS/COMPA" (USERID)
		//array[2] -> "COMP"  (COURSE SUBJECT NAME)
		//array[3] -> "6231"  (COURSE CODE)
		//array[4] -> "FALL"  (TERM)
		
		/*String [] array1 = enrolled.split(" ");

		String Enrolled_Term=array1[4];
		
		String [] array2 = enrolled.split(" ");

		String Swap_Term=array2[4];
		
		if(!Swap_Term.equals(Enrolled_Term)) {
			return "THE TERMS OF THE 2 COURSES HAVE TO BE THE SAME";
		}
		
		
		String result="THE COURSES HAVE BEEN SWAPPED";
		String result1=drop_course(enrolled);
		if(result1.contains("COURSE DELETED")) {
			result1=add_course(swap);
			if(!result1.contains("Course Added") && !result1.contains("The course has already been added to the semester")) {// THE REQUESTED FOR IS NOT AVAIABLE 
				enrolled=enrolled.replace("drop", "add");
				add_course(enrolled);
				result="THE REQUESTED COURSE IS NOT AVAILABLE";
			}
		}else if(!result1.contains("COURSE DELETED")) {// NOT ENROLLED IN THAT COURSE
			result = "STUDENT IS NOT ENROLLED IN THAT COURSE";
		}
	
		return result;*/
		FRONT_END.send_sequencer_request(enrolled);
		FRONT_END.send_sequencer_request(swap);
		return FRONT_END.final_result();
	}

	

}
