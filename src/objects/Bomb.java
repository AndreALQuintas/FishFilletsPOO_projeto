package objects;
import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends GameObject {
	private boolean isFalling = false;
	public Bomb(Room room) {
		super(room);
		addTag("Light");
	}
	
	@Override
	public String getName() {
		return "bomb";
	}

	public void startFalling() {
		isFalling = true;
	}

	private void doExplosion() {
		Point2D mainPos = getPosition();
		List<Point2D> positionsList =  mainPos.getNeighbourhoodPoints();
		positionsList.add(mainPos);
		Room r = getRoom();
		for (Point2D pos : positionsList) {
			GameObject gObj = new Explosion(r);
			gObj.setPosition(pos);
			r.addObject(gObj);
			GameObject placedObject = r.getObjectAtPoint(pos);
			if (placedObject != null) {
				if (placedObject instanceof GameCharacter)
					((GameCharacter)placedObject).killCharacter();
				r.removeObject(placedObject);
			}
		}
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		if (isFalling && !other.hasTag("BigFish") && !other.hasTag("SmallFish")) {
			doExplosion();
			getRoom().removeObject(this);
		}
		return super.doCollision(other, dir);
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
	
}
