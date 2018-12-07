package request_information;

import java.io.Serializable;

public class request_information implements Serializable{
	
	private static final long serialVersionUID = 1L;
	int REPLICA_ID;
	int request_id;
	String request;
	public boolean FRONT_END_INSERTION;
	public boolean result;
	public request_information(int id, String command){
		request=command;
		request_id= id;
		FRONT_END_INSERTION=true;
	}
	public request_information() {
	}
	public int get_request_id() {
		return request_id;
	}
	public String get_request() {
		return request;
	}
	public void set_request_id(int id) {
		request_id= id;
	}
	public void set_command(String command) {
		request=command;
	}
	public void set_replica(int i) {
		REPLICA_ID=i;
	}
	public int get_replica_id() {
		return REPLICA_ID;
	}
	public void set_result(boolean bool) {
		result=bool;
	}
	public boolean get_result() {
		return result;
	}
}
