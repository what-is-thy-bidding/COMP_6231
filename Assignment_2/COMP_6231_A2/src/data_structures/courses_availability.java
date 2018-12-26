package data_structures;

public class courses_availability {
	private String course_name;
	private String term;
	private int course_count;
	private int course_capacity;
	
	public courses_availability(String course, String season, int capacity) {
		course_name=course;
		term=season;
		course_count=0;
		course_capacity=capacity;
	}
	public courses_availability(String course, String season){
		course_name = course;
		term=season;
		course_count = 0;
		course_capacity=2;
	}
	public String get_course_name(){
		return course_name;
	}
	
	public String get_term(){
		return term;
	}
	public int get_course_capacity() {
		return course_capacity;
	}
	public int get_course_count(){
		return course_count;
	}
	void set_course_count(int count){
		course_count=count;
	}
	public String increase_count(){
		if(course_count==course_capacity){
			return "course is full that semester";
		}else{
			course_count=course_count+1;
			return "true";
		}
	}
	public void decrease_count(){
		if(course_count>0) {
			course_count=course_count-1;
		}
	}
	
	public String print_data(){
		int seats_left = course_capacity-course_count;
		String seat = Integer.toString(seats_left);
			return course_name + "                " + term + "     " +seat+"/"+course_capacity; 
			 
	}
	
	
}
