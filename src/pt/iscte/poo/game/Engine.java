package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public abstract class Engine {
    public Room currentRoom;
	public boolean gameRunning = true;

    public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
    
    public void endGame() {
		gameRunning = false;
	}
}
