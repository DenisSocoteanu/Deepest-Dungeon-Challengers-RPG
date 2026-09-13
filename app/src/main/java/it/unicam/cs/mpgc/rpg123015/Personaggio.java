package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

public abstract class Personaggio implements Actions {
    public String name;
    public int maxHp;
    public int hp = maxHp;
    private int STR, DEX;
    public int actionsPerTurn;
    public ProgressBar hpbar;
    private Image icon;
    private Auxiliary AttackAnim1;
    private Media attackSfx;

    //Coordinate
    public XYVector position;

    public Personaggio() {}

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

    public int getActionsPerTurn() {return actionsPerTurn;}
    public void setActionsPerTurn(int mod)
    {
        this.actionsPerTurn = mod;
    }
    public void decAPT()
    {
        setActionsPerTurn(getActionsPerTurn()-1);
    }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public void UpdatePosition(XYVector movement) {
        position.setX(movement.getX());
        position.setY(movement.getY());
    }

    public void Spawn(int x, int y) {
        position = new XYVector(x, y);
    }

    public Image getIcon() {
        return icon;
    }
    public void setIcon(String s) {
        icon = new Image(s);
    }

    public void RegHit(int dmg) {
        hp -= dmg;
    }

    public void setAttackAnim1(String id, String url) {
        AttackAnim1 = new Auxiliary(id, url, AuxType.ANIMATION);
    }
    public Auxiliary getAttackAnim1(){
        return AttackAnim1;
    }

    public void setAttackSfx(String url){
        attackSfx = new Media(new File(url).toURI().toString());
    }
    public Media getAttackSfx()
    {
        return attackSfx;
    }
}
