package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.image.Image;

public class Auxiliary implements Actions{
    public String name;
    public XYVector position;
    public AuxType auxType;
    private String imageDir;
    private Image image;

    public Auxiliary(String name, String image, AuxType type) {
        this.name = name;
        this.imageDir = image;
        this.image = new Image(imageDir);
        this.auxType = type;
    }

    public Auxiliary(String name, String image, int x, int y, AuxType type) {
        this.name = name;
        this.image = new Image(image);
        this.position = new XYVector(x, y);
        this.auxType = type;
    }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public void setX(int x) { position.setX(x); }
    public void setY(int y) { position.setY(y); }

    public String getImageDir() { return imageDir; }
    public void setImageDir(String imageDir) { this.imageDir = imageDir; }

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
