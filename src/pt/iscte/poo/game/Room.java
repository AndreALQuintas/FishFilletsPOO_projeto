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
import objects.SteelHorizontal;
import objects.Wall;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private List<GameObject> objects;
	private String roomName;
	private GameEngine engine;
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;
	
	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	private void setEngine(GameEngine engine) {
		this.engine = engine;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		engine.updateGUI();
	}
	
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		engine.updateGUI();
	}
	
	public List<GameObject> getObjects() {
		return objects;
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
	
	public static Room readRoom(File f, GameEngine engine) {
		Room r = new Room();
		r.setEngine(engine);
		r.setName(f.getName());
		
		Scanner scan;
		try {
			scan = new Scanner(f);
			int y = 0, width = -1;

			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (width == -1)
					width = line.length();
				for (int i = 0; i<width; i++) {
					GameObject water = new Water(r);
					water.setPosition(new Point2D(i, y));
					r.addObject(water);
					if (i < line.length())
						instanciateChar(r, line.charAt(i), new Point2D(i, y));
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
		GameObject gObj = null;
		switch (c) {
			case 'W':
				gObj = new Wall(r);
				break;
			case 'B':
				gObj = BigFish.getInstance();
				break;
			case 'S':
				gObj = SmallFish.getInstance();
				break;
			case 'H':
				gObj = new SteelHorizontal(r);
				break;

			
			default:
				break;

			}
			if (gObj != null) {
				gObj.setPosition(pos);
				r.addObject(gObj);
			}
	}
	
}