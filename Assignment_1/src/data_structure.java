package Assignment_1;

import java.util.LinkedList;
import java.util.Iterator;

public class data_structure {
	private String username;
	private  static LinkedList<String> fall_courses=new LinkedList<String>();  //LIST OF FALL COURSES
	private  static LinkedList<String> winter_courses=new LinkedList<String>();//LIST OF WINTER COURSES
	private  static LinkedList<String> summer_courses=new LinkedList<String>();//LIST OF SUMMER COURSES
	private int comp=0, soen=0, inse=0;
	
	data_structure(String Id){
		 username=Id;
	}
	public String get_Id(){
		return username;
	}
	
	public  String add_courses_fall(String fall_register){
		if(username.contains("COMP") && fall_register.contains("INSE")) {
			if(inse==1) {
				return "Cannot add more courses from INSE.";
			}else {
				inse=1;
			}
			
		}else if(username.contains("COMP") && fall_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from soen";
			}else {
				soen=1;
			}
		}else if(username.contains("INSE") && fall_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more courses from COMP";
			}else {
				comp=1;
			}
		}else if(username.contains("INSE") && fall_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from SOEN";
			}else {
				soen=1;
			}
		}else if(username.contains("SOEN") && fall_register.contains("INSE")) {
			if(inse==1) {
				return "cannot add more INSE courses";
			}else {
				inse=1;
			}
		}else if(username.contains("SOEN") && fall_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more COMP courses";
			}else {
				comp=1;
			}
		}
		
		
		
		Iterator<String> it = fall_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(fall_register)==0){
				
				return "The course has already been added to the semester";
				
			}
		}
		if(fall_courses.size()==3){
			return "Cannot add more courses to the semester";
		}else{
			fall_courses.addLast(fall_register);
			return "Course Added to the Fall semester";
		}
	}
	
	
	public  String add_courses_winter(String winter_register){
		if(username.contains("COMP") && winter_register.contains("INSE")) {
			if(inse==1) {
				return "Cannot add more courses from INSE.";
			}else {
				inse=1;
			}
			
		}else if(username.contains("COMP") && winter_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from soen";
			}else {
				soen=1;
			}
		}else if(username.contains("INSE") && winter_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more courses from COMP";
			}else {
				comp=1;
			}
		}else if(username.contains("INSE") && winter_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from SOEN";
			}else {
				soen=1;
			}
		}else if(username.contains("SOEN") && winter_register.contains("INSE")) {
			if(inse==1) {
				return "cannot add more INSE courses";
			}else {
				inse=1;
			}
		}else if(username.contains("SOEN") && winter_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more COMP courses";
			}else {
				comp=1;
			}
		}
		
		
		Iterator<String> it = winter_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(winter_register)==0){
				return "The course has already been added to the semester";
			}
		}
		if(fall_courses.size()==3){
			return "Cannot add more courses to the semester";
		}else{
			winter_courses.addLast(winter_register);
			return "Course Added to the Winter semester";

		}
	}
	
	public  String add_courses_summer(String summer_register){
		if(username.contains("COMP") && summer_register.contains("INSE")) {
			if(inse==1) {
				return "Cannot add more courses from INSE.";
			}else {
				inse=1;
			}
			
		}else if(username.contains("COMP") && summer_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from soen";
			}else {
				soen=1;
			}
		}else if(username.contains("INSE") && summer_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more courses from COMP";
			}else {
				comp=1;
			}
		}else if(username.contains("INSE") && summer_register.contains("SOEN")) {
			if(soen==1) {
				return "Cannot add more courses from SOEN";
			}else {
				soen=1;
			}
		}else if(username.contains("SOEN") && summer_register.contains("INSE")) {
			if(inse==1) {
				return "cannot add more INSE courses";
			}else {
				inse=1;
			}
		}else if(username.contains("SOEN") && summer_register.contains("COMP")) {
			if(comp==1) {
				return "Cannot add more COMP courses";
			}else {
				comp=1;
			}
		}
		Iterator<String> it = summer_courses.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(summer_register)==0){
				return "The course has already been added to the semester";
			}
		}
		if(fall_courses.size()==3){
			return "Cannot add more courses to the semester";
		}else{
			summer_courses.addLast(summer_register);
			return "Course Added to the Winter semester";	
		}
	}
	
	
	public String drop_course_fall(String register){
		Iterator<String> it = fall_courses.iterator();
		while(it.hasNext()){
			if(register.equals(it.next())){
				it.remove();
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
