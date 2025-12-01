package objects;

import pt.iscte.poo.game.Room;

public class Krab extends GameObject {
	
	public Krab(Room room) {
		super(room);
		addTag("Enemy");
	}
	
	@Override
	public String getName() {
		return "krab";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
