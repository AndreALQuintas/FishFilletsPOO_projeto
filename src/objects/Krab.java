package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.EnemyMove;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends GameObject implements EnemyMove{
	
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
	public void doEnemyMove() {
		
	}
}
