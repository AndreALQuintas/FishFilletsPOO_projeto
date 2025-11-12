package pt.iscte.poo.utils;

import objects.GameObject;

public interface Collidable {
    public boolean doCollision(GameObject other);
}
