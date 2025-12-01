package objects;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.PushAction;
import pt.iscte.poo.utils.Vector2D;

public class Stone extends GameObject implements PushAction {
	private boolean hasKrab = true;
	public Stone(Room room) {
		super(room);
		addTag("Heavy");
	}
	
	@Override
	public String getName() {
		return "stone";
	}

	public boolean hasKrab() {
		return hasKrab;
	}

	public void spawnKrab() {
		Point2D upPos = getPosition().plus(Direction.UP.asVector());
		if (getRoom().getObjectAtPoint(upPos) != null) return;

		GameObject krab = new Krab(getRoom());
		krab.setPosition(upPos);;
		getRoom().addObject(krab);
		hasKrab = false;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void getPushedAction(GameObject other, Vector2D dir) {
		if (hasKrab())
			spawnKrab();
	}
}
