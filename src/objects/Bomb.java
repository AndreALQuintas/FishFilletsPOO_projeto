package objects;
import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends GameObject {
	public boolean isFalling = false;
	public Bomb(Room room) {
		super(room);
		addTag("Light");
	}
	
	@Override
	public String getName() {
		return "bomb";
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
		}
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		if (isFalling && !other.hasTag("BigFish") && !other.hasTag("SmallFish")) {
			System.out.println("BOOOOOOM");
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
