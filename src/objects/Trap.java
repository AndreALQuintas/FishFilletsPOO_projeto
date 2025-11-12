package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends GameObject{
	
	public Trap(Room room) {
		super(room);
	}
	
	@Override
	public String getName() {
		return "trap";
	}
	
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) { 
		super.doCollision(other, dir);
		System.out.println("Dead - Endgame...");
		return false;
	}
}
