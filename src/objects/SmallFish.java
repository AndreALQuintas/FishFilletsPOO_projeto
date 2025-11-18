package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);

	private static final String cantGoThroughTags[] = {"Wall", "Heavy", "BigFish"};
	private static final String canPushTags[] = {"Light"};
	
	private SmallFish(Room room) {
		super(room);
		addTag("Fixed");
		addTag("SmallFish");
	}

	public static SmallFish getInstance() {
		return sf;
	}

	@Override
	public String getName() {
		return "smallFish" + getCurrentDir();
	}

	@Override
	public int getLayer() {
		return 2;
	}

	private boolean push(GameObject other, Vector2D dir) {
		Point2D otherDestination = other.getPosition().plus(dir); //MUDAR LIMITE
		GameObject otherDestinationObject = getRoom().getObjectAtPoint(otherDestination);
		if (otherDestination.getX() >= 10 || otherDestination.getY() >= 10) 
			return false;
		if (otherDestinationObject != null && otherDestinationObject.hasTag("Fixed"))
			return false;
		other.setPosition(otherDestination);
		return true;
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		System.out.println(this.getName() + " collides with " + other.getName() + ", other.hastag: " + other.getTagList());
		for (String tag : cantGoThroughTags) {
			if (other.hasTag(tag)) return false;
		}
		for (String tag : canPushTags) {
			if (other.hasTag(tag))
				if (!push(other, dir)) return false;
		}
		return true;
	}

}
