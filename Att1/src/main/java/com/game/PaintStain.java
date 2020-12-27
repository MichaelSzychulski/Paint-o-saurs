package com.game;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PaintStain {
    private int x;
    private int y;
    private Color color;
    private Character owner;
    private Ellipse2D ellipse;

    public PaintStain(int x, int y, Color color, Character owner) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.owner = owner;
    }

    public Ellipse2D getEllipse() {
        return ellipse;
    }

    public void setEllipse(Ellipse2D ellipse) {
        this.ellipse = ellipse;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public Character getOwner() {
        return owner;
    }
}
