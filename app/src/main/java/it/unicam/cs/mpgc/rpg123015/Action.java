package it.unicam.cs.mpgc.rpg123015;

public class Action {
    private XYVector targetPosition;
    private TypeOfAction type;

    public Action(XYVector targetPosition, TypeOfAction type) {
        setXYVector(targetPosition);
        setTypeOfAction(type);
    }

    private int dmg;
    public Action(XYVector targetPosition, TypeOfAction type, int dmg) {
        setXYVector(targetPosition);
        setTypeOfAction(type);
        setDmg(dmg);
    }

    public int getDMG() {
        return dmg;
    }
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public XYVector getXYVector() {
        return targetPosition;
    }
    public TypeOfAction getTypeOfAction() {
        return type;
    }

    public void  setXYVector(XYVector targetPosition) {
        this.targetPosition = targetPosition;
    }
    public void  setTypeOfAction(TypeOfAction type) {
        this.type = type;
    }
}
