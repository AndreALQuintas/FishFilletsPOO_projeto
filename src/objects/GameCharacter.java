package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.PushAction;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {
	private int moveCount=0;
	private boolean leftMap = false;
	public abstract String[] getCantGoThroughTags();
    public abstract String[] getCanPushTags();
	private String currentDir = "Left";
	public GameCharacter(Room room) {
		super(room);
		addTag("Player");
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public boolean leftMap() {
		return leftMap;
	}

	public void setLeftMap() {
		leftMap = true;
	}

	public void resetLeftMap() {
		leftMap = false;
	}

	public boolean push(GameObject other, Vector2D dir) {
		Point2D otherDestination = other.getPosition().plus(dir); //MUDAR LIMITE
		GameObject otherDestinationObject = getRoom().getObjectAtPoint(otherDestination);
		
		if (otherDestinationObject == null) {
			other.setPosition(otherDestination);
			return true;
		} else if (hasTag("SmallFish")) {
			return false;
		}
		
		if (otherDestinationObject.hasTag("Fixed")) {
            return false;
        }

		boolean canPushNext = false;
        for (String tag : getCanPushTags()) {
            if (otherDestinationObject.hasTag(tag)) {
                canPushNext = true;
                break;
            }
        }


		if (canPushNext) {
            if (push(otherDestinationObject, dir)) {
                // Se o próximo objeto foi empurrado com sucesso, mover o objeto atual
                other.setPosition(otherDestination);
                return true;
            }
            return false;
        }

		// Verificar se o objeto no destino impede a passagem
        for (String tag : getCantGoThroughTags()) {
            if (otherDestinationObject.hasTag(tag)) {
                return false;
            }
        }


		if (otherDestinationObject != null) {
			if (!push(otherDestinationObject, dir)) return false;
		}

		if (otherDestinationObject != null && otherDestinationObject.hasTag("Fixed"))
			return false;

		other.setPosition(otherDestination);
		return true;
	}

	public void reset(Room r) {
		setRoom(r);
		if (hasTag("BigFish"))
			setPosition(r.getBigFishStartingPosition());
		else
			setPosition(r.getSmallFishStartingPosition());
		resetLeftMap();
		currentDir = "Right";
	}
	
	public void move(Vector2D dir) {

		if (dir.equals(Direction.LEFT.asVector())) {
			currentDir = "Left";
		} else if (dir.equals(Direction.RIGHT.asVector())) {
			currentDir = "Right";
		} else if (dir.equals(Direction.DOWN.asVector())) {
			currentDir = "Down";
		} else if (dir.equals(Direction.UP.asVector())) {
			currentDir = "Up";
		}
		Point2D destination = getPosition().plus(dir); 
		GameObject destinationObject = getRoom().getObjectAtPoint(destination);
		if (destinationObject == null) {
			setPosition(destination);
			moveCount++;
		} else {
			if (doCollision(destinationObject, dir)) {
				setPosition(destination);
				destinationObject.doCollision(this, dir);
				moveCount++;
			}
		}	

	}
	
	public int getMoves() {
		return moveCount;
	}
	@Override
	public int getLayer() {
		return 2;
	}
	
	public void setMoveCount(int moves) {
		moveCount = moves;
	}
}