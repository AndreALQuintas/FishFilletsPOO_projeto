package pt.iscte.poo.utils;

public class Time{
	private final int totalSeconds;
	
	public Time(int totalSeconds) {
		this.totalSeconds=totalSeconds;
	}
	
	
	public int getMinutes() {
		return totalSeconds/60;
	}
	
	public int getSeconds() {
		return totalSeconds%60;
	}
	
	public int totalSeconds() {
		return totalSeconds;
	}
	
	public Time addTime (Time time) {
		return new Time(totalSeconds+time.totalSeconds());
	}
	
	public Time subtractTime (Time time) {
		return new Time(totalSeconds+time.totalSeconds());
	}
	
	public boolean isOtherBigger(Time time) {
		return totalSeconds<time.totalSeconds();
	}
	
	public boolean isOtherSmaller(Time time) {
		return totalSeconds>time.totalSeconds();
	}
	
	public String toString() {
		String seconds=String.valueOf(getSeconds());
		if(getSeconds()<10) {
			seconds="0" +seconds;
		}
		String minutes=String.valueOf(getMinutes());
		if(getMinutes()<10) {
			minutes="0" + minutes;
		}
		return minutes+":"+seconds;
	}
}