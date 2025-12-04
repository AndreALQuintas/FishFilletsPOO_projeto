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
	
	public void teleport(GameObject other) {
		other.setPosition(otherPortal.getPosition());
	}

	
	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		System.out.println("MKMKMKMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
		System.out.println(other.getName());
		System.out.println(other.getPosition());
		System.out.println(otherPortal.getPosition());
		other.setPosition(otherPortal.getPosition());
		System.out.println(other.getPosition());

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
