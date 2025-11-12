package pt.iscte.poo.utils;

import java.util.ArrayList;
import java.util.List;

public interface Tags {
    
    public List<String> getTagList();
    
    // Default method implementations
    default boolean hasTag(String tag) {
        return getTagList().contains(tag);
    }
    
    default void addTag(String tag) {
        if (!hasTag(tag)) {
            getTagList().add(tag);
        }
    }
    
    default void removeTag(String tag) {
        getTagList().remove(tag);
    }
    
    default void clearTags() {
        getTagList().clear();
    }
}