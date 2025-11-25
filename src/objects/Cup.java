package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Vector2D;

public class Cup extends GameObject {
	
	public Cup(Room room) {
		super(room);
		addTag("Light");
	}
	
	@Override
	public String getName() {
		return "cup";
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		if (other.hasTag("OnlySmall"))
			return true;
		return super.doCollision(other, dir);
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
