package systemApp;


/**
* systemApp/systemOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from U:/workspace/COMP_6231_A2/src/system.idl
* Monday, October 29, 2018 8:56:36 PM EDT
*/

public interface systemOperations 
{
  String course_availability (String command);
  String add_course (String command);
  String drop_course (String command);
  String new_course (String command);
  String delete_course (String command);
  String swap_course (String command1, String command2);
  String list_course (String command);
  void shutdown ();
} // interface systemOperations
