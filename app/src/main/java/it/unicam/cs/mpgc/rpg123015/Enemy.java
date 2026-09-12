package it.unicam.cs.mpgc.rpg123015;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Personaggio{

    public int xpOnKill;

    public Enemy(LevelDifficulty diff, int diffMod, int numeroId) {
        name = "e-" + diff.name() + numeroId;

        switch(diff)
        {
            case EASY:
                setIcon("/icons/EASYEnemy_Sprite.png");
                setSTR(1);
                setDEX(1);
                maxHp = 16+diffMod;
                hp = maxHp;
                xpOnKill = Math.floorDiv(maxHp,5);
                break;
            case MEDIUM:
                setIcon("/icons/MEDIUMEnemy_Sprite.png");
                setSTR(1);
                setDEX(2);
                maxHp = 23+diffMod;
                hp = maxHp;
                xpOnKill = Math.floorDiv(maxHp,6);
                break;
            case HARD:
                setIcon("/icons/HARDEnemy_Sprite.png");
                setSTR(2);
                setDEX(2);
                maxHp = 34+diffMod;
                hp = maxHp;
                xpOnKill = Math.floorDiv(maxHp,7);
                break;
            case BOSS:
                setIcon("/icons/BOSSEnemy_Sprite.png");
                setSTR(2);
                setDEX(3);
                maxHp = 52+diffMod;
                hp = maxHp;
                xpOnKill = Math.floorDiv(maxHp,6);
                break;
        }

        setAttackAnim1("EASY_AttAni_1","/icons/QuickClaw.gif");
        setAttackSfx("src/main/resources/audio/sfx_Claw.mp3");
        setActionsPerTurn(getDEX());
        hpbar = new ProgressBar();
        hpbar.setPrefSize(64,16);
        hpbar.setStyle("-fx-background-color: black;-fx-accent: red;");
        hpbar.setVisible(true);
    }

    public Action TakeAction(Personaggio[][] arena)
    {
        /*A questo metodo viene passata una copia di arena, lo stato attuale della mappa. Il nemico controlla ogni azione valida (movimento o attaccare)
         * e la aggiunge a una piccola lista. L'azione viene presa casualmente da quella lista. return Action a livello che si occupa di chiamare executeTurn()
         */
        List<Action> availableActions = new ArrayList<>();

        XYVector bersaglio = null;
        for(Personaggio[] p : arena)
            for (Personaggio e : p)
                if (e instanceof Avatar)
                    bersaglio = new XYVector(e.getX(), e.getY());

        double range = CalcAbsoluteDistance(bersaglio, position);
        // System.out.println("Pos di Guglielmo: " + bersaglio.PrintXY() + " | Posizione di " + name + ": " + position.PrintXY() + " | Distanza assoluta: " + range);

        if(range == 1)
        {
            Action a = new Action(bersaglio, TypeOfAction.ATTACK, getSTR(), getAttackAnim1(), getAttackSfx(),getName());
            availableActions.add(a);
        }

        availableActions.addAll(Pathtracking(bersaglio, arena));
        if(availableActions.isEmpty())
        {
            Action WAIT = new Action(TypeOfAction.WAIT, getName());
            availableActions.add(WAIT);
        }


        Random RANDOM = new Random();
        int r = RANDOM.nextInt(0,availableActions.size());
        //System.out.println(availableActions.get(r).getTypeOfAction().toString());

        return availableActions.get(r);
    }

    public void RegHit(int dmg) {
        hp -= dmg;
        UpdateHPBar();
    }

    public void UpdateHPBar()
    {
        double percentile = ((double) hp /maxHp);
        hpbar.setProgress(percentile);
    }

    private double CalcAbsoluteDistance(XYVector bersaglio, XYVector origine)
    {
        double range;
        double bx = bersaglio.getX();
        double by = bersaglio.getY();

        double ox = origine.getX();
        double oy = origine.getY();

        double absX = Math.abs(ox - bx);
        double absY = Math.abs(oy - by);

        range = absX+absY;

        return range;
    }

    private List<Action> Pathtracking(XYVector bersaglio, Personaggio[][] arena)
    {
        List<XYVector> avMoves = new ArrayList<>();

        for (int i = 0; i<arena.length; i++)
        {
            for (int j = 0; j<arena[i].length; j++)
            {
                if (CalcAbsoluteDistance(new XYVector(i, j), position) == 1 && arena[i][j] == null)
                {
                    avMoves.add(new XYVector(i, j));
                }
            }
        }

        // Setaccia la lista di possibili movimenti, prendo le posizioni favorevoli e propone un'azione per muoversi in quella direzione
        List<Action> paths = new ArrayList<>();
        for (XYVector v : avMoves)
        {
            if(CalcAbsoluteDistance(bersaglio, v) <= CalcAbsoluteDistance(bersaglio, position)) // Posizione favorevole, diminuisce la distanza dal bersaglio
            {
                paths.add(new Action(v, TypeOfAction.MOVEMENT, name));
            }
            else // Posizione sfavorevole, incrementa la distanza
            {
                //System.out.println(name + " NON SI MUOVERA' MAI IN " + v.PrintXY());
            }
        }

        //System.out.println("POSSIBLE PATHS: " + paths.size());
        return paths;
    }

}
