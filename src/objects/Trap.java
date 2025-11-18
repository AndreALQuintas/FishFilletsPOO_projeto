package objects;

import pt.iscte.poo.game.Room;

public class Trap extends GameObject{
	
	public Trap(Room room) {
		super(room);
		addTag("KillBigFish");
	}
	
	@Override
	public String getName() {
		return "trap";
	}
	
	public int getLayer() {
		return 1;
	}
}
