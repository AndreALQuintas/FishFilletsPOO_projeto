package pt.iscte.poo.utils;

public class User {
	String name;
	Time time;
	int moveCount;
	
	public User(String name,int moveCount, Time time) {
		this.name=name;
		this.time=time;
		this.moveCount=moveCount;
	}
	
	public String getName() {
		return name;
	}
	
	public Time getTime() {
		return time;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void setTime(Time newTime) {
		time=newTime;
	}
	
	public void setMoveCount(int newMoveCount) {
		moveCount=newMoveCount;
	}
	
	public String toString() {
		return name +" " + moveCount +" " + time.totalSeconds();
	}
}
