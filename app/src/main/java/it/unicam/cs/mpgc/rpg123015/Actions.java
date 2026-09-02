package it.unicam.cs.mpgc.rpg123015;

public interface Actions {

    public void Spawn(int x, int y);

    public void Move(XYVector movement);

    public void UpdatePosition(XYVector position);

    public void RegHit(int dmg);

    public Action TakeAction();
}
