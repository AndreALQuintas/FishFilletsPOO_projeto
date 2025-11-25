package objects;
import pt.iscte.poo.game.Room;

public class Explosion extends GameObject {
	private boolean done = false;
	public Explosion(Room room) {
		super(room);
		addTag("TimeAffected");
		addTag("Background");
	}

	public boolean isDone() {
		return done;
	}

	public void setDone() {
		done = true;
	}
	
	@Override
	public String getName() {
		return "explosion";
	}
	
	@Override
	public int getLayer() {
		return 2;
	}
	
}
