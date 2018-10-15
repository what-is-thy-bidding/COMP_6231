package Assignment_1;

class courses_availability {
	private String course_name;
	private String term;
	private int course_count;
	
	
	courses_availability(String course, String season){
		course_name = course;
		term=season;
		course_count = 0;
	}
	String get_course_name(){
		return course_name;
	}
	
	String get_term(){
		return term;
	}
	int get_course_count(){
		return course_count;
	}
	void set_course_count(int count){
		course_count=count;
	}
	String increase_count(){
		if(course_count==2){
			return "course is full that semester";
		}else{
			course_count=course_count+1;
			return "true";
		}
	}
	void decrease_count(){
		if(course_count>0) {
			course_count=course_count-1;
		}
	}
	
	String print_data(){
		int seats_left = 2-course_count;
		String seat = Integer.toString(seats_left);
			return course_name + "                " + term + "     " +seat+"/2"; 
			 
	}
	
	
}
