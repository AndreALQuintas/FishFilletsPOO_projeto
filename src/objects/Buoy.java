package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Vector2D;

public class Buoy extends GameObject {
	
	public Buoy(Room room) {
		super(room);
		addTag("Light");
		addTag("Buoy");
	}
	
	@Override
	public String getName() {
		return "buoy";
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		if (!dir.equals(Direction.UP.asVector())) return true;

		if (other.hasTag("Heavy") || other.hasTag("Light")) {
			System.out.println(getRoom().getObjectAtPoint(getPosition().plus(Direction.DOWN.asVector())));
			if (getRoom().getObjectAtPoint(getPosition().plus(Direction.DOWN.asVector())) == null) {
				setPosition(getPosition().plus(Direction.DOWN.asVector()));
			}
			
		}
		return false;
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
