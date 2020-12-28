package com.game;

import io.reactivex.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Game extends JPanel {
    List<Character> dinos;
    Trail mainTrail;

    KeyHandler keyHandler;

    Color green;
    Color red;
    Color yellow;
    Color blue;

    public Game() {
        super();
        mainTrail = new Trail(50, 160);

        initColors();
        initCharacters();

        //repainting in 60FPS
        Observable.interval(17, TimeUnit.MILLISECONDS).subscribe(i -> {
            repaint();
            detectCollision();
        });

        //action every 100 ms
        Observable.interval(100, TimeUnit.MILLISECONDS).subscribe(i -> addStainsToTrail());


        //usuwanie postaci po sekundzie animacji smierci
        Observable.interval(17, TimeUnit.MILLISECONDS).subscribe(i -> {
            List<Character> list = new ArrayList<>(dinos);
            Observable.fromIterable(list).filter(d -> d.isDead() && d.getDeadFrames()<60).subscribe(d -> d.incrementDeadFrames());
            Observable.fromIterable(list).filter(d -> d.isDead() && d.getDeadFrames()>=60).subscribe(d -> dinos.remove(d));
        });

        initKeyHandler();

        addKeyListener(keyHandler);
        setFocusable(true);
    }

    public void initColors() {
        green = new Color(159, 188, 77);
        red = new Color(188, 77, 79);
        yellow = new Color(253, 199, 96);
        blue = new Color(77, 146, 188);
    }

    public void initCharacters() {
        dinos = new ArrayList<>();
        dinos.add(new Character(getImgList("Green"), getDeadImgList("Green"), 300, 200, 7, 0, 5, 1920, 1020, 96, green));
        dinos.add(new Character(getImgList("Red"), getDeadImgList("Red"), 300, 750, 7, 0, 5, 1920, 1020, 96, red));
        dinos.add(new Character(getImgList("Yellow"), getDeadImgList("Yellow"), 1500, 200, 7, 0, 5, 1920, 1020, 96, yellow));
        dinos.add(new Character(getImgList("Blue"), getDeadImgList("Blue"), 1500, 750, 7, 0, 5, 1920, 1020, 96, blue));
    }

    public void initKeyHandler() {
        keyHandler = new KeyHandler();
        keyHandler.addKeyBinding(dinos.get(0), KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
        keyHandler.addKeyBinding(dinos.get(1), KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E);
        keyHandler.addKeyBinding(dinos.get(2), KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P);
        keyHandler.addKeyBinding(dinos.get(3), KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C);
    }


    public List<Image> getImgList(String color) {
        List<Image> animation = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            animation.add(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Michael\\Documents\\GitHub\\Paint-o-saurs\\Att1\\src\\main\\resources\\Dinos\\"+ color +"\\walk"+ i +".png"));
        }
        return animation;
    }

    public List<Image> getDeadImgList(String color) {
        List<Image> animation = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            animation.add(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Michael\\Documents\\GitHub\\Paint-o-saurs\\Att1\\src\\main\\resources\\Dinos\\"+ color +"\\dead"+ i +".png"));
        }
        return animation;
    }

    public void addStainsToTrail() {
        List<Character> list = new ArrayList<>(dinos);
        Observable.fromIterable(list).filter(d -> !d.isJumping()).subscribe(d -> mainTrail.addPaintSatin(d.getPaintStain()));
        mainTrail.trimList();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTrail(g);
        drawCharacters(g);

        updateCharactersPosition();
    }

    public void drawTrail(Graphics g) {
        mainTrail.draw(g);
    }

    public void drawCharacters(Graphics g) {
        List<Character> list = new ArrayList<>(dinos);
        Observable.fromIterable(list).subscribe(d -> d.draw(g));
    }

    public void updateCharactersPosition() {
        List<Character> list = new ArrayList<>(dinos);
        Observable.fromIterable(list).filter(d -> !d.isDead()).subscribe(d -> d.updatePosition());
    }

    public void detectCollision() {
        List<Character> list = new ArrayList<>(dinos);
        Observable.fromIterable(list).filter(d -> !d.isJumping()).subscribe(d -> mainTrail.collisionCheck(d));
    }
}