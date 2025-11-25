package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final String cantGoThroughTags[] = {"OnlySmall", "Wall", "SmallFish", "SuperHeavy"};
	private static final String canPushTags[] = {"Light", "Heavy"};
	
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
		return "bigFish" + getCurrentDir();
	}

	@Override
	public int getLayer() {
		return 2;
	}

	

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		System.out.println(this.getName() + " collides with " + other.getName() + ", other.tags: " + other.getTagList());
		for (String tag : cantGoThroughTags) {
			if (other.hasTag(tag)) return false;
		}
		for (String tag : canPushTags) {
			if (other.hasTag(tag))
				return push(other, dir);
		}
		return true;
	}

	@Override
	public String[] getCantGoThroughTags() {
		return cantGoThroughTags;
	}

	@Override
	public String[] getCanPushTags() {
		return canPushTags;
	}
}
