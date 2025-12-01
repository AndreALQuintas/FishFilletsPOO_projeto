package pt.iscte.poo.utils;

import objects.GameObject;

public interface PushAction {
    public void getPushedAction(GameObject other, Vector2D dir);
}
