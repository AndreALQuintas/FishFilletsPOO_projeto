package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.SmallFish;
import objects.BigFish;
import objects.Bomb;
import objects.Explosion;
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
	private static final String nonGravityAffectedTags[] = {"Fixed", "BigFish", "SmallFish"};
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room0.txt");
		updateGUI();		
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		changeRoom(currentRoom.getName());
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
		System.out.println(rooms);
		rooms.put(currentRoom.getName(),r);
		currentRoom = r;
		currentPlayer = 'b';
		updateGUI();
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		gameRunning = true;
		lastTickProcessed = ImageGUI.getInstance().getTicks();
	}

	private void changeRoom(String room) {
		currentRoom = rooms.get(room);
		currentPlayer = 'b';
		updateGUI();
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom); 
		SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());
		BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition());
	}

	private void updateFallingState() {
		for (GameObject gObj : currentRoom.getNonBackgroundObjects()) {
			Vector2D dir = Direction.UP.asVector();
			Point2D above = gObj.getPosition().plus(dir);
			GameObject aboveObj = currentRoom.getObjectAtPoint(above);
			if (aboveObj == null) {
				if (gObj.hasTag("TempHeavy")) {
					gObj.removeTag("TempHeavy");
					gObj.removeTag("Heavy");
				}

				if (gObj.hasTag("TempSuperHeavy")) {
					gObj.removeTag("TempSuperHeavy");
					gObj.removeTag("SuperHeavy");
				}
			} else {

				if (gObj.hasTag("Light") && aboveObj.hasTag("Light")) {
					gObj.addTag("Heavy");
					gObj.addTag("TempHeavy");
				}

				if (gObj.hasTag("Heavy") && aboveObj.hasTag("Heavy") && !aboveObj.hasTag("TempHeavy")) {
					gObj.addTag("SuperHeavy");
					gObj.addTag("TempSuperHeavy");
				}
			}
		}

	}

	@Override
	public void update(Observed source) {
		int key = getInput();
		treatInput(key);

		Point2D bfPos = BigFish.getInstance().getPosition();
		Point2D sfPos = SmallFish.getInstance().getPosition();
		if (bfPos.getX() >= 10 || bfPos.getX() < 0 || bfPos.getY() >= 10 || bfPos.getY() < 0)
			if (sfPos.getX() >= 10 || sfPos.getX() < 0 || sfPos.getY() >= 10 || sfPos.getY() < 0) {
				changeRoom("room1.txt");
			}

		updateFallingState();
		
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
		} else if (gObj.getName().equals("bomb")) {
			Bomb bomb = (Bomb) gObj;
			bomb.isFalling = true;
		}
		gObj.setPosition(destination);
		
	}

	private void processTick() {		
		lastTickProcessed++;
		System.out.println("Tick done!");
		List<GameObject> nonBackgroundObjects = new ArrayList<GameObject>(currentRoom.getNonBackgroundObjects());
		for (GameObject gObj : nonBackgroundObjects) {
			if (!hasNonGravityAffectedTag(gObj))
				applyGravity(gObj);
		}

		//TEMPORARIO MUDAR DEPOIS PQ TA UMA MERDA
		List<GameObject> timeAffectedObjects = new ArrayList<GameObject>(currentRoom.getTimeAffectedObjects());
		for (GameObject gObj : timeAffectedObjects) {
			if (((Explosion) gObj).isDone())
				{currentRoom.removeObject(gObj);}
			else
				{((Explosion) gObj).setDone();}
		}
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
	
}
