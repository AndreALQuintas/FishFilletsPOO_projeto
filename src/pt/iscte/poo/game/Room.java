package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.Water;
import objects.BigFish;
import objects.GameObject;
import objects.SmallFish;
import pt.iscte.poo.utils.ObjectList;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private List<GameObject> objects;
	private List<GameObject> nonBackgroundObjects;
	private List<GameObject> timeAffectedObjects;
	private String roomName;
	private Engine engine;
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;
	private static final int roomWidth = 10;
	
	public Room() {
		objects = new ArrayList<GameObject>();
		nonBackgroundObjects = new ArrayList<GameObject>();
		timeAffectedObjects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	private void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		if (!obj.hasTag("Background"))
			nonBackgroundObjects.add(obj);
		if (obj.hasTag("TimeAffected"))
			timeAffectedObjects.add(obj);
		engine.updateGUI();
	}

	public void endGame() {
		engine.endGame();
	}
	
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		if (!obj.hasTag("Background"))
			nonBackgroundObjects.remove(obj);
		if (obj.hasTag("TimeAffected"))
			timeAffectedObjects.remove(obj);
		engine.updateGUI();
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}

	public List<GameObject> getNonBackgroundObjects() {
		return nonBackgroundObjects;
	}

	public List<GameObject> getTimeAffectedObjects() {
		return timeAffectedObjects;
	}

	public void setSmallFishStartingPosition(Point2D heroStartingPosition) {
		this.smallFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getSmallFishStartingPosition() {
		return smallFishStartingPosition;
	}
	
	public void setBigFishStartingPosition(Point2D heroStartingPosition) {
		this.bigFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getBigFishStartingPosition() {
		return bigFishStartingPosition;
	}

	public GameObject getObjectAtPoint(Point2D point) {
		for (GameObject obj : objects) {
			if (obj.hasTag("Background")) continue;
			if (obj.getPosition().equals(point)) return obj;
		}
		return null;
	}
	
	public static Room readRoom(File f, Engine engine) {
		Room r = new Room();
		r.setEngine(engine);
		r.setName(f.getName());
		
		Scanner scan;
		try {
			scan = new Scanner(f);
			int y = 0, width = roomWidth;
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				for (int x = 0; x<width; x++) {
					GameObject water = new Water(r);
					water.setPosition(new Point2D(x, y));
					r.addObject(water);
					if (x < line.length())
						instanciateChar(r, line.charAt(x), new Point2D(x, y));
				}
				y++;
			}

		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro nao encontrado");
			e.printStackTrace();
		}

		return r;
		
	}

	private static void instanciateChar(Room r, char c, Point2D pos) {
		GameObject gObj = ObjectList.instantiate(c, r);

		if (gObj instanceof BigFish) {
			r.setBigFishStartingPosition(pos);
		} else if (gObj instanceof SmallFish) {
			r.setSmallFishStartingPosition(pos);
		}

		if (gObj != null) {
			gObj.setPosition(pos);
			r.addObject(gObj);
		}	
	}
	
}