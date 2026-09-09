package it.unicam.cs.mpgc.rpg123015;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
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
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Personaggio> initiativeOrder = new ArrayList<>();
    private final Personaggio[][] arena;

    public Livello(Avatar h) {

        id = this.hashCode();

        diff = genDiff(LevelDifficulty.class);
        genArena(diff);

        arena = new Personaggio[lArena][hArena];
        scena = new ScenaLivello(id, lArena, hArena);

        scena.genArena(lArena, hArena);
        hero = h;
        hero.scena = scena;
        hero.ShareStats();
        initiativeOrder.add(hero);
        initiativeOrder.addAll(enemies);


        //Eseguire la funzione spawn di tutti gli elementi in initiative, aggiungerli ad arena[][] e aggiungere un'icona a scenalivello
        for (Personaggio e : initiativeOrder) {
            SpawnEntity(e);
        }

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

                enemies.add(new Enemy(diff, 0));
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

    public void GameStart() {
        System.out.println("GAME START!");
        initController();
    }

    public void initController()
    {
        AtomicBoolean focusTarget = new AtomicBoolean(false);
        getScenaLivello().setOnKeyPressed(event -> {

            XYVector targetPos;
            Action heroAction;

            if(hero.getActionsPerTurn()>0) //Se il giocatre ha fatto tutte le sue azioni, non può più usare comandi finchè non torna il suo turno
            {

                if(!focusTarget.get())
                {
                    switch(event.getCode())
                    {
                        case W:
                            if (hero.position.getY() == 0)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX(), hero.position.getY()-1);
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            break;
                        case A:
                            if (hero.position.getX() == 0)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX()-1, hero.position.getY());
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            break;
                        case S:
                            if (hero.position.getY() == hArena-1)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX(), hero.position.getY()+1);
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            break;
                        case D:
                            if (hero.position.getX() == lArena-1)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX()+1, hero.position.getY());
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
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
                            if (hero.target.position.getY() == 0)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET UP");
                            scena.MoveAux(hero.target, hero.target.getX(), hero.target.getY()-1 );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX(), hero.target.getY()-1));
                            break;
                        case A:
                            if (hero.target.position.getX() == 0)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET LEFT");
                            scena.MoveAux(hero.target, hero.target.getX()-1, hero.target.getY() );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX()-1, hero.target.getY()));
                            break;
                        case S:
                            if (hero.target.position.getY() == hArena-1)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET DOWN");
                            scena.MoveAux(hero.target, hero.target.getX(), hero.target.getY()+1 );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX(), hero.target.getY()+1));
                            break;
                        case D:
                            if (hero.target.position.getX() == lArena-1)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET RIGHT");
                            scena.MoveAux(hero.target, hero.target.getX()+1, hero.target.getY() );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX()+1, hero.target.getY()));
                            break;
                        case ENTER:
                        case Z:
                            System.out.println("NOW FOCUSING PLAYER");
                            Action azioneEroe = new Action(hero.target.position, TypeOfAction.ATTACK, hero.getSTR(), hero.getAttackAnim1(), hero.name);
                            scena.RemoveAux(hero.target);
                            hero.decAPT();
                            ExecuteTurn(azioneEroe);
                            focusTarget.set(false);
                            break;
                        case ESCAPE:
                            scena.ReturnToMM();
                            break;
                    }
                }
            }

        });
    }

    synchronized private void DoEnemiesTurn()
    {
        for (Enemy e : enemies)
        {
            e.setActionsPerTurn(e.getDEX());
            while(e.getActionsPerTurn()>0)
            {
                Action a = e.TakeAction(arena);
                System.out.println(a.getTypeOfAction());
                ExecuteTurn(a);
                e.decAPT();
            }
        }
        hero.setActionsPerTurn(hero.getDEX());
        System.out.println("Qua hanno finito i nemici, ora tornerebbero i controlli al player. APT:" + hero.getActionsPerTurn());
    }

    //idealmente, questa funzione viene chiamata una (o più) volta per turno, ogni turno, ogni volta che un elemento fa qualcosa in gameStart.
    private void ExecuteTurn(Action azione)
    {
        if(azione.getTypeOfAction().equals(TypeOfAction.MOVEMENT) &&  arena[azione.getXYVector().getX()][azione.getXYVector().getY()] != null)
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
            scena.MoveEntity(actor.getIcon(),actor.name, azione.getXYVector().getX(), azione.getXYVector().getY());
            System.out.println(azione.getMasterID() + " spostato da " + a + "," + b + " a " + azione.getXYVector().getX() + "," + azione.getXYVector().getY() );

        }
        else if (azione.getTypeOfAction().equals(TypeOfAction.ATTACK))
        {
            actor =  arena[azione.getXYVector().getX()][azione.getXYVector().getY()];
            if(actor != null)
            {
                scena.SpawnAux(azione.getAttackAnimation(), actor.getX(),  actor.getY());
                System.out.println(azione.getDMG());
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
                            Victory();

                    }
                    else
                    {

                        GameOver();

                    }

                }

            }
        } else if (azione.getTypeOfAction().equals(TypeOfAction.WAIT)) {
            {
                System.out.println("Boh qua non fa niente");
            }

        }

        if(azione.getMasterID().equals(hero.name) && hero.getActionsPerTurn() == 0)
            DoEnemiesTurn();
    }

    private void GameOver() {
        scena.ReturnToMM();
    }

    private void Victory(){
        hero.addVittoria();

        Button btnNewLvl = new Button("Prossimo Livello");
        btnNewLvl.setAlignment(Pos.CENTER);
        btnNewLvl.setStyle("-fx-font-size:24;");

        Button btnBackToMenu = new Button("Torna al Menù");
        btnBackToMenu.setAlignment(Pos.CENTER);
        btnBackToMenu.setStyle("-fx-font-size:24;");

        Button btnQuit = new Button("Esci dal Gioco");
        btnQuit.setAlignment(Pos.CENTER);
        btnQuit.setStyle("-fx-font-size:24;");

        Insets insets = new Insets(20, 20, 60, 20);
        FlowPane victoryPane = new FlowPane(Orientation.VERTICAL);
        victoryPane.setAlignment((Pos.CENTER));
        victoryPane.setPadding(insets);
        victoryPane.setVgap(16);
        victoryPane.setPrefWrapLength(494);
        victoryPane.setId("VictoryPane");

        btnNewLvl.setOnAction(event -> {
            Livello l = new Livello(hero);
            Stage x = (Stage) btnNewLvl.getScene().getWindow();
            x.requestFocus();
            x.setScene(l.getScenaLivello());
            l.GameStart();
        });

        btnBackToMenu.setOnAction(e -> {
            scena.ReturnToMM();
        });

        btnQuit.setOnAction(e -> {
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        victoryPane.getChildren().add(btnNewLvl);
        victoryPane.getChildren().add(btnBackToMenu);
        victoryPane.getChildren().add(btnQuit);

        scena.getStackPane().getChildren().add(victoryPane);
    }
}
