package it.unicam.cs.mpgc.rpg123015;

public class Action {
    private String masterID;
    private XYVector targetPosition;
    private TypeOfAction type;

    public Action(XYVector targetPosition, TypeOfAction type, String idperformer) {
        setMasterID(idperformer);
        setXYVector(targetPosition);
        setTypeOfAction(type);
    }

    private int dmg;
    public Action(XYVector targetPosition, TypeOfAction type , int dmg, String idperformer) {
        setMasterID(idperformer);
        setXYVector(targetPosition);
        setTypeOfAction(type);
        setDmg(dmg);
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
}
