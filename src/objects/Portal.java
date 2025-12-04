package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Vector2D;

public class Portal extends GameObject{
	private Portal otherPortal;

	public Portal(Room room) {
		super(room);
		addTag("Portal");
	}

	public void linkPortals(Portal otherPortal) {
		this.otherPortal = otherPortal;
		otherPortal.otherPortal = this;
	}
	
	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		other.setPosition(otherPortal.getPosition());
		return true;
	}

	@Override
	public String getName() {
		return "portal";
	}
	
	public int getLayer() {
		return 1;
	}
}
