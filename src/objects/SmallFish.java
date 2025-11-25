package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);

	private static final String cantGoThroughTags[] = {"Wall", "Heavy", "BigFish"};
	private static final String canPushTags[] = {"Light", "TempHeavy"};
	
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
