package it.unicam.cs.mpgc.rpg123015;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Livello {

    private int id;
    private LevelStatus status;
    private LevelDifficulty diff;
    private ScenaLivello scena;
    private Avatar hero;

    private int lArena,hArena;
    private int nEnemies;
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Personaggio> initiativeOrder = new ArrayList<>();
    private Personaggio[][] arena;

    public Livello() {

        id = this.hashCode();

        diff = genDiff(LevelDifficulty.class);
        genArena(diff);

        arena = new Personaggio[lArena][hArena];
        scena = new ScenaLivello(id);

        hero = new Avatar("Guglielmo", scena, new XYVector(lArena,hArena));
        hero.ShareStats();
        scena.genArena(lArena, hArena);
        initiativeOrder.add(hero);
        initiativeOrder.addAll(enemies);

        //Eseguire la funzione spawn di tutti gli elementi in initiative, aggiungerli ad arena[][] e aggiungere un'icona a scenalivello
        for (Personaggio e : initiativeOrder) {
            SpawnEntity(e);
        }

    }

    public void GameStart() {

        System.out.println("GAME START!");

        Action azione;
        initController();

    }

    private void SpawnEntity(Personaggio e) {
        Random RANDOM = new Random();

        int x,y;
        boolean disp = false;

        do {
            disp = false;
            x = RANDOM.nextInt(0, lArena);
            y = RANDOM.nextInt(0, hArena);
            if (arena[x][y] == null)
            {
                disp = true;
                System.out.println("Spawnato " + e.getName() + " in posizione " + x + ", " + y);
                e.Spawn(x, y);
                arena[x][y] = e;
                scena.SpawnEntity(e.getIcon(), e.getName(),x,y);
            }
        } while (!disp);

    }

    //Genera casualmente un valore dell'enum LevelDifficulty
    private static <T extends Enum<LevelDifficulty>> T genDiff(Class<T> clazz) {

        Random RANDOM = new Random();
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private void genArena(LevelDifficulty diff) {
        System.out.println("Generating " + diff + " Arena..." );

        Random RANDOM = new Random();
        nEnemies = 0;

        //In base alla DIFFICOLTA' del livello, verrà generata un'arena con le adeguate prioprietà.
        switch (diff){
            case EASY:
                nEnemies = RANDOM.nextInt(1,4);
                lArena = RANDOM.nextInt(5,7);
                hArena = RANDOM.nextInt(5,7);

                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(diff, i, new XYVector(lArena, hArena)));
                }
                break;
            case MEDIUM:
                //Generati il numero di nemici ed i nemici
                nEnemies = RANDOM.nextInt(1,9);
                lArena = RANDOM.nextInt(6,9);
                hArena = RANDOM.nextInt(6,9);

                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(diff, i,  new XYVector(lArena, hArena)));
                }
                break;
            case HARD:
                //Generati il numero di nemici ed i nemici
                nEnemies = RANDOM.nextInt(1,13);
                lArena = RANDOM.nextInt(7,11);
                hArena = RANDOM.nextInt(7,11);

                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(diff, i,  new XYVector(lArena, hArena)));
                }
                break;
            case BOSS:
                //Generati il numero di nemici, 1 boss ed il resto dei nemici
                nEnemies = RANDOM.nextInt(1,9);
                lArena = RANDOM.nextInt(8,13);
                hArena = RANDOM.nextInt(8,13);

                enemies.add(new Enemy(diff, 0, new XYVector(lArena, hArena)));
                for(int i = 0; i < nEnemies; i++){
                    enemies.add(new Enemy(LevelDifficulty.EASY, i, new XYVector(lArena, hArena)));
                }
                break;
        }

        for (Enemy enemy : enemies) {
            System.out.println(enemy.getName() + " | HP: " + enemy.hp);
        }

    }

    public Scene getScenaLivello() {
        return scena.getScenaLivello();
    }

    public void initController()
    {

        AtomicBoolean focusTarget = new AtomicBoolean(false);
        getScenaLivello().setOnKeyPressed(event -> {

            XYVector targetPos;
            Action heroAction;

            if(!focusTarget.get())
            {
                switch(event.getCode())
                {
                    case W:
                        if (hero.position.getY() == 0)
                        {scena.PlayOOBsound();break;}

                        targetPos = new  XYVector(hero.position.getX(), hero.position.getY()-1);
                        heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                        ExecuteTurn(heroAction);
                        break;
                    case A:
                        if (hero.position.getX() == 0)
                        {scena.PlayOOBsound();break;}

                        targetPos = new  XYVector(hero.position.getX()-1, hero.position.getY());
                        heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                        ExecuteTurn(heroAction);
                        break;
                    case S:
                        if (hero.position.getY() == hArena-1)
                        {scena.PlayOOBsound();break;}

                        targetPos = new  XYVector(hero.position.getX(), hero.position.getY()+1);
                        heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                        ExecuteTurn(heroAction);
                        break;
                    case D:
                        if (hero.position.getX() == lArena-1)
                        {scena.PlayOOBsound();break;}

                        targetPos = new  XYVector(hero.position.getX()+1, hero.position.getY());
                        heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                        ExecuteTurn(heroAction);
                        break;
                    case Z:
                        System.out.println("NOW FOCUSING TARGET");
                        hero.SelectTarget();
                        focusTarget.set(true);
                        break;
                    case ESCAPE:
                        scena.ReturnToMM();
                        break;
                }
            }
            else if(focusTarget.get())
            {
                switch(event.getCode())
                {
                    case W:
                        System.out.println("TARGET UP");
                        break;
                    case A:
                        System.out.println("TARGET LEFT");
                        break;
                    case S:
                        System.out.println("TARGET DOWN");
                        break;
                    case D:
                        System.out.println("TARGET RIGHT");
                        break;
                    case ENTER:
                    case Z:
                        System.out.println("NOW FOCUSING PLAYER");
                        hero.Attack();
                        focusTarget.set(false);
                        break;
                    case ESCAPE:
                        scena.ReturnToMM();
                        break;
                }
            }
        });
    }

    //idealmente, questa funzione viene chiamata una (o più) volta per turno, ogni turno, ogni volta che un elemento fa qualcosa in gameStart.
    private void ExecuteTurn(Action azione)
    {
        if(arena[azione.getXYVector().getX()][azione.getXYVector().getY()] != null)
        {
            azione.setTypeOfAction(TypeOfAction.ATTACK);
            for(Personaggio p : initiativeOrder)
            {
                if (p.getName().equals(azione.getMasterID()))
                    azione.setDmg(p.getSTR());
            }
        }
        Personaggio actor;
        if(azione.getTypeOfAction().equals(TypeOfAction.MOVEMENT))
        {
            //Trova l'attore, rimuovilo, aggiungilo a target location
            int a=0,b=0;
            for (int i = 0; i < lArena; i++)
            {
                for (int j = 0; j < hArena; j++) {
                    if (arena[i][j] != null && arena[i][j].getName().equals(azione.getMasterID()))
                    {
                        a=i;b=j;
                    }
                }
            }
            actor = arena[a][b];
            arena[azione.getXYVector().getX()][azione.getXYVector().getY()] = actor;
            arena[a][b] = null;
            actor.Move(azione.getXYVector());
            System.out.println(azione.getMasterID() + " spostato da " + a + "," + b + " a " + azione.getXYVector().getX() + "," + azione.getXYVector().getY() );

        }
        else if (azione.getTypeOfAction().equals(TypeOfAction.ATTACK))
        {
            actor =  arena[azione.getXYVector().getX()][azione.getXYVector().getY()];
            if(actor != null)
            {
                actor.RegHit(azione.getDMG());
                scena.AttachObj(actor.name, actor.hpbar);
                if(actor.hp <= 0)
                {
                    scena.RemoveEntity(arena[azione.getXYVector().getX()][azione.getXYVector().getY()].getName());
                    arena[azione.getXYVector().getX()][azione.getXYVector().getY()] = null;
                }

            }
        }
    }
}
