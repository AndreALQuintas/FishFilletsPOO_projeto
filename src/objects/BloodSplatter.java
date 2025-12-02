package objects;
import pt.iscte.poo.game.Room;

public class BloodSplatter extends GameObject {
	
	public BloodSplatter(Room room) {
		super(room);
		addTag("Background");
	}
	
	@Override
	public String getName() {
		return "bloodSplatter";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
	
}
