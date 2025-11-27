package pt.iscte.poo.utils;

import objects.*;
import pt.iscte.poo.game.Room;

public enum ObjectList {
    ANCHOR(Anchor.class, 'A', false),
    BIGFISH(BigFish.class, 'B', true),
    BOMB(Bomb.class, 'b', false),
    CUP(Cup.class, 'C', false),
    HOLEDWALL(HoledWall.class, 'X', false),
    SMALLFISH(SmallFish.class, 'S', true),
    STEELHORIZONTAL(SteelHorizontal.class, 'H', false),
    STEELVERTICAL(SteelVertical.class, 'V', false),
    STONE(Stone.class, 'R', false),
    TRAP(Trap.class, 'T', false),
    TRUNK(Trunk.class, 'Y', false),
    WALL(Wall.class, 'W', false),
    //WATER(Water.class, ' ')
    ;

    private final Class<? extends GameObject> objectClass;
    private final char objectChar;
    private final boolean isSingleton;

    private ObjectList(Class<? extends GameObject> objectClass, char c, boolean isSingleton) {
        this.objectClass = objectClass;
        this.objectChar = c;
        this.isSingleton = isSingleton;
    }

    public static GameObject instantiate(char c, Room r) {
        for (ObjectList obj : ObjectList.values()) {
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
