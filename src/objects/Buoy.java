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
		if (!dir.equals(Direction.UP.asVector())) return super.doCollision(other, dir);

		if (other.hasTag("Heavy") || other.hasTag("Light")) {
			GameObject downObject = getRoom().getObjectAtPoint(getPosition().plus(Direction.DOWN.asVector()));
			System.out.println(downObject);
			if (downObject == null) {
				setPosition(getPosition().plus(Direction.DOWN.asVector()));
			} else {
				super.doCollision(downObject, dir);
				System.out.println("OTHERRRRR: " + other.getName());
			}
			
		}
		return super.doCollision(other, dir);
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
