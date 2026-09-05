package it.unicam.cs.mpgc.rpg123015;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
        scena = new ScenaLivello(id, lArena, hArena);

        scena.genArena(lArena, hArena);
        hero = new Avatar("Guglielmo", scena, new XYVector(lArena,hArena));
        hero.ShareStats();
        initiativeOrder.add(hero);
        initiativeOrder.addAll(enemies);

        //Eseguire la funzione spawn di tutti gli elementi in initiative, aggiungerli ad arena[][] e aggiungere un'icona a scenalivello
        for (Personaggio e : initiativeOrder) {
            SpawnEntity(e);
        }

    }

    public void GameStart() {

        System.out.println("GAME START!");
        boolean gameOver = false;
        Action eAction;

        synchronized (this)
        {
            for (Personaggio e : initiativeOrder) {
                if (e instanceof Enemy){
                    eAction = e.TakeAction();
                    try {
                        wait(300);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    System.out.println("E' IL TUO TURNO QUA DOVRESTI FARE QUALCOSA MA WAIT 300");
                    try {
                        initController();
                        wait(300);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        }

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

        //In base alla DIFFICOLTA' del livello, verrà generata un'arena con le adeguate prioprietà.
        switch (diff){
            case EASY:
                lArena = RANDOM.nextInt(5,7);
                hArena = RANDOM.nextInt(5,7);
                enemies.addAll(new MobSpawner(lArena,hArena).SpawnEnemies(17));

                break;
            case MEDIUM:
                //Generati il numero di nemici ed i nemici
                lArena = RANDOM.nextInt(6,9);
                hArena = RANDOM.nextInt(6,9);
                enemies.addAll(new MobSpawner(lArena,hArena).SpawnEnemies(37));

                break;
            case HARD:
                //Generati il numero di nemici ed i nemici
                lArena = RANDOM.nextInt(7,11);
                hArena = RANDOM.nextInt(7,11);
                enemies.addAll(new MobSpawner(lArena,hArena).SpawnEnemies(57));

                break;
            case BOSS:
                //Generati il numero di nemici, 1 boss ed il resto dei nemici
                lArena = RANDOM.nextInt(8,13);
                hArena = RANDOM.nextInt(8,13);

                enemies.add(new Enemy(diff, 0, new XYVector(lArena, hArena)));
                enemies.addAll(new MobSpawner(lArena,hArena).SpawnEnemies(35));
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

            /*posso mettere tutti i controlli dentro try/finally. Metto tutto dentro un altro if che controlla se il player
            * ha ancora azioni disponibili. se non ce le ha, gli input non funzionano!
            *try
            {

            }finally {
                System.out.println("QUA PUOI CHIAMARE ENEMY ACTIONSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            }*/
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
                {
                    azione.setDmg(p.getSTR());
                    azione.setAttAnim(p.getAttackAnim1());
                }
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
                /*
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        return null;
                    }
                };
                task.run();
                * Posso usare questo task per cancellare l'aux in qualche modo ma non so come
                */
                scena.SpawnAux(azione.getAttackAnimation(), actor.getX(),  actor.getY());
                scena.RemoveAux(azione.getAttackAnimation());
                actor.RegHit(azione.getDMG());

                //scena.AttachObj(actor.name, actor.hpbar);

                if(actor.hp <= 0)
                {
                    scena.RemoveEntity(arena[azione.getXYVector().getX()][azione.getXYVector().getY()].getName());
                    arena[azione.getXYVector().getX()][azione.getXYVector().getY()] = null;

                    if(actor instanceof Enemy)
                    {
                        hero.gainExp(((Enemy) actor).xpOnKill);
                        enemies.remove(actor);
                        if(enemies.isEmpty())
                            System.out.println("VICTORY!");
                    }
                    else
                    {

                        GameOver();

                    }

                }

            }
        }
    }

    private void GameOver()
    {
        scena.ReturnToMM();
    }
}
