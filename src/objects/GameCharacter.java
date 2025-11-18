package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {
	private String currentDir = "Left";
	public GameCharacter(Room room) {
		super(room);
	}

	public String getCurrentDir() {
		return currentDir;
	}
	
	public void move(Vector2D dir) {
		if (dir.equals(Direction.LEFT.asVector())) {
			currentDir = "Left";
		} else if (dir.equals(Direction.RIGHT.asVector())) {
			currentDir = "Right";
		}
		Point2D destination = getPosition().plus(dir); 
		GameObject destinationObject = getRoom().getObjectAtPoint(destination);
		if (destinationObject == null) {
			setPosition(destination);
		} else {
			if (doCollision(destinationObject, dir)) {
				setPosition(destination);
				destinationObject.doCollision(this, dir);
			}
		}	
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
}