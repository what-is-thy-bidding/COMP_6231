package com.web.client;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.web.service.WebServices;


class Consumer extends Thread {
	   private String client;
	   private WebServices Interface;

	   Consumer(String client){
		   this.client=client;
	   }
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

	   public void run() {
		   System.out.println(client + " thread has started to run ..... \n");
		   try {	
		   		if(client.contains("COMP")) {
		   			URL url= new URL("http://localhost:8080/comp?wsdl");
		   			QName qName=new QName("http://impl.service.web.com/","systemObjService");
		   			Service service=Service.create(url, qName);
		   			Interface=service.getPort(WebServices.class);
				
		   		}else if(client.contains("SOEN")) {
		   			URL url= new URL("http://localhost:9000/soen?wsdl");
		   			QName qName=new QName("http://impl.service.web.com/","systemObjService");
		   			Service service=Service.create(url, qName);
		   			Interface=service.getPort(WebServices.class);
		   		}else if(client.contains("INSE")) {
		   			URL url= new URL("http://localhost:8000/inse?wsdl");
		   			QName qName=new QName("http://impl.service.web.com/","systemObjService");
		   			Service service=Service.create(url, qName);
		   			Interface =service.getPort(WebServices.class);
		   		}
		   	}catch(Exception e){
		   		System.out.print(e);
		   	}
		   		
		   		
			if(client.contains("COMPS")||client.contains("INSES")|| client.contains("SOENS")) {
	    	  if(client.contains("COMPS")) {
	    		  //add a course 
	    		  //drop a course
	    		  String command= add_course(client, "COMP 6231 FALL");
	    		  System.out.println(Interface.add_course(command));
	    		  String command2= drop_course(client, "COMP 6231 FALL");
	    		  System.out.println(Interface.drop_course(command2));
	    		  
	    	  }else if(client.contains("INSES")) {
	    		  String command= add_course(client, "COMP 6231 FALL");
	    		  System.out.println(Interface.add_course(command));
	    		 
	    		  String enrolled=drop_course(client,"COMP 6231 FALL");
  				  String swap=add_course(client,"COMP 6651 FALL");
  				  System.out.println(Interface.swap_course(enrolled, swap));
	    		  //add a comp course and then swap it
	    		  //swap the registered code with a course in a different term
  				  enrolled=drop_course(client, "COMP 6651 FALL");
  				  swap=add_course(client,"INSE 6110 WINTER");
  				  System.out.println(Interface.swap_course(enrolled, swap));
	    	  }else if(client.contains("SOENS")) {
	    		//add 2 INSE courses and then a COMP course in the same semester and then try to add another INSE course in a new term
	    		  //try and add another course in a different semester
	    		  String command= add_course(client, "INSE 6110 FALL");
	    		  System.out.println(Interface.add_course(command));
	    		  String command2= add_course(client, "INSE 6150 FALL");
	    		  System.out.println(Interface.add_course(command2));
	    		  String command3=add_course(client,"COMP 6231 FALL");
	    		  System.out.println(Interface.add_course(command3));
	    		  String command5=add_course(client,"INSE 6110 WINTER");
	    		  System.out.println(Interface.add_course(command5));
	    		  String command4=add_course(client,"COMP 6651 FALL");
	    		  System.out.println(Interface.add_course(command4));
	    		  
	    	  }
	      }else if(client.contains("COMPA")||client.contains("INSEA")|| client.contains("SOENA")) {
	    	  if(client.contains("COMPA")) {
	    		  //delete COMP 6231 FALL course and then re add it with 2 seats
	    		  String command= delete_course(client,"COMP 6231 FALL");
	    		  System.out.println(Interface.delete_course(command));
	    		  String command2= new_course(client,"COMP 6231 FALL 2");
	    		  System.out.println(Interface.new_course(command2));
	    		  
	    	  }else if(client.contains("INSEA")) {
	    		  //drop a comp course and add a new INSE course
	    		  String command=delete_course(client,"COMP 6231 FALL");
	    		  System.out.println(Interface.delete_course(command));
	    		  String command2=new_course(client,"INSE 7832 FALL 0");
	    		  System.out.println(Interface.new_course(command2));
	    	  }else if(client.contains("SOENA")) {
	    		  //try to delete a non existent course
	    		  String command=delete_course(client,"SOEN 6443 FALL");
	    		  System.out.println(Interface.delete_course(command));
	    	  } 
	      }
	   }
	}
public class client { 
	static WebServices Interface;
	

	
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

	
	
