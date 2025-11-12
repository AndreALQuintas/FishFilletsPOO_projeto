package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import objects.SmallFish;
import objects.BigFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private char currentPlayer = 'b';
	private final int INVALID_INPUT = -1;
	
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

	private void changePlayer() {
		currentPlayer = (currentPlayer == 'b') ? 's' : 'b';
	}

	private int getInput() {
		if (!ImageGUI.getInstance().wasKeyPressed()) return INVALID_INPUT;
		return ImageGUI.getInstance().keyPressed();
	}

	private void treatInput(int key) {
		if (key != INVALID_INPUT ) {
			if (key == KeyEvent.VK_SPACE)
				changePlayer();
			else if (Direction.isDirection(key)){
				if (currentPlayer == 'b')
					BigFish.getInstance().move(Direction.directionFor(key).asVector());
				else
					SmallFish.getInstance().move(Direction.directionFor(key).asVector());
			}
		}
	}

	@Override
	public void update(Observed source) {

		int key = getInput();
		treatInput(key);
		
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	private void processTick() {		
		lastTickProcessed++;
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
	
}
