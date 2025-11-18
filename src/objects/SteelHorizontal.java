package objects;

import pt.iscte.poo.game.Room;

public class SteelHorizontal extends GameObject {

	public SteelHorizontal(Room room) {
		super(room);
		addTag("Fixed");
		addTag("Wall");
	}

	@Override
	public String getName() {
		return "steelHorizontal";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}
