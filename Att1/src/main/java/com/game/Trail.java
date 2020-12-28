package com.game;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Trail {
    private List<PaintStain> stains = new ArrayList<>();
    private int radius;
    private int maxSize;
    ObservableEmitter<Object> collisionCheckEmitter;

    public Trail(int radius, int maxSize) {
        this.radius = radius;
        this.maxSize = maxSize;

        Observable.create(emitter -> this.collisionCheckEmitter = emitter).subscribe(d -> {
            Character dino = (Character) d;
            List<PaintStain> list = new ArrayList<>(stains);
           Observable.fromIterable(list)
                   .filter(s -> (s.getOwner() != dino))
                   .any(s -> s.getEllipse().intersects(dino.getDinoX() + dino.getDinoSize()/3, dino.getDinoY() + (dino.getDinoSize()/2), dino.getDinoSize()/2, dino.getDinoSize()/2))
                   .filter(b -> b)
                   .subscribe(s -> {
                       dino.setDead(true);
                   });
        });
    }

    /*
    sets Ellipse2D for given PaintStain and adds it on beggining of the list.
    It is done to leave newest PaintStains when trimming list
     */
    public void addPaintSatin(PaintStain stain){
        stain.setEllipse(new Ellipse2D.Double(stain.getX(), stain.getY(), radius, radius));
        stains.add(0, stain);
    }

    /*
    trims list of PaintStains to given number
     */
    public void trimList(){
        stains = stains.stream().limit(maxSize).collect(Collectors.toList());
    }


    public void collisionCheck(Character dino){
        collisionCheckEmitter.onNext(dino);
    }

    /*
    Copy list of stains and reverse it's order for newer stains to cover older stains
     */
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        List<PaintStain> list = new ArrayList<>(stains);
        Collections.reverse(list);
        Observable.fromIterable(list).subscribe(s -> {
            g2.setColor(s.getColor());
            g2.fill(s.getEllipse());
        });
    }

}
