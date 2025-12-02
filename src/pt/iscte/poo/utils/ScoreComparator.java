package pt.iscte.poo.utils;

import java.util.Comparator;

public class ScoreComparator implements Comparator<User> {

	
	public int compare(User u1, User u2) {
		if(u1.getMoveCount()==u2.getMoveCount()) {
			if(u1.getTime().totalSeconds()-u2.getTime().totalSeconds()==0) {
				Comparator<String> comp = String.CASE_INSENSITIVE_ORDER;
				return comp.compare(u1.getName(), u2.getName());
			}
			return u1.getTime().totalSeconds()-u2.getTime().totalSeconds();
		}return u1.getMoveCount()-u2.getMoveCount();
	}
}
