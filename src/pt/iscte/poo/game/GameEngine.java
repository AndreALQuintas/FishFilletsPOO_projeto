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
import objects.GameCharacter;
import objects.GameObject;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine extends Engine implements Observer {
	private int totalMoveCount;
	private ImageGUI gui = ImageGUI.getInstance();
	private Map<String,Room> rooms;
	//private Room currentRoom;
	private int lastTickProcessed = 0;
	//private boolean gameRunning = true;
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
		if (currentPlayer == 'b') {
			currentPlayer = (SmallFish.getInstance().leftMap()) ? 'b' : 's';
		} else {
			currentPlayer = (BigFish.getInstance().leftMap()) ? 's' : 'b';
		}
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
			if (currentPlayer == 'b') {
				BigFish.getInstance().move(Direction.directionFor(key).asVector());
			} else {
				SmallFish.getInstance().move(Direction.directionFor(key).asVector());
			}

			GameCharacter currentPlayerInstance = (currentPlayer == 'b') ? BigFish.getInstance() : SmallFish.getInstance();
			Point2D newPos = currentPlayerInstance.getPosition();
			if (newPos.getX() < 0 || newPos.getX() > 9 ||newPos.getY() < 0 ||newPos.getY() > 9) {
				currentPlayerInstance.setLeftMap();
				currentRoom.removeObject(currentPlayerInstance);
				changePlayer();

				if (bothPlayersLeftMap()) {
					goToNextRoom();
				}
			}

			updateMoveCount();
			updateHeader();
		}
	}

	private boolean bothPlayersLeftMap() {
		return BigFish.getInstance().leftMap() && SmallFish.getInstance().leftMap();
	}

	public void resetCurrentRoom(){
		Room r = Room.readRoom(new File("./rooms/" + currentRoom.getName()), this);
		System.out.println(rooms);
		rooms.put(currentRoom.getName(),r);
		currentRoom = r;
		currentPlayer = 'b';
		updateGUI();
		
		SmallFish.getInstance().reset(currentRoom);
		BigFish.getInstance().reset(currentRoom);

		gameRunning = true;
		lastTickProcessed = ImageGUI.getInstance().getTicks();
	}

	private void goToNextRoom() {
		String currentRoomName = currentRoom.getName();
		int currentRoomNumber = currentRoomName.charAt(currentRoomName.indexOf('.') - 1);
		String nextRoomName = "room" + ((char)(currentRoomNumber+1) + ".txt");
		
		if (!rooms.containsKey(nextRoomName)) {
			System.out.println("!!!JOGO ACABOU!!!");
			return;
		}

		changeRoom(nextRoomName);
		
	}

	private void changeRoom(String room) {
		currentRoom = rooms.get(room);
		currentPlayer = 'b';
		updateGUI();
		SmallFish.getInstance().reset(currentRoom);
		BigFish.getInstance().reset(currentRoom);
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

		updateFallingState();
		
		int t = gui.getTicks();
		while (lastTickProcessed < t) {
			if (!gameRunning) return;
			processTick();
		}
		gui.update();
	}

	private void applyGravity(GameObject gObj) {
		Vector2D dir = Direction.DOWN.asVector();
		Point2D destination = gObj.getPosition().plus(dir);
		GameObject destinationObject = currentRoom.getObjectAtPoint(destination);
		if (destinationObject != null) {
			if (!gObj.doCollision(destinationObject, dir)) return;
		} else if (gObj.getName().equals("bomb")) {
			Bomb bomb = (Bomb) gObj;
			bomb.startFalling();
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
	
		updateHeader();
		

		//TEMPORARIO MUDAR DEPOIS PQ TA UMA MERDA
		List<GameObject> timeAffectedObjects = new ArrayList<GameObject>(currentRoom.getTimeAffectedObjects());
		for (GameObject gObj : timeAffectedObjects) {
			if (((Explosion) gObj).isDone())
				{currentRoom.removeObject(gObj);}
			else
				{((Explosion) gObj).setDone();}
		}
	}
	
	public void updateMoveCount() {
		totalMoveCount = BigFish.getInstance().getMoves()+SmallFish.getInstance().getMoves();
	}
	
	public String totalTime() {
		
		int seconds=(gui.getTicks()/2);
		int minutes=seconds/60;
		seconds-=minutes*60;
		
		return minutes + ":" + seconds;
	}
	
	public void updateHeader() {
		gui.setStatusMessage((currentPlayer=='b'? "Peixe Grande" : "Peixe Pequeno")
				 + " | Passos: " + totalMoveCount + " | " + totalTime());
	}
}
