package objects;

import pt.iscte.poo.game.Room;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	
	private SmallFish(Room room) {
		super(room);
		addTag("Immovable");
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
	public String getName() {
		return "smallFishLeft";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean doCollision(GameObject other) {
		System.out.println(this.getName() + " collides with " + other.getName() + ", other.hastag: " + other.getTagList());
		if (other.hasTag("Immovable")) 
			return false;
		return true;
	}

}
