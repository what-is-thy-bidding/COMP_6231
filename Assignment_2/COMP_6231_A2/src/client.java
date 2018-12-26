import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import systemApp.*;

public class client { 
	public static String new_course(String userId, String courseId){
		return "add "+userId+" "+courseId;
		
	}

	public static String delete_course(String userId, String courseId){
		return "delete " + userId+" "+courseId;
		
	}
	
	public static String add_course(String student_Id, String course_Id ){
		return "add "+student_Id+" "+course_Id;
		
	}
	
	public static String drop_course(String student_Id, String course_Id ){
		return "drop "+student_Id+" "+course_Id;
	}

	 
	
	public static void student_menu(String userId, system sysobj){
		Scanner sc=new Scanner(System.in);
		if(userId.contains("COMPS")){
			System.out.print(sysobj.list_course(userId));
		}else if(userId.contains("SOENS")){
			System.out.print(sysobj.list_course(userId)); 
		}else if(userId.contains("INSES")){
			System.out.print(sysobj.list_course(userId));
		}
		System.out.println("ENTER CHOICE : ");
		System.out.println("1. ADD COURSE.");
		System.out.println("2. DROP COURSE.");
		System.out.println("3. Check Availability");
		System.out.println("4. Swap Courses");
		System.out.println("0. LOGOUT.");
		
	    int ch = sc.nextInt(); 
	    while(ch!=0){
	    	switch(ch){
	    		case 1: 
	    				System.out.println("------------------------------ \n");
	    				System.out.print("WHICH COURSE_ID TO ADD AND WHICH TERM(eg. COMP 6231 FALL): ");
	    				sc=new Scanner(System.in);
	    				String course_Id = sc.nextLine();
	    				course_Id=add_course(userId, course_Id );
	    				System.out.println(sysobj.add_course(course_Id));
	    			    ch=5; 
	    			    break; 
	    		
	    		case 2: 
    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE_ID TO DROP AND WHICH TERM(eg. COMP 6231 FALL): ");
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				System.out.println(sysobj.drop_course(drop_course(userId, course_drop)));
	    				ch=5;
	    				break; 
	    		
	    		case 3: System.out.println(sysobj.course_availability(userId+" course_availability"));
	    				ch=5;
	    				break;
	    		
	    		case 4:  
	    				System.out.println("--------------------------------\n");
	    				System.out.print("WHICH ENROLLED COURSE WOULD YOU LIKE TO SWAP(eg. COMP 6231 FALL): ");
	    				sc=new Scanner(System.in);
	    				String enrolled= sc.nextLine();
	    				System.out.println("WHICH COURSE WOULD YOU LIKE TO TAKE IN PLACE(eg. COMP 6231 FALL):");
	    				String swap=sc.nextLine();
	    				enrolled=drop_course(userId,enrolled);
	    				swap=add_course(userId,swap);
	    				System.out.println(sysobj.swap_course(enrolled,swap));
	    				//System.out.println(enrolled +" swapped for "+swap);
	    		default:
	    			
	    				if(userId.contains("COMPS")){
	    					System.out.print(sysobj.list_course(userId));
	    				}else if(userId.contains("SOENS")){
	    					System.out.print(sysobj.list_course(userId));
	    				}else if(userId.contains("INSES")){ 
	    					System.out.print(sysobj.list_course(userId)); 
	    				}
	    					
	    				
	    				System.out.println("ENTER CHOICE : ");
	    				System.out.println("1. ADD COURSE.");
	    				System.out.println("2. DROP COURSE.");
	    				System.out.println("3. Check Availability");
	    				System.out.println("4. Swap Courses");
	    				System.out.println("0. LOGOUT.");
						sc=new Scanner(System.in);

	    				ch = sc.nextInt();	
	    				break;
	    		
	    	}
	    	
	    }
	    System.out.println(" LOGGING OUT ");
	    
	    sc.close();

	  

		
	}
	
	
	public static void advisor_menu(String userId, system sysobj ){
		Scanner sc=new Scanner(System.in);
		System.out.println("ENTER CHOICE : ");
		System.out.println("1. ADD A NEW COURSE.");
		System.out.println("2. DELETE A COURSE.");
		System.out.println("3. CHECK AVAILABILITY");
		System.out.println("4. CHECK STUDENT PROFILE");
		System.out.println("0. LOGOUT.");

	    int ch = sc.nextInt(); 
	    while(ch!=0){
	    	switch(ch){
	    		case 1:	    			
	    				System.out.println("------------------------------ \n");
	    				System.out.print("WHICH COURSE WOULD YOU LIKE TO ADD, WHICH TERM AND WHAT CAPACITY (eg. COMP 6231 FALL 2): ");
	    				sc=new Scanner(System.in);
	    				String course_Id = sc.nextLine();
	    				course_Id=new_course(userId, course_Id );
	    				System.out.println(sysobj.new_course(course_Id)); 
	    			    ch=5;
	    			    break;
	    		
	    		case 2: 

    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE WOULD YOU LIKE TO DELETE AND WHICH TERM(eg. COMP 6231 FALL): "); 
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				course_drop = delete_course(userId, course_drop);
	    				System.out.println(sysobj.delete_course(course_drop));
	    				ch=5;
	    				break; 
	    				
	    		case 3: 
	    				System.out.println(sysobj.course_availability(userId+" course_availability"));
	    				ch=5;
    					break;
	    		case 4: 
    					System.out.println("Enter Student Id: ");
    					sc = new Scanner(System.in);
    					String student = sc.nextLine();
    					student_menu(student,sysobj);
    					
    					break;
					
	    		default:	    					
	    				
	    				System.out.println("ENTER CHOICE : ");
	    				System.out.println("1. ADD A NEW COURSE.");
	    				System.out.println("2. DELETE A COURSE.");
	    				System.out.println("3.CHECK AVAILABILITY");
	    				System.out.println("4. CHECK STUDENT PROFILE");
	    				System.out.println("0. LOGOUT.");
						sc=new Scanner(System.in);

	    				ch = sc.nextInt();	
	    				break;
	    		
	    	}
	    	
	    }
	    System.out.println(" LOGGING OUT ");
	    
	    sc.close();

	
	
	}
	    
	    
	    
	    
	    
	public static void main(String[] args) {

		try {
			ORB orb = ORB.init(args, null); 
			//-ORBInitialPort 1050 -ORBInitialHost localhost
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			Scanner c = new Scanner(System.in);
			System.out.println("Welcome to the DCRS system ");
			System.out.println("Enter user Id: ");
			for (;;) {
				String aa = c.nextLine();
				if(aa.equals("COMPA")|| aa.equals("SOENA")||aa.equals("INSEA")) {
					if(aa.equals("COMPA")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("comp"));
						advisor_menu(aa, sysobj);
					}else if(aa.equals("SOENA")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("soen"));
						advisor_menu(aa,sysobj);
					}else if(aa.equals("INSEA")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("inse"));
						advisor_menu(aa,sysobj);
					}
				}else if(aa.contains("COMPS")|| aa.contains("SOENS")||aa.contains("INSES")) {
					if(aa.equals("COMPS")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("comp"));
						student_menu(aa, sysobj); 
					}else if(aa.equals("SOENS")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("soen"));
						student_menu(aa,sysobj);
					}else if(aa.equals("INSES")) {
						system sysobj = (system) systemHelper.narrow(ncRef.resolve_str("inse"));
						student_menu(aa,sysobj);
					}
				}
				System.out.println("-----------------------------------");
				
				break;
			}
			c.close();
		} catch (Exception e) {
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace(); 
		}
	}
}
