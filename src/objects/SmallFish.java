package objects;

import pt.iscte.poo.game.Room;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);

	private static final String cantGoThroughTags[] = {"Wall", "Heavy", "BigFish", "Immovable"};
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
	public String[] getCantGoThroughTags() {
		return cantGoThroughTags;
	}

	@Override
	public String[] getCanPushTags() {
		return canPushTags;
	}

}
