package com.game;

import javax.swing.JFrame;
public class MainForGraphics1
{
    public static void main(String[] args)
    {

        JFrame frame = new JFrame();


        frame.setSize(1920,1080);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Game panel = new Game();


        frame.add(panel);

        frame.setVisible(true);


    }

}