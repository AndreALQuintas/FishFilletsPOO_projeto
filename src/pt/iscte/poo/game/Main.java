package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("editor")) {
			ImageGUI gui = ImageGUI.getInstance();
			LevelEditorEngine engine = new LevelEditorEngine();
			//gui.setStatusMessage("Level Editor");
			gui.registerObserver(engine);
			gui.go();
		} else {
			ImageGUI gui = ImageGUI.getInstance();
			GameEngine engine = new GameEngine();
			gui.setStatusMessage("Good luck!");
			gui.registerObserver(engine);
			gui.go();
		}
	}
	
}
