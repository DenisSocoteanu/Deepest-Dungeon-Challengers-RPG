package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.media.Media;

public class Action {
    private String masterID;
    private XYVector targetPosition;
    private TypeOfAction type;

    public Action(XYVector targetPosition, TypeOfAction type, String actor) {
        setMasterID(actor);
        setXYVector(targetPosition);
        setTypeOfAction(type);
    }

    private int dmg;
    private Auxiliary attAnim;
    private Media attSfx;
    public Action(XYVector targetPosition, TypeOfAction type , int dmg, Auxiliary attackAnimation, Media attSfx, String actor) {
        setMasterID(actor);
        setXYVector(targetPosition);
        setTypeOfAction(type);
        setDmg(dmg);
        setAttAnim(attackAnimation);
        setAttSfx(attSfx);
    }

    public Action(TypeOfAction t, String actor) {
        setTypeOfAction(t);
        setMasterID(actor);
    }

    public String getMasterID() {return masterID;}
    public void setMasterID(String masterID) {this.masterID = masterID;}

    public int getDMG() {
        return dmg;
    }
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public XYVector getXYVector() {
        return targetPosition;
    }
    public void  setXYVector(XYVector targetPosition) {
        this.targetPosition = targetPosition;
    }

    public TypeOfAction getTypeOfAction() {
        return type;
    }
    public void  setTypeOfAction(TypeOfAction type) {
        this.type = type;
    }

    public Auxiliary getAttackAnimation(){return attAnim;}
    public void setAttAnim(Auxiliary attAnim){this.attAnim = attAnim;}

    public Media getAttSfx(){return attSfx;}
    public void setAttSfx(Media attSfx)
    {
        this.attSfx = attSfx;
    }
}
