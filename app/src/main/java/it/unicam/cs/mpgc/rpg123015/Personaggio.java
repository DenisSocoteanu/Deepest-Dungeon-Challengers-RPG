package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;

import java.util.Random;

public abstract class Personaggio implements Actions {
    public String name;
    public int maxHp = 10;
    public int hp = maxHp;
    private int STR, DEX;
    private Image icon;
    public ProgressBar hpbar;

    //Coordinate
    public XYVector position;
    //Ogni personaggio esiste nei confini dell'arena. Pertanto, ogni personaggio è cosciente delle dimensioni dell'arena. La dimensione massima è chiamata upper-limit
    public XYVector upperLimits;

    public Personaggio() {}

    public Personaggio(String name, int STR, int DEX) {
        this.name = name;
        this.STR = STR;
        this.DEX = DEX;
    }

    public String getName() {
        return name;
    }
    public int getSTR() {
        return STR;
    }
    public int getDEX() {
        return DEX;
    }

    public void setNome(String name) {
        this.name = name;
    }
    public void setSTR(int STR) {
        this.STR = STR;
    }
    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public void setX(int x) { position.setX(x); }
    public void setY(int y) { position.setY(y); }

    public void Spawn(int x, int y) {
        position = new XYVector(x, y);
    }

    public void Move(XYVector movement) {
        System.out.println("Placeholder Movement");
    }

    public Action TakeAction() {
        System.out.println(name + " ha agito!");
        return null;
    }

    public void UpdatePosition(XYVector movement) {
        position.setX(movement.getX());
        position.setY(movement.getY());
    }

    public Image getIcon() {
        return icon;
    }
    public void setIcon(String s) {
        icon = new Image(s);
    }

    public void RegHit(int dmg) {
        hp -= dmg;
        System.out.println(name + " HP: " + hp);
    }



}