	public static void student_menu(String userId) throws MalformedURLException{

		
		Scanner sc=new Scanner(System.in);
 		System.out.print(Interface.list_course(userId));
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
	    				System.out.println(Interface.add_course(course_Id));
	    			    ch=5; 
	    			    break; 
	    		
	    		case 2: 
    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE_ID TO DROP AND WHICH TERM(eg. COMP 6231 FALL): ");
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				System.out.println(Interface.drop_course(drop_course(userId, course_drop)));
	    				ch=5;
	    				break; 
	    		
	    		case 3: System.out.println(Interface.course_availability(userId+" course_availability"));
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
		    		System.out.println(Interface.swap_course(enrolled,swap));	
		    		ch=5;
		    		break;
						
	    				
	    		default:
	    			
	    				if(userId.contains("COMPS")){
	    					System.out.print(Interface.list_course(userId));
	    				}else if(userId.contains("SOENS")){
	    					System.out.print(Interface.list_course(userId));
	    				}else if(userId.contains("INSES")){ 
	    					System.out.print(Interface.list_course(userId)); 
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
	
	
	public static void advisor_menu(String userId) throws MalformedURLException{
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
	    				System.out.println(Interface.new_course(course_Id)); 
	    			    ch=5;
	    			    break;
	    		
	    		case 2: 

    					System.out.println("------------------------------ \n");
    					System.out.println("WHICH COURSE WOULD YOU LIKE TO DELETE AND WHICH TERM(eg. COMP 6231 FALL): "); 
						sc=new Scanner(System.in);
	    				String course_drop = sc.nextLine();
	    				course_drop = delete_course(userId, course_drop);
	    				System.out.println(Interface.delete_course(course_drop));
	    				ch=5;
	    				break; 
	    				
	    		case 3: 
	    				System.out.println(Interface.course_availability(userId+" course_availability"));
	    				ch=5;
    					break;
	    		case 4: 
    					System.out.println("Enter Student Id: ");
    					sc = new Scanner(System.in);
    					String student = sc.nextLine();
    					student_menu(student);
    					
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
			


			Scanner c = new Scanner(System.in);
			System.out.println("Welcome to the DCRS system ");
			System.out.println("Enter user Id: ");
			for (;;) {
				String aa = c.nextLine();
				if(aa.contains("COMP")) {
					URL url= new URL("http://localhost:8080/comp?wsdl");
					QName qName=new QName("http://impl.service.web.com/","systemObjService");
					Service service=Service.create(url, qName);
					Interface=service.getPort(WebServices.class);
					
				}else if(aa.contains("SOEN")) {
					URL url= new URL("http://localhost:9000/soen?wsdl");
					QName qName=new QName("http://impl.service.web.com/","systemObjService");
					Service service=Service.create(url, qName);
					Interface=service.getPort(WebServices.class);
				}else if(aa.contains("INSE")) {
					URL url= new URL("http://localhost:8000/inse?wsdl");
					QName qName=new QName("http://impl.service.web.com/","systemObjService");
					Service service=Service.create(url, qName);
					Interface =service.getPort(WebServices.class);
				}
				if(aa.equals("COMPA")|| aa.equals("SOENA")||aa.equals("INSEA")) {
					advisor_menu(aa);
				}else if(aa.contains("COMPS")|| aa.contains("SOENS")||aa.contains("INSES")) {
					student_menu(aa);
				}
				
				System.out.println("-----------------------------------");
				
				break;
			}
			c.close();
			String client[]= {"COMPA","INSEA","SOENS","COMPS","INSES","SOENA"};
		      int slaveCount = 6;
		      Thread[] slaves = new Thread[slaveCount];
		      for(int i = 0; i < slaveCount; i++) {
		            slaves[i] = new Consumer(client[i]);
		      }
		      for(int i = 0; i < slaveCount; i++) {
		         slaves[i].start();
		      }
		      for(int i = 0; i < slaveCount; i++) {
		         try {
		            slaves[i].join();
		         } catch(InterruptedException ie) {
		               System.err.println(ie.getMessage());
		         } finally {
		            System.out.println("slave "+ i+"("+client[i] + ") has died");
		         }
		      }
		      System.out.println();
		    
				
			/*URL url= new URL("http://localhost:8080/comp?wsdl");
			QName qName=new QName("http://impl.service.web.com/","systemObjService");
			Service service=Service.create(url, qName);
			Interface=service.getPort(WebServices.class);
			System.out.println(Interface.add(1, 2));*/
		
			
		} catch (Exception e) {
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace(); 
		}
	}

	
}


