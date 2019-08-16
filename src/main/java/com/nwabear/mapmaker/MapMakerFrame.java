package com.nwabear.mapmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapMakerFrame extends JFrame {
    private MapMakerSurface surface;

    private Point point;

    public MapMakerFrame() throws Exception {
        this.initUI();
    }

    private void initUI() throws Exception {
        this.surface = new MapMakerSurface(this);

        this.add(this.surface);

        this.setTitle("Map Maker");

        int width = 768;
        int height = 768;
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MapMakerFrame.this.point = e.getPoint();
                MapMakerFrame.this.surface.tick();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        } );
    }

    public Point getPoint() {
        return this.point;
    }

    public static void main(String[] args) throws Exception {
        MapMakerFrame mmf = new MapMakerFrame();
        EventQueue.invokeLater( () -> {
            mmf.setVisible( true );
        } );
    }

    public void forceTick() {
        this.surface.tick();
    }

    public void exit() {
        this.dispose();
    }
}
