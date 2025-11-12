package objects;

import pt.iscte.poo.game.Room;

public class Anchor extends GameObject {
	
	public Anchor(Room room) {
		super(room);
		addTag("Heavy");
	}
	
	@Override
	public String getName() {
		return "anchor";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
