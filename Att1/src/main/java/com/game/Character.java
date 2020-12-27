package com.game;

import io.reactivex.Observable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Character {
    private List<Image> images;
    private List<Image> deadImages;
    private int currentImage = 0;
    private int currentDeadImage = 0;

    private int dinoX, dinoY;
    private int dinoDX, dinoDY;

    private int dinoSpeed;
    private int degree;
    private int degreeChange;

    private int maxWidth, maxHeight;
    private int dinoSize;

    private int pointerX;
    private int pointerY;

    private double sin, cos;

    private boolean RKP = false, LKP = true;

    private boolean isJumping = false;
    private int jumpDegree = 0;
    private int jumpHeight = 0;
    private int jumpDegreeChange = 5;
    private int maxJumpHeight = 50;

    private Color color;

    private boolean isDead = false;
    private int deadFrames = 0;

    public Character(List<Image> images, List<Image> deadImages, int dinoX, int dinoY, int dinoSpeed, int degree, int degreeChange, int maxWidth, int maxHeight, int dinoSize, Color color) {
        this.images = images;
        this.deadImages = deadImages;
        this.dinoX = dinoX;
        this.dinoY = dinoY;
        this.dinoSpeed = dinoSpeed;
        this.degree = degree;
        this.degreeChange = degreeChange;
        this.dinoSize = dinoSize;
        this.maxWidth = maxWidth - dinoSize;
        this.maxHeight = maxHeight - dinoSize;
        this.color = color;
        Observable.interval(100, TimeUnit.MILLISECONDS).subscribe(i -> nextImage());
        //new Timer(100, e -> nextImage()).start();
        updatePositionChange();
    }

    public void updatePosition() {
        updatePositionChange();
        dinoX += dinoDX;
        dinoY += dinoDY;
        if (dinoX >= maxWidth) dinoX = maxWidth;
        if (dinoY >= maxHeight) dinoY = maxHeight;
        if (dinoX <= 0) dinoX = 0;
        if (dinoY <= 0) dinoY = 0;
        if (isJumping) progressJump();
        setPointer();
    }

    private void updatePositionChange() {
        if (RKP) turnRight();
        if (LKP) turnLeft();
        sin = Math.sin(Math.toRadians(degree));
        cos = Math.cos(Math.toRadians(degree));
        dinoDX = (int) (dinoSpeed * cos);
        dinoDY = (int) (dinoSpeed * sin);
    }

    private void turnLeft() {
        degree -= degreeChange;
        degree %= 360;
    }

    private void turnRight() {
        degree += degreeChange;
        degree %= 360;
    }

    private void progressJump() {
        jumpDegree += jumpDegreeChange;
        if (jumpDegree >= 180) {
            jumpDegree = 0;
            jumpHeight = 0;
            isJumping = false;
        } else {
            jumpHeight = (int) (Math.sin(Math.toRadians(jumpDegree)) * maxJumpHeight);
        }
    }

    private void setPointer() {
        int ix = dinoX + (dinoSize / 2), iy = dinoY + (dinoSize / 2);
        pointerX = (int) (ix + cos * (dinoSize / 2));
        pointerY = (int) (iy + sin * (dinoSize / 2));
    }

    public void draw(Graphics g) {
        drawPointer(g);
        int rh = getRealHeight();

        //If moving right - draw without changes. If moving left - flip vertically
        if (!isDead) {
            if (dinoDX < 0) g.drawImage(images.get(currentImage), dinoX + dinoSize, rh, -dinoSize, dinoSize, null);
            else g.drawImage(images.get(currentImage), dinoX, rh, null);
        } else {
            g.drawImage(deadImages.get(currentDeadImage), dinoX, dinoY, null);
        }
    }

    private int getRealHeight() {
        int realHeight = dinoY;
        if (isJumping) realHeight -= jumpHeight;
        return realHeight;
    }

    private void drawPointer(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(pointerX, pointerY, 10, 10);
    }

    public PaintStain getPaintStain() {
        return new PaintStain(dinoX + dinoSize / 2, dinoY + dinoSize / 2, color, this);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getDinoX() {
        return dinoX;
    }

    public int getDinoY() {
        return dinoY;
    }

    public void setRKP(boolean RKP) {
        this.RKP = RKP;
    }

    public void setLKP(boolean LKP) {
        this.LKP = LKP;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public int getDinoSize() {
        return dinoSize;
    }

    public int getDeadFrames() {
        return deadFrames;
    }

    public void incrementDeadFrames() {
        this.deadFrames++;
    }

    public void nextImage() {
        currentImage++;
        if (currentImage >= images.size()) currentImage = 0;
        currentDeadImage = currentImage / 3;
    }

}
