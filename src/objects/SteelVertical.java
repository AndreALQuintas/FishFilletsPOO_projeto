package objects;

import pt.iscte.poo.game.Room;

public class SteelVertical extends GameObject{
	
	public SteelVertical (Room room) {
		super(room);
		addTag("Fixed");
		addTag("Wall");
	}
	
	@Override
	public String getName() {
		return "steelVertical";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
