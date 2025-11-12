package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Collidable;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject implements Collidable {
	
	public GameCharacter(Room room) {
		super(room);
	}
	
	public void move(Vector2D dir) {
		Point2D destination = getPosition().plus(dir); 
		GameObject destinationObject = getRoom().getObjectAtPoint(destination);
		if (destinationObject == null) {
			setPosition(destination);
		} else {
			if (doCollision(destinationObject, dir))
				setPosition(destination);
		}	
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
}