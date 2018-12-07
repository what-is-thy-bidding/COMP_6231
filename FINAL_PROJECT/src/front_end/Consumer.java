package front_end;


class Consumer extends Thread {
	   private String client;

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
			return "enroll "+student_Id+" "+course_Id;
			
		}
		
		public static String drop_course(String student_Id, String course_Id ){
			return "drop "+student_Id+" "+course_Id;
		}

		public void run() {
		   System.out.println(client + " thread has started to run ..... \n");
		   try {	
		   		
		   		
			if(client.contains("COMPS")||client.contains("INSES")|| client.contains("SOENS")) {
	    	  if(client.contains("COMPS")) {
	    		  //add a course 
	    		  //drop a course
	    		  String command= add_course(client, "COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    		  String command2= drop_course(client, "COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command2);
	    		  
	    	  }else if(client.contains("INSES")) {
	    		  String command= add_course(client, "COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    		 
	    		  String enrolled=drop_course(client,"COMP 6231 FALL");
				  String swap=add_course(client,"COMP 6651 FALL");
				  FRONT_END.send_sequencer_request(swap);
				  FRONT_END.send_sequencer_request(enrolled);
	    		  //add a comp course and then swap it
	    		  //swap the registered code with a course in a different term
				  enrolled=drop_course(client, "COMP 6651 FALL");
				  swap=add_course(client,"INSE 6110 WINTER");
				  FRONT_END.send_sequencer_request(enrolled);
				  FRONT_END.send_sequencer_request(swap);
	    	  }else if(client.contains("SOENS")) {
	    		//add 2 INSE courses and then a COMP course in the same semester and then try to add another INSE course in a new term
	    		  //try and add another course in a different semester
	    		  String command= add_course(client, "INSE 6110 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    		  String command2= add_course(client, "INSE 6150 FALL");
	    		  FRONT_END.send_sequencer_request(command2);
	    		  String command3=add_course(client,"COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command3);
	    		  String command5=add_course(client,"INSE 6110 WINTER");
	    		  FRONT_END.send_sequencer_request(command5);
	    		  String command4=add_course(client,"COMP 6651 FALL");
	    		  FRONT_END.send_sequencer_request(command4);
	    		  
	    	  }
	      }else if(client.contains("COMPA")||client.contains("INSEA")|| client.contains("SOENA")) {
	    	  if(client.contains("COMPA")) {
	    		  //delete COMP 6231 FALL course and then re add it with 2 seats
	    		  String command= delete_course(client,"COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    		  String command2= new_course(client,"COMP 6231 FALL 2");
	    		  FRONT_END.send_sequencer_request(command2);
	    		  
	    	  }else if(client.contains("INSEA")) {
	    		  //drop a comp course and add a new INSE course
	    		  String command=delete_course(client,"COMP 6231 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    		  String command2=new_course(client,"INSE 7832 FALL 0");
	    		  FRONT_END.send_sequencer_request(command2);
	    	  }else if(client.contains("SOENA")) {
	    		  //try to delete a non existent course
	    		  String command=delete_course(client,"SOEN 6443 FALL");
	    		  FRONT_END.send_sequencer_request(command);
	    	  } 
	      }
	   }catch(Exception e) {
		   
	   }
	}
		public static void main(String args[]) {
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
		    
		}
}