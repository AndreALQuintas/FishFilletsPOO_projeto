package objects;
import pt.iscte.poo.game.Room;

public class Bomb extends GameObject {
	
	public Bomb(Room room) {
		super(room);
		addTag("Light");
	}
	
	@Override
	public String getName() {
		return "bomb";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
	
}
