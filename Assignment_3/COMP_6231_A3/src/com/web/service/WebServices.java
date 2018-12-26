package com.web.service;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)

public interface WebServices {
	public String course_availability(String command);
	public String add_course (String command);
	public String drop_course(String command);
	public String new_course(String command);
	public String delete_course(String command);
	public String swap_course(String command1, String command2);	
	public String list_course(String command);
}
