package pt.iscte.poo.game;

import java.util.List;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import objects.SmallFish;
import objects.BigFish;
import objects.GameObject;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private boolean gameRunning = true;
	private char currentPlayer = 'b';
	private static final int INVALID_INPUT = -1;
	private static final List<String> nonGravityAffectedTags = Arrays.asList(
    	"Fixed", "BigFish", "SmallFish"
	);
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room0.txt");
		updateGUI();		
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
	}

	private void loadGame() {
		File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}

	private boolean hasNonGravityAffectedTag(GameObject gObj) {
		for (String tag : nonGravityAffectedTags) {
			if (gObj.hasTag(tag)) return true;
		}
		return false;
	}

	private void changePlayer() {
		currentPlayer = (currentPlayer == 'b') ? 's' : 'b';
	}

	private int getInput() {
		if (!ImageGUI.getInstance().wasKeyPressed()) return INVALID_INPUT;
		return ImageGUI.getInstance().keyPressed();
	}

	private void treatInput(int key) {
		if (key == INVALID_INPUT ) return;
		if (key == KeyEvent.VK_SPACE)
			changePlayer();
		else if (key == KeyEvent.VK_R)
			resetCurrentRoom();
		else if (Direction.isDirection(key) && gameRunning){
			if (currentPlayer == 'b')
				BigFish.getInstance().move(Direction.directionFor(key).asVector());
			else
				SmallFish.getInstance().move(Direction.directionFor(key).asVector());
		}
	}

	public void endGame() {
		gameRunning = false;
		
	}

	public void resetCurrentRoom(){
		Room r = Room.readRoom(new File("./rooms/" + currentRoom.getName()), this);
		rooms.put(currentRoom.getName(),r);
		currentRoom = r;
		currentPlayer = 'b';
		updateGUI();
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		gameRunning = true;
	}

	@Override
	public void update(Observed source) {
		int key = getInput();
		treatInput(key);
		
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			if (!gameRunning) return;
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	private void applyGravity(GameObject gObj) {
		Vector2D dir = Direction.DOWN.asVector();
		Point2D destination = gObj.getPosition().plus(dir);
		GameObject destinationObject = currentRoom.getObjectAtPoint(destination);
		if (destinationObject != null) {
			if (!gObj.doCollision(destinationObject, dir)) return;
		}
		gObj.setPosition(destination);
		
	}

	private void processTick() {		
		lastTickProcessed++;
		System.out.println("Tick done!");
		for (GameObject gObj : currentRoom.getNonBackgroundObjects()) {
			if (!hasNonGravityAffectedTag(gObj))
				applyGravity(gObj);
		}
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
	
}
