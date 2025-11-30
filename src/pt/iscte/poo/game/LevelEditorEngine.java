package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import objects.BigFish;
import objects.GameCharacter;
import objects.GameObject;
import objects.SelectionSquare;
import objects.SmallFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.GameObjectList;
import pt.iscte.poo.utils.Point2D;

public class LevelEditorEngine extends Engine implements Observer {
	private ImageGUI gui = ImageGUI.getInstance();
    private List<String> allRooms;
	private int lastTickProcessed = 0;
    private String currentEditMode;
	private static final int INVALID_INPUT = -1;

    public LevelEditorEngine () {
        System.out.println("LevelEditor initiated");
        gui.setStatusMessage("  Editor de Niveis - room0.txt");

        init();
    }

    private void refreshLevels() {
        allRooms = getAllRooms();
    }

    private void init() {
        refreshLevels();

        String answer = "";
        while (answer == null || !validateOptionBasedAnswer(answer)) {
            answer = gui.askUser(getAllRoomsString() + "\nEscolha opcao(numero): Adicionar(1) | Modificar(2) | Remover(3) | Organizar(4)");
            if(answer == null){
                // MUDAR
                gui.dispose();
                return;
            }
        }

        treatOptionBasedAnswer(answer);
		
        
        
        lastTickProcessed = ImageGUI.getInstance().getTicks();
    }

    private boolean validateOptionBasedAnswer(String answer) {
        return  answer.equals("1") || answer.equalsIgnoreCase("Adicionar") ||
                answer.equals("2") || answer.equalsIgnoreCase("Modificar") ||
                answer.equals("3") || answer.equalsIgnoreCase("Remover") ||
                answer.equals("4") || answer.equalsIgnoreCase("Organizar");
    }

    private boolean treatOptionBasedAnswer(String answer) {
        if (answer.equals("1") || answer.equalsIgnoreCase("Adicionar")) {
            showAddMenu();
            return true;
        }else if (answer.equals("2") || answer.equalsIgnoreCase("Modificar")) {
            showModifyMenu();
            return true;
        }else if (answer.equals("3") || answer.equalsIgnoreCase("Remover")) {
            showRemoveMenu();
            return true;
        }else if (answer.equals("4") || answer.equalsIgnoreCase("Organizar")) {
            showSortMenu();
            return true;
        }

        return false;
    }

    private int[] ValidateSortAnswer(String ans) {
        if (ans == null) return null;

        String[] parts = ans.trim().split(" ");

        if (parts.length != 2) return null;

        int a,b;
        try {
            a = Integer.parseInt(parts[0]);
            b = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }

        if (a == b) return null;

        if (a < 0 || a >= allRooms.size() ||
            b < 0 || b >= allRooms.size()) 
            return null;
        
        return new int[]{a, b};
    }

    private void showSortMenu() {
		String answer = null;
        int[] roomNumbers = null;
        while(roomNumbers == null) {
            answer = gui.askUser(getAllRoomsString() + "\nColoque o numero das duas salas separadas por um espaco:\nEx.: 0 2");
            roomNumbers = ValidateSortAnswer(answer);
        }
        System.out.println(roomNumbers[0] + " " + roomNumbers[1]);
        swapRoomNames(roomNumbers[0], roomNumbers[1]);

        init();
	}

    private void showRemoveMenu() {
        int answer = -1;
        while (answer < 0 || answer >= allRooms.size()) {
            try {
                answer = Integer.parseInt(gui.askUser(getAllRoomsString() + "\nEscolha o numero da sala para remover: "));
            }catch(NumberFormatException ex){
                answer = -1;
            }
        }

        File file = new File("./rooms/room" + answer + ".txt");
        file.delete();
        reorderRoomOrderAfterDeletion(answer);

        init();
    }

    private void showModifyMenu() {
        currentEditMode = "modify";
        int answer = -1;
        while (answer < 0 || answer >= allRooms.size()) {
            try {
                answer = Integer.parseInt(gui.askUser(getAllRoomsString() + "\nEscolha o numero da sala para alterar: "));
            }catch(NumberFormatException ex){
                answer = -1;
            }
        }

        currentRoom = Room.readRoom(new File("./rooms/room" + answer + ".txt"), this);
        setupModifyObjects();
    }

    private void showAddMenu() {
        currentEditMode = "add";
        int roomNumber = getNewRoomNumber();
        String roomName = "room" + roomNumber + ".txt";

        currentRoom = Room.getEmptyRoom(roomName, this);
        setupModifyObjects();
        updateGUI();

    }

