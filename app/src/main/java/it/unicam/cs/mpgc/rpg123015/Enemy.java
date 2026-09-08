package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;

public class Enemy extends Personaggio{

    public int xpOnKill;

    public Enemy(LevelDifficulty diff, int numeroId, XYVector dimArena) {
        name = "e-" + diff.name() + numeroId;

        switch(diff)
        {
            case EASY:
                setIcon("/icons/EASYEnemy_Sprite.png");
                setSTR(1);
                setDEX(1);
                maxHp = 17;
                hp = maxHp;
                xpOnKill = 2;
                break;
            case MEDIUM:
                setIcon("/icons/MEDIUMEnemy_Sprite.png");
                setSTR(1);
                setDEX(2);
                maxHp = 23;
                hp = maxHp;
                xpOnKill = 3;
                break;
            case HARD:
                setIcon("/icons/HARDEnemy_Sprite.png");
                setSTR(2);
                setDEX(2);
                maxHp = 35;
                hp = maxHp;
                xpOnKill = 4;
                break;
            case BOSS:
                setIcon("/icons/BOSSEnemy_Sprite.png");
                setSTR(3);
                setDEX(3);
                maxHp = 53;
                hp = maxHp;
                xpOnKill = 6;
                break;
        }

        setActionsPerTurn(getDEX());
        upperLimits = dimArena;
        hpbar = new ProgressBar();
        hpbar.setPrefSize(64,16);
        hpbar.setStyle("-fx-background-color: black;-fx-accent: red;");
        hpbar.setVisible(true);
    }

    @Override
    public Action TakeAction()
    {
        System.out.println(name + " ha compiuto un'azione!");
        /*A questo metodo viene passata una copia di arena, lo stato attuale della mappa. Il nemico controlla ogni azione valida (movimento o attaccare)
        * e la aggiunge a una piccola lista. L'azione viene presa casualmente da quella lista. return Action a livello che si occupa di chiamare executeTurn()
        */
        return null;
    }

    public void RegHit(int dmg) {
        hp -= dmg;
        System.out.println(name + " HP: " + hp);
        UpdateHPBar();
    }

    public void UpdateHPBar()
    {
        double percentile = ((double) hp /maxHp);
        hpbar.setProgress(percentile);
    }



}
