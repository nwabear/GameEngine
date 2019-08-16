package com.nwabear.raycasting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Frame extends JFrame {

    private Surface surface;

    private ArrayList<Actions> actions = new ArrayList<>();

    public Frame() { this.initUI(); }

    private void initUI() {
        this.surface = new Surface(this);

        Thread gc = new Thread(new GameClock(this.surface));

        this.add(this.surface);

        this.setTitle("DOOT");

        int width = AppContext.WIDTH;
        int height = AppContext.LENGTH;
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gc.start();

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed( KeyEvent keyEvent ) {
                Actions toAdd = Actions.NONE;
                switch(keyEvent.getKeyCode()) {
                    case 38: //up
                        toAdd = Actions.UP;
                        break;

                    case 40: //down
                        toAdd = Actions.DOWN;
                        break;

                    case 37: //left
                        toAdd = Actions.LEFT;
                        break;

                    case 39: //right
                        toAdd = Actions.RIGHT;
                        break;
                }

                if(!Frame.this.actions.contains(toAdd) && toAdd != Actions.NONE) {
                    Frame.this.actions.add(toAdd);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                switch(keyEvent.getKeyCode()) {
                    case 38: //up
                        Frame.this.actions.remove(Actions.UP);
                        break;

                    case 40: //down
                        Frame.this.actions.remove(Actions.DOWN);
                        break;

                    case 37: //left
                        Frame.this.actions.remove(Actions.LEFT);
                        break;

                    case 39: //right
                        Frame.this.actions.remove(Actions.RIGHT);
                        break;
                }
            }
        } );
    }

    public static void main(String[] args) {
        Frame display = new Frame();
        EventQueue.invokeLater( () -> {
            display.setVisible( true );
        } );
    }

    public ArrayList<Actions> getActions() {
        return this.actions;
    }
}