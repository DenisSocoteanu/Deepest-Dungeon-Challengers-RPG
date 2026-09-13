package it.unicam.cs.mpgc.rpg123015;

public interface Actions {

    public void Spawn(int x, int y);

    public void UpdatePosition(XYVector position);

    public void RegHit(int dmg);

}
