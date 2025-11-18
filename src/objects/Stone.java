package objects;
import pt.iscte.poo.game.Room;

public class Stone extends GameObject {

	public Stone(Room room) {
		super(room);
		addTag("Heavy");
	}
	
	@Override
	public String getName() {
		return "stone";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
}
