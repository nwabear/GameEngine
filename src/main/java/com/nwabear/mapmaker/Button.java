package com.nwabear.mapmaker;

import java.awt.image.BufferedImage;

public class Button {
    private BufferedImage image;

    private int action;

    public Button(BufferedImage image, int action) {
        this.image = image;
        this.action = action;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
