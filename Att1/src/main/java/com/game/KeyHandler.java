package com.game;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.*;
//TODO - change all to observable
public class KeyHandler extends KeyAdapter {
    List<KeyBinding> mapping;
    ObservableEmitter<Object> keyPressedEmitter;
    ObservableEmitter<Object> keyReleasedEmitter;

    public KeyHandler() {
        this.mapping = new ArrayList<>();

        //keyPressed event handler
        Observable.create(emitter -> this.keyPressedEmitter = emitter).subscribe(e -> {
            Optional<KeyBinding> kb = getKeyBinding(((KeyEvent)e).getKeyCode());
            kb.ifPresent(k -> {
                Character dino = k.getDino();
                int value = k.getKeyValue(((KeyEvent)e).getKeyCode());
                if(value<0) {
                    dino.setLKP(true);
                    dino.setRKP(false);
                }
                else if(value == 0) dino.setJumping(true);
                else if(value > 0) {
                    dino.setRKP(true);
                    dino.setLKP(false);
                }
            });
        });

        //keyReleased event handler
        Observable.create(emitter -> this.keyReleasedEmitter = emitter).subscribe(e -> {
            Optional<KeyBinding> kb = getKeyBinding(((KeyEvent)e).getKeyCode());
            kb.ifPresent(k -> {
                Character dino = k.getDino();
                int value = k.getKeyValue(((KeyEvent)e).getKeyCode());
                if(value<0) dino.setLKP(false);
                else if(value > 0) dino.setRKP(false);
            });
        });
    }

    public void addKeyBinding(Character dino, int left, int up, int right){
        mapping.add(new KeyBinding(dino, left, up, right));
    }

    public Optional<KeyBinding> getKeyBinding(int event){
        return mapping.stream()
                .filter(k -> k.isKeyBinded(event))
                .findFirst();
    }


    public void keyPressed(KeyEvent e){
        keyPressedEmitter.onNext(e);
    }

    public void keyReleased(KeyEvent e){
        keyReleasedEmitter.onNext(e);
    }
}
