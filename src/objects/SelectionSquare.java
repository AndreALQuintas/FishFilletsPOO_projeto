package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SelectionSquare extends GameCharacter{

	private static SelectionSquare sq = new SelectionSquare(null);
	public SelectionSquare(Room room) {
		super(room);
		addTag("Selection");
        addTag("Background");
	}

    public static SelectionSquare getInstance() {
		return sq;
	}

	@Override
	public String getName() {
		return "selectionSquare";
	}

	@Override
	public int getLayer() {
		return 3;
	}

    @Override
    public void move(Vector2D dir) {
        Point2D destination = getPosition().plus(dir); 
        if (destination.getX() < 0 || destination.getX() > 9) return;
        if (destination.getY() < 0 || destination.getY() > 9) return;
        setPosition(destination);
    }

    @Override
    public String[] getCantGoThroughTags() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCantGoThroughTags'");
    }

    @Override
    public String[] getCanPushTags() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCanPushTags'");
    }

}