    private void setupModifyObjects() {
        SelectionSquare sq = SelectionSquare.getInstance();
        sq.setPosition(0,0);
        currentRoom.addObject(sq);

        gui.setStatusMessage("  Editor de Niveis - " + currentRoom.getName());
    }

    private int getNewRoomNumber() {
        return allRooms.size();
    }

    private void reorderRoomOrderAfterDeletion(int deletedNumber) {
        refreshLevels();
        if (allRooms.isEmpty()) return;

        String folderPath = "./rooms";

        for (int i = deletedNumber+1; i<allRooms.size(); i++) {
            Path filePath = Paths.get(folderPath+"/room"+i+".txt");
            Path destinationPath = Paths.get(folderPath+"/room"+(i-1)+".txt");

            try {
                Files.move(filePath, destinationPath);
                System.out.println(filePath + " mudou para " + destinationPath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("Reorder done!");
    }

    private List<String> getAllRooms() {
        List<String> allRooms = new ArrayList<>();
        File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			allRooms.add(f.getName());
		}
        return allRooms;
    }

    private String getAllRoomsString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Salas:\n");
        for (String room : allRooms) {
            sb.append("   -" + room + "\n");
        }

        return sb.toString();
    }

    private int getInput() {
		if (!ImageGUI.getInstance().wasKeyPressed()) return INVALID_INPUT;
		return ImageGUI.getInstance().keyPressed();
	}

    private void placeObject(int key) {
        char objChar = GameObjectList.getCharByKeyEvent(key);
        if (objChar == '\0') return;

        GameObject gObj = GameObjectList.instantiate(objChar, currentRoom);
        Point2D pos = SelectionSquare.getInstance().getPosition();
        
        if (gObj instanceof GameCharacter && currentRoom.getObjectAtPoint(pos) instanceof GameCharacter) return;
        
        removeObject();

        gObj.setPosition(pos);
        currentRoom.addObject(gObj);

    }

    private void removeObject() {
        Point2D pos = SelectionSquare.getInstance().getPosition();
        GameObject obj = currentRoom.getObjectAtPoint(pos);
        if (obj != null)
            currentRoom.removeObject(obj);
    }

    private void exitEditorAndSave() {
        if (!askToSave()) return;

        
        currentRoom.exportToFile(currentRoom.getName());
        init();
    }

    private boolean askToSave() {

        if (!currentRoom.getNonBackgroundObjects().contains(BigFish.getInstance()) ||
            !currentRoom.getNonBackgroundObjects().contains(SmallFish.getInstance())) {
                gui.showMessage("Aviso", "E necessario conter ambos os peixes para completar o nivel");
                return false;
        }

        String answer = null;
        System.out.println(currentEditMode);
        if (currentEditMode.equals("modify"))
            answer = gui.askUser("Guardar modificacoes no ficheiro '" + currentRoom.getName() + "'?: (s/n)");
        else if (currentEditMode.equals("add"))
            answer = gui.askUser("Guardar ficheiro '" + currentRoom.getName() + "'?: (s/n)");
        if (answer != null && answer.equalsIgnoreCase("s"))
            return true;
        return false;
    }

    private void treatInput(int key) {
		if (key == INVALID_INPUT ) return;
        if (key == KeyEvent.VK_BACK_SPACE)
            removeObject();
        else if (key == KeyEvent.VK_ESCAPE) {
            exitEditorAndSave();
        }
		else if (Direction.isDirection(key) && gameRunning){
            SelectionSquare.getInstance().move(Direction.directionFor(key).asVector());
        } else {
            placeObject(key);
        }
	}

    private void swapRoomNames(int a, int b) {
        String file1 = "./rooms/room"+a+".txt";
        String file2 = "./rooms/room"+b+".txt";

        Path path1 = Paths.get(file1);
        Path path2 = Paths.get(file2);
        
        if (!Files.exists(path1) || !Files.exists(path2)) {
            System.err.println("Error: files dont exist");
            return;
        }
        
        Path pathTemp = Paths.get(file1 + ".temp");
        
        try {
            Files.move(path1, pathTemp);      // file1 -> temp
            Files.move(path2, path1);         // file2 -> file1
            Files.move(pathTemp, path2);      // temp -> file2
            
            System.out.println("Successfully swapped: " + file1 + " - " + file2);
            return ;
            
        } catch (IOException e) {
            System.out.println("Error swapping files");
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observed source) {
        int key = getInput();
        treatInput(key);
        
        int t = gui.getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
        
        gui.update();
    }

    private void processTick() {		
		lastTickProcessed++;
        //System.out.println("Tick Done!");
    }
}
