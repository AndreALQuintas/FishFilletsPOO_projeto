package pt.iscte.poo.utils;

import objects.GameObject;

public interface Collidable {
    default boolean doCollision(GameObject other, Vector2D dir) {
        System.out.println(" auto collision with " + other.getName() + ", other.hastag: " + other.getTagList());
        return false;
    }
}
