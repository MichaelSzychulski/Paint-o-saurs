package com.game;

import java.util.HashMap;
import java.util.Map;

public class KeyBinding {
    private Map<Integer, Integer> mapping;
    private Character dino;

    public KeyBinding(Character dino, int left, int up, int right) {
        this.dino = dino;
        mapping = new HashMap<>();
        mapping.put(left, -1);
        mapping.put(up, 0);
        mapping.put(right, 1);
    }

    public Character getDino(){
        return dino;
    }

    public boolean isKeyBinded(int e){
        return (mapping.containsKey(e));
    }

    public int getKeyValue(int e){
        return mapping.get(e);
    }
}
