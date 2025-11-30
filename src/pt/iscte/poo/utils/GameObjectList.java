package pt.iscte.poo.utils;

import java.awt.event.KeyEvent;

import objects.*;
import pt.iscte.poo.game.Room;

public enum GameObjectList {
    ANCHOR(Anchor.class, 'A', KeyEvent.VK_A, false),
    BIGFISH(BigFish.class, 'B', KeyEvent.VK_B ,true),
    BOMB(Bomb.class, 'b', KeyEvent.VK_O, false),
    CUP(Cup.class, 'C', KeyEvent.VK_C, false),
    HOLEDWALL(HoledWall.class, 'X', KeyEvent.VK_X, false),
    SMALLFISH(SmallFish.class, 'S', KeyEvent.VK_S, true),
    STEELHORIZONTAL(SteelHorizontal.class, 'H', KeyEvent.VK_H, false),
    STEELVERTICAL(SteelVertical.class, 'V', KeyEvent.VK_V, false),
    STONE(Stone.class, 'R', KeyEvent.VK_R, false),
    TRAP(Trap.class, 'T', KeyEvent.VK_T, false),
    TRUNK(Trunk.class, 'Y', KeyEvent.VK_Y, false),
    WALL(Wall.class, 'W', KeyEvent.VK_W, false),
    //WATER(Water.class, ' ')
    ;

    private final Class<? extends GameObject> objectClass;
    private final char objectChar;
    private final int editKey;
    private final boolean isSingleton;

    private GameObjectList(Class<? extends GameObject> objectClass, char objectChar, int editKey, boolean isSingleton) {
        this.objectClass = objectClass;
        this.objectChar = objectChar;
        this.editKey = editKey;
        this.isSingleton = isSingleton;
    }

    public static char getCharByGameObject(GameObject gObj) {
        Class<? extends GameObject> gObjClass = gObj.getClass();
        for (GameObjectList obj : GameObjectList.values()) 
            if (gObjClass.equals(obj.objectClass)) 
                return obj.objectChar;
        return '\0';
    }

    public static char getCharByKeyEvent(int keyevent) {
        for (GameObjectList obj : GameObjectList.values()) 
            if (obj.editKey == keyevent) 
                return obj.objectChar;
        return '\0';
    }

    public static GameObject instantiate(char c, Room r) {
        if (c == '\0') return null;
        for (GameObjectList obj : GameObjectList.values()) {
            if (obj.objectChar != c) continue;

            try {
                GameObject gameObj;
                if (obj.isSingleton) {
                    java.lang.reflect.Method getInstanceMethod = obj.objectClass.getMethod("getInstance");
                    gameObj = (GameObject) getInstanceMethod.invoke(null);
                } else {
                    gameObj = obj.objectClass.getDeclaredConstructor(Room.class).newInstance(r);
                }
                System.out.println("Instanciou - " + gameObj.getName());
                return gameObj;
            } catch (Exception e) {
                System.err.println("Error instantiating object for char: " + c);
                System.err.println("Object class: " + obj.objectClass.getName());
                System.err.println("Is singleton: " + obj.isSingleton);
                e.printStackTrace();
                throw new RuntimeException("Failed to instantiate object for char: " + c, e);
            }
        }
        return null;
    }


}
