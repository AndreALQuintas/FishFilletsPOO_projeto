package objects;

import java.util.Arrays;
import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final List<String> cantGoThroughTags = Arrays.asList(
    	"OnlySmall", "Wall", "SmallFish"
	);
	private static final List<String> canPushTags = Arrays.asList(
    	"Light", "Heavy"
	);
	
	private BigFish(Room room) {
		super(room);
		addTag("Fixed");
		addTag("BigFish");
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		return "bigFishLeft";
	}

	@Override
	public int getLayer() {
		return 1;
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

		other.doCollision(this, dir);
		return true;
	}
}
