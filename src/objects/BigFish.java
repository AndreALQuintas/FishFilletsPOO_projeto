package objects;

import pt.iscte.poo.game.Room;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final String cantGoThroughTags[] = {"OnlySmall", "Wall", "SmallFish", "SuperHeavy", "Immovable"};
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
	public String[] getCantGoThroughTags() {
		return cantGoThroughTags;
	}

	@Override
	public String[] getCanPushTags() {
		return canPushTags;
	}
}
