package data_structures;

import java.util.LinkedList;
import java.util.Iterator;

public class data_structure {
	private String username;
	private  static LinkedList<String> fall_courses=new LinkedList<String>();  //LIST OF FALL COURSES
	private  static LinkedList<String> winter_courses=new LinkedList<String>();//LIST OF WINTER COURSES
	private  static LinkedList<String> summer_courses=new LinkedList<String>();//LIST OF SUMMER COURSES
	private int comp=0, soen=0, inse=0;
	 
	public data_structure(String Id){
		 username=Id;
	}
	public String get_Id(){
		return username;
	} 
	
	public  String add_courses_fall(String fall_register){
		if(username.contains("COMP") && fall_register.contains("INSE")) { 
			if(inse==2) {
				return "Cannot add more courses from INSE.";
			}			
		}else if(username.contains("COMP") && fall_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from soen";
			}
		}else if(username.contains("INSE") && fall_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more courses from COMP";
			}
		}else if(username.contains("INSE") && fall_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from SOEN";
			}
		}else if(username.contains("SOEN") && fall_register.contains("INSE")) {
			if(inse==2) {
				return "cannot add more INSE courses";
			}
		}else if(username.contains("SOEN") && fall_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more COMP courses";
			}
		}
		
		
		
		Iterator<String> it = fall_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(fall_register)==0){
				return "The course has already been added to the semester";
				
			}
		}
		if(fall_courses.size()==3){
			return "Cannot add more courses to the fall semester";
		}else{
			fall_courses.addLast(fall_register);
			if(fall_register.contains("COMP")){
				comp++;
			}else if(fall_register.contains("SOEN")){
				soen++;
			}else if(fall_register.contains("INSE")){
				inse++;
			}
			return "Course Added to the Fall semester";
		}
	}
	
	
	public  String add_courses_winter(String winter_register){
		if(username.contains("COMP") && winter_register.contains("INSE")) {
			if(inse==2) {
				return "Cannot add more courses from INSE.";
			}
			
		}else if(username.contains("COMP") && winter_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from soen";
			}
		}else if(username.contains("INSE") && winter_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more courses from COMP";
			}
		}else if(username.contains("INSE") && winter_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from SOEN";
			}
		}else if(username.contains("SOEN") && winter_register.contains("INSE")) {
			if(inse==2) {
				return "cannot add more INSE courses";
			}
		}else if(username.contains("SOEN") && winter_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more COMP courses";
			}
		}
		
		
		Iterator<String> it = winter_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(winter_register)==0){
				return "The course has already been added to the semester";
			}
		}
		if(winter_courses.size()==3){
			return "Cannot add more courses to the winter semester";
		}else{
			winter_courses.addLast(winter_register);
			if(winter_register.contains("COMP")){
				comp++;
			}else if(winter_register.contains("SOEN")){
				soen++;
			}else if(winter_register.contains("INSE")){
				inse++;
			}
			return "Course Added to the Winter semester";

		}
	}
	
	public  String add_courses_summer(String summer_register){
		if(username.contains("COMP") && summer_register.contains("INSE")) {
			if(inse==2) {
				return "Cannot add more courses from INSE.";
			}			
		}else if(username.contains("COMP") && summer_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from soen";
			}
		}else if(username.contains("INSE") && summer_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more courses from COMP";
			}
		}else if(username.contains("INSE") && summer_register.contains("SOEN")) {
			if(soen==2) {
				return "Cannot add more courses from SOEN";
			}
		}else if(username.contains("SOEN") && summer_register.contains("INSE")) {
			if(inse==2) {
				return "cannot add more INSE courses";
			}
		}else if(username.contains("SOEN") && summer_register.contains("COMP")) {
			if(comp==2) {
				return "Cannot add more COMP courses";
			}
		}
		Iterator<String> it = summer_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(summer_register)==0){
				return "The course has already been added to the semester";
			}
		}
		if(summer_courses.size()==3){
			return "Cannot add more courses to the summer semester";
		}else{
			if(summer_register.contains("COMP")){
				comp++;
			}else if(summer_register.contains("SOEN")){
				soen++;
			}else if(summer_register.contains("INSE")){
				inse++;
			}
			summer_courses.addLast(summer_register);
			return "Course Added to the Summer semester";	
		}
	}
	
	
	public String drop_course_fall(String register){
		Iterator<String> it = fall_courses.iterator(); 
		while(it.hasNext()){
			if(register.equals(it.next())){
				it.remove();
				if(register.contains("COMP")){
					comp--;
				}else if(register.contains("SOEN")){
					soen--;
				}else if(register.contains("INSE")){
					inse--;
				}
				return "COURSE DELETED";
			}
		}
		return "COURSE DOESN'T EXIST IN THE FALL SEMESTER";
	}
	public String drop_course_winter(String register){
		Iterator<String> it = winter_courses.iterator();
		while(it.hasNext()){
			if(register.equals(it.next())){				
				it.remove();
				if(register.contains("COMP")){
					comp--;
				}else if(register.contains("SOEN")){
					soen--;
				}else if(register.contains("INSE")){
					inse--;
				}
				return "COURSE DELETED";

			}
		}
		return "COURSE DOESN'T EXIST IN THE WINTER SEMESTER";
	}
	public String drop_course_summer(String register){
		Iterator<String> it = summer_courses.iterator();
		while(it.hasNext()){
			if(register.equals(it.next())){
				it.remove();
				if(register.contains("COMP")){
					comp--;
				}else if(register.contains("SOEN")){
					soen--;
				}else if(register.contains("INSE")){
					inse--;
				}
				return "COURSE DELETED";
			}
		}
		return "COURSE DOESN'T EXIST IN THE SUMMER SEMESTER";
	}
	
	public String print(){
		String print = "\n"+"The List of courses Registered in : \n"+
		
		"FALL COURSES : ";
		String courses="";
		Iterator<String> fall = fall_courses.iterator();
		while(fall.hasNext()){
			courses += fall.next()+ "  ";
		}
		courses=courses+"\n";
		print = print+ courses+"\n"+"WINTER COURSES : ";
		
		courses="";
		Iterator<String> winter = winter_courses.iterator();
		while(winter.hasNext()){
			courses += winter.next()+ "  ";
		}
		courses=courses+"\n";
		print = print+ courses+"\n"+"SUMMER COURSES : ";
		
		courses="";
		Iterator<String> summer = summer_courses.iterator();
		while(summer.hasNext()){
			courses += summer.next()+ "  ";
		}
		
		courses=courses+"\n";
		print = print+ courses;
		/**/
		return print;
	}
}
