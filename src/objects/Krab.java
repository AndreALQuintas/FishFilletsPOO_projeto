package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.EnemyMove;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends GameObject implements EnemyMove{
	
	private boolean skipFirstMove = false;
	public Krab(Room room) {
		super(room);
		addTag("Enemy");
	}
	
	@Override
	public String getName() {
		return "krab";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		if (!other.hasTag("BigFish")) return super.doCollision(other, dir);

		getRoom().removeObject(this);
		GameObject gObj = new BloodSplatter(getRoom());
		gObj.setPosition(other.getPosition());
		getRoom().addObject(gObj);

		return false;
	}

	@Override
	public void doEnemyMove() {
		if (!skipFirstMove) {
			skipFirstMove = true;
			return;
		}
		Direction[] possibleDirection = {Direction.LEFT, Direction.RIGHT};
		
		Direction randomDirection = possibleDirection[(int)(Math.random()*2)];
		Point2D nextPos = getPosition().plus(randomDirection.asVector());

		if (getRoom().getObjectAtPoint(nextPos) != null) return;

		setPosition(nextPos);

	}
}
