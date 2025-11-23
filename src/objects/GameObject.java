package objects;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Collidable;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Tags;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile, Tags, Collidable{
	
	private Point2D position;
	private Room room;
	private List<String> tagList;
	
	public GameObject(Room room) {
		this.room = room;
		tagList = new ArrayList<>();
	}
	
	public GameObject(Point2D position, Room room) {
		this.position = position;
		this.room = room;
		tagList = new ArrayList<>();
	}

	@Override
	public boolean doCollision(GameObject other, Vector2D dir) {
		System.out.println("auto collision with " + other.getName() + ", other.tags: " + other.getTagList());

		if (other.hasTag("Wall")) {
			if (this.hasTag("TempHeavy")) {
				this.removeTag("TempHeavy");
				this.removeTag("Heavy");
			} else if (this.hasTag("TempSuperHeavy")){
				this.removeTag("TempSuperHeavy");
				this.removeTag("SuperHeavy");
			}
			return false;
		}

		if (this.hasTag("Light") && other.hasTag("Light")) {
			other.addTag("Heavy");
			other.addTag("TempHeavy");
		}

		if (this.hasTag("Heavy") && !this.hasTag("TempHeavy") && other.hasTag("Heavy") && !other.hasTag("TempHeavy")) {
			other.addTag("SuperHeavy");
			other.addTag("TempSuperHeavy");
		}

		if (this.hasTag("SuperHeavy") && other.hasTag("BigFish") ||
			this.hasTag("Heavy") && other.hasTag("SmallFish") ||
			this.hasTag("KillBigFish") && other.hasTag("BigFish")) {
			
			room.removeObject(other);
			GameObject gObj = new BloodSplatter(room);
			gObj.setPosition(other.getPosition());
			room.addObject(gObj);
			room.endGame();

			return true;
		}
        return false;
    }

	@Override
    public List<String> getTagList() {
        return tagList;
    }

	public void setPosition(int i, int j) {
		position = new Point2D(i, j);
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
}
