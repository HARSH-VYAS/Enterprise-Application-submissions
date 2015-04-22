package hello;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

public class Polls {
	@Id
	String id;

	String question,started_at,expired_at;
	String [] choice= new String[2];

	@JsonIgnore
	int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	//	@JsonIgnore
//	ArrayList <Integer> moderatorList = new ArrayList<Integer>();


	int [] result= new int[2];
	public Polls(){

	}

	public Polls(String id, String question, String started_at, String expired_at, String[] choice,int[] result) {

		super();
		this.id = id;
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = choice;
		//this.moderatorList = moderatorList;
		this.result = result;
		this.flag=0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	public String[] getChoice() {
		return choice;
	}
	public void setChoice(String[] choice) {
		this.choice = choice;
	}
	public int[] getResult() {
		return result;
	}
	public void setResult(int[] result) {
		this.result = result;
	}
	/*public ArrayList<Integer> getModeratorList() {
		return moderatorList;
	}

	public void setModeratorList(ArrayList<Integer> moderatorList) {
		this.moderatorList = moderatorList;
	}*/
		

}
