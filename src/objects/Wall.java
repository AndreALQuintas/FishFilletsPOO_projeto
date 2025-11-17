package objects;

import pt.iscte.poo.game.Room;

public class Wall extends GameObject {

	public Wall(Room room) {
		super(room);
		addTag("Fixed");
		addTag("Wall");
	}

	@Override
	public String getName() {
		return "wall";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

}
