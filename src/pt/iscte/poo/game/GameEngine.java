package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import pt.iscte.poo.utils.EnemyMove;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Time;
import pt.iscte.poo.utils.User;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine extends Engine implements Observer {
	private int totalMoveCount;
	private int lastMoveCount = 0;
	private int lastBigFishMoveCount=0;
	private int lastSmallFishMoveCount=0;
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

			doEnemyMovement();
			updateMoveCount();
			updateHeader();
		}
	}

	private void doEnemyMovement() {
		for (GameObject gObj : currentRoom.getEnemyObjects()) {
			if (gObj instanceof EnemyMove) 
				((EnemyMove)gObj).doEnemyMove();
		}
	}

	private boolean bothPlayersLeftMap() {
		return BigFish.getInstance().leftMap() && SmallFish.getInstance().leftMap();
	}

<<<<<<< HEAD
=======
	public void pauseGame() {
		gameRunning = false;
	}
	
	
	public void changeScore() {
		File score = new File("score.txt");
		System.out.println(score.getAbsolutePath());
		ArrayList<User> users= new ArrayList<>();
		String name = gui.askUser("Qual é o seu username");
		Time time = timeAsClass();
		try{
			Scanner scanner = new Scanner(score);
			while(scanner.hasNextLine()) {
				String line=scanner.nextLine();
				String[] data=line.split(" ");
				users.add(new User(data[0], Integer.parseInt(data[1]),new Time(Integer.parseInt(data[2]))));
			}
			User thisUser =new User(name,totalMoveCount, time);
			System.out.println(thisUser);
			//Verifica se user já existe no score
			
			
			users.sort((a,b)->{
				if(a.getMoveCount()==b.getMoveCount()) {
					return a.getTime().totalSeconds()-b.getTime().totalSeconds();
				}return a.getMoveCount()-b.getMoveCount();
			});
			boolean foundTheSame= false;
			for(User u: users) {
				if(u.getName().equals(thisUser.getName())){
					foundTheSame=true;
					if(u.getMoveCount()==thisUser.getMoveCount()) {
						if(u.getTime().totalSeconds()<thisUser.getTime().totalSeconds()) {
							users.remove(u);
							users.add(thisUser);
							
						}
					}else if(u.getMoveCount()>thisUser.getMoveCount()) {
						users.remove(u);
						users.add(thisUser);
					}
				}
			}
			if(!foundTheSame && !users.isEmpty() ) {
				User worstUser = users.getLast();
				if(worstUser.getMoveCount()==thisUser.getMoveCount()) {
					if(worstUser.getTime().isOtherBigger(thisUser.getTime())) {
						users.removeLast();
						users.add(thisUser);
					}
				}
			}
			if(users.size()<10 && !foundTheSame) {
				users.add(thisUser);
			}
			
			scanner.close();
			
			users.sort((a,b)->{
				if(a.getMoveCount()==b.getMoveCount()) {
					return a.getTime().totalSeconds()-b.getTime().totalSeconds();
				}return a.getMoveCount()-b.getMoveCount();
			});
			if(users.size()>10) {
				users.removeLast();
			}
			PrintWriter writer=new PrintWriter(new FileWriter(score,false));
			for(User user: users) {
				writer.println(user.toString());
			}
			writer.close();
		}catch(FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado.");
			return;
		}catch(IOException e) {
			return;
		}
		showScore(users);
		
	}
	
	
	public void showScore(ArrayList<User> users) {
		String shown = "";
		for(User user: users) {
			shown += user.getName() + ": Passos- " + user.getMoveCount() + " Tempo- " + user.getTime().toString() + "\n" ;
		}
		gui.showMessage("Top 10 highscores", shown);
	}


>>>>>>> henrique
	public void resetCurrentRoom(){
		Room r = Room.readRoom(new File("./rooms/" + currentRoom.getName()), this);
		rooms.put(currentRoom.getName(),r);
		currentRoom = r;
		currentPlayer = 'b';
		totalMoveCount=lastMoveCount;
		BigFish.getInstance().setMoveCount(lastBigFishMoveCount);
		SmallFish.getInstance().setMoveCount(lastSmallFishMoveCount);
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
			changeScore();
			return;
		}

		changeRoom(nextRoomName);
		
	}

	private void changeRoom(String room) {
		currentRoom = rooms.get(room);
		currentPlayer = 'b';
		lastMoveCount= totalMoveCount;
		lastBigFishMoveCount=BigFish.getInstance().getMoves();
		lastSmallFishMoveCount=SmallFish.getInstance().getMoves();
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
		String s=String.valueOf(seconds);
		String m=String .valueOf(minutes);
		if(seconds<10) {
			s = "0" + s;
		}
		if(minutes<10) {
			m = "0" + m;
		}
		
		return m + ":" + s;
	}
	
	public Time timeAsClass() {
		return new Time(gui.getTicks()/2);
	}
	
	public void updateHeader() {
		gui.setStatusMessage((currentPlayer=='b'? "Peixe Grande" : "Peixe Pequeno")
				 + " | Passos: " + totalMoveCount + " | " + totalTime());
	}
}
