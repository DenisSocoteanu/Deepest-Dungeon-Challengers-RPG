package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.image.Image;

public class Auxiliary implements Actions{
    public String name;
    public XYVector position;
    private Image image;

    public Auxiliary(String name, String image) {
        this.name = name;
        this.image = new Image(image);
    }

    public Auxiliary(String name, String image, int x, int y) {
        this.name = name;
        this.image = new Image(image);
        this.position = new XYVector(x, y);
    }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public void setX(int x) { position.setX(x); }
    public void setY(int y) { position.setY(y); }

    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }

    @Override
    public void Spawn(int x, int y) {
        position = new XYVector(x, y);
    }

    @Override
    public void Move(XYVector movement) {
        System.out.println("QUESTO SI MUOVE");
    }

    @Override
    public Action TakeAction() {
        System.out.println("HA FATTO QUALCOSA");
        return null;
    }

    @Override
    public void UpdatePosition(XYVector position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    @Override
    public void RegHit(int dmg) {

    }
}
