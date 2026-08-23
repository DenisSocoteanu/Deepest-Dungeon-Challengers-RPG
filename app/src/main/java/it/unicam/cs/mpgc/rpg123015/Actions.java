package it.unicam.cs.mpgc.rpg123015;

public interface Actions {

    public void Spawn(int x, int y);

    public void Move(int x, int y);

    public Action TakeAction();
}
