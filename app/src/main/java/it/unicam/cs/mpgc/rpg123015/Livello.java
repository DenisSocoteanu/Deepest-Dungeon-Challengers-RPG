package it.unicam.cs.mpgc.rpg123015;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Livello {

    private String nomeLvl;
    private LevelStatus status;
    private LevelDifficulty diff;
    private ScenaLivello scena;
    private Avatar hero;

    private int lArena,hArena;
    private final List<Enemy> enemies = new ArrayList<>();
    private List<Personaggio> initiativeOrder = new ArrayList<>();
    private final Personaggio[][] arena;

    public Livello(Avatar h) {

        nomeLvl = "l-" + h.name + h.getVittorie();

        diff = genDiff(LevelDifficulty.class);
        genArena(diff);

        arena = new Personaggio[lArena][hArena];
        scena = new ScenaLivello(h.name, h.getVittorie(), lArena, hArena);

        scena.genArena(lArena, hArena);
        hero = h;
        hero.scena = scena;
        hero.ShareStats();

        scena.logAction("Vedi " + enemies.size() + " nemici!");
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

    //SpawnEntity assegna a ogni personaggio la sua posizione nell'arena, poi invoca SummonEntity
    private void SpawnEntity(Personaggio e) {
        Random RANDOM = new Random();

        int x,y;
        boolean disp;

        do {
            disp = false;
            x = RANDOM.nextInt(0, lArena);
            y = RANDOM.nextInt(0, hArena);
            if (arena[x][y] == null)
            {
                disp = true;
                e.Spawn(x, y);
                SummonEntity(e);
            }
        } while (!disp);
    }

    //Avendo la posizione nell'arena, ogni personaggio viene posizionato
    private void SummonEntity(Personaggio e) {
        System.out.println("Spawnato " + e.getName() + " in posizione " + e.getX() + ", " + e.getY());
        arena[e.getX()][e.getY()] = e;
        scena.SpawnEntity(e.getIcon(), e.getName(), e.getX(), e.getY());
    }

    public void GameStart() {
        System.out.println("GAME START!");
        hero.setActionsPerTurn(hero.getDEX());
        initController();
    }

    public void initController()
    {
        AtomicBoolean focusTarget = new AtomicBoolean(false);
        getScenaLivello().setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case ESCAPE:
                    PauseMenu();
            }

            XYVector targetPos;
            Action heroAction;

            if(hero.getActionsPerTurn()>0) //Se il giocatre ha fatto tutte le sue azioni, non può più usare comandi finchè non torna il suo turno
            {

                if(!focusTarget.get())
                {
                    switch(event.getCode())
                    {
                        case UP:
                        case W:
                            if (hero.position.getY() == 0)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX(), hero.position.getY()-1);
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            scena.logAction(hero.name + " spostato in " + targetPos.PrintXY());
                            break;
                        case LEFT:
                        case A:
                            if (hero.position.getX() == 0)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX()-1, hero.position.getY());
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            scena.logAction(hero.name + " spostato in " + targetPos.PrintXY());
                            break;
                        case DOWN:
                        case S:
                            if (hero.position.getY() == hArena-1)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX(), hero.position.getY()+1);
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            scena.logAction(hero.name + " spostato in " + targetPos.PrintXY());
                            break;
                        case RIGHT:
                        case D:
                            if (hero.position.getX() == lArena-1)
                            {scena.PlayOOBsound();break;}

                            targetPos = new  XYVector(hero.position.getX()+1, hero.position.getY());
                            heroAction = new Action(targetPos, TypeOfAction.MOVEMENT, hero.name);
                            hero.decAPT();
                            ExecuteTurn(heroAction);
                            scena.logAction(hero.name + " spostato in " + targetPos.PrintXY());
                            break;
                        case Z:
                            System.out.println("NOW FOCUSING TARGET");
                            hero.SelectTarget();
                            focusTarget.set(true);
                            break;
                    }
                }
                else if(focusTarget.get())
                {
                    switch(event.getCode())
                    {
                        case UP:
                        case W:
                            if (hero.target.position.getY() == 0)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET UP");
                            scena.MoveAux(hero.target, hero.target.getX(), hero.target.getY()-1 );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX(), hero.target.getY()-1));
                            break;
                        case LEFT:
                        case A:
                            if (hero.target.position.getX() == 0)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET LEFT");
                            scena.MoveAux(hero.target, hero.target.getX()-1, hero.target.getY() );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX()-1, hero.target.getY()));
                            break;
                        case DOWN:
                        case S:
                            if (hero.target.position.getY() == hArena-1)
                            {scena.PlayOOBsound();break;}

                            System.out.println("TARGET DOWN");
                            scena.MoveAux(hero.target, hero.target.getX(), hero.target.getY()+1 );
                            hero.target.UpdatePosition(new XYVector(hero.target.getX(), hero.target.getY()+1));
                            break;
                        case RIGHT:
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
        scena.logAction("I nemici incombono!");
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
                scena.logAction("Colpito " + actor.getName() + "! " + azione.getDMG() + " danni");
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

    private int PauseMenu()
    {
        StackPane s = scena.getStackPane();
        if (s.getChildren().getLast() instanceof VBox) {
            return -1;
        }

        VBox pauseM = new VBox(24);
        pauseM.setMinHeight(s.getHeight()); pauseM.setPrefHeight(s.getHeight());
        pauseM.setMinWidth(s.getWidth()); pauseM.setPrefWidth(s.getWidth());
        pauseM.setStyle("-fx-background-color: rgba(14,14,14,0.7);");

        ImageView titleCard = new ImageView(new Image("/icons/TitleCard.png"));
        Text subTitle = new Text("Discesa di "+ hero.name + " | B-"+hero.getVittorie());
        subTitle.setFont(Font.font("Power Red and Green", 28));
        subTitle.setFill(Color.WHITE);
        pauseM.setAlignment(Pos.CENTER);

        Button continua = new Button("Continua");
        continua.setFont(Font.font("Power Red and Green", 24));
        continua.setPrefHeight(32);
        continua.setPrefWidth(s.getWidth()/3);
        continua.setAlignment(Pos.CENTER);
        continua.setOnAction(event -> {
            s.getChildren().removeLast();
        });

        Button backtoMM = new Button("Torna al Menu");
        backtoMM.setFont(Font.font("Power Red and Green", 24));
        backtoMM.setPrefHeight(32);
        backtoMM.setPrefWidth(s.getWidth()/3);
        backtoMM.setAlignment(Pos.CENTER);
        backtoMM.setOnAction(e -> {
            scena.ReturnToMM();
        });

        pauseM.getChildren().addAll(titleCard, subTitle, continua, backtoMM);

        s.getChildren().add(pauseM);

        return 1;
    }

    private void GameOver() {

        hero.KILL();
        SaveLoadParser slp = new SaveLoadParser();
        slp.writeSave(hero);StackPane s = scena.getStackPane();

        VBox defeatMenu = new VBox(24);
        defeatMenu.setMinHeight(s.getHeight()); defeatMenu.setPrefHeight(s.getHeight());
        defeatMenu.setMinWidth(s.getWidth()); defeatMenu.setPrefWidth(s.getWidth());
        defeatMenu.setStyle("-fx-background-color: rgba(14,14,14,0.7);");

        ImageView titleCard = new ImageView(new Image("/icons/TitleCard.png"));
        Text subTitle = new Text("Discesa di "+ hero.name + " | B-"+hero.getVittorie());
        subTitle.setFont(Font.font("Power Red and Green", 28));
        subTitle.setFill(Color.WHITE);
        defeatMenu.setAlignment(Pos.CENTER);

        Button backtoMM = new Button("Torna al Menu");
        backtoMM.setFont(Font.font("Power Red and Green", 24));
        backtoMM.setPrefHeight(32);
        backtoMM.setPrefWidth(s.getWidth()/3);
        backtoMM.setAlignment(Pos.CENTER);
        backtoMM.setOnAction(e -> {
            scena.ReturnToMM();
        });

        Button btnQuit = new Button("Esci dal gioco");
        btnQuit.setFont(Font.font("Power Red and Green", 24));
        btnQuit.setPrefHeight(32);
        btnQuit.setPrefWidth(s.getWidth()/3);
        btnQuit.setAlignment(Pos.CENTER);
        btnQuit.setOnAction(e -> {
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        defeatMenu.getChildren().addAll(titleCard, subTitle, backtoMM, btnQuit);
        s.getChildren().add(defeatMenu);
    }

    private void Victory(){
        StackPane s = scena.getStackPane();

        hero.addVittoria();

        Text celebration = new Text("Vittoria!");
        celebration.setFill(Color.WHITE);
        celebration.setFont(Font.font("Power Red and Green", 60));
        celebration.setTextAlignment(TextAlignment.CENTER);
        celebration.setWrappingWidth(s.getWidth()/3);

        Text subText = new Text("Vedi le scale per il prossimo livello in lontananza..");
        subText.setFill(Color.WHITE);
        subText.setFont(Font.font("Power Red and Green", FontPosture.ITALIC, 24));
        subText.setTextAlignment(TextAlignment.CENTER);
        subText.setWrappingWidth(s.getWidth()/3);

        Button btnNewLvl = new Button("Prosegui");
        btnNewLvl.setFont(Font.font("Power Red and Green", 24));
        btnNewLvl.setPrefHeight(32);
        btnNewLvl.setPrefWidth(s.getWidth()/3);
        btnNewLvl.setAlignment(Pos.CENTER);

        Button btnBackToMenu = new Button("Torna al Menù");
        btnBackToMenu.setFont(Font.font("Power Red and Green", 24));
        btnBackToMenu.setPrefHeight(32);
        btnBackToMenu.setPrefWidth(s.getWidth()/3);
        btnBackToMenu.setAlignment(Pos.CENTER);

        Button btnQuit = new Button("Salva ed esci");
        btnQuit.setFont(Font.font("Power Red and Green", 24));
        btnQuit.setPrefHeight(32);
        btnQuit.setPrefWidth(s.getWidth()/3);
        btnQuit.setAlignment(Pos.CENTER);

        btnNewLvl.setOnAction(event -> {
            Livello l = new Livello(hero);
            Stage x = (Stage) btnNewLvl.getScene().getWindow();
            x.requestFocus();
            x.setScene(l.getScenaLivello());
            l.GameStart();
        });

        btnBackToMenu.setOnAction(e -> {
            SaveLoadParser slp = new SaveLoadParser();
            slp.writeSave(hero);
            scena.ReturnToMM();
        });

        btnQuit.setOnAction(e -> {
            SaveLoadParser slp = new SaveLoadParser();
            slp.writeSave(hero);
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        Insets insets = new Insets(20, 20, 60, 20);
        FlowPane victoryPane = new FlowPane(Orientation.VERTICAL);
        victoryPane.setAlignment((Pos.CENTER));
        victoryPane.setPadding(insets);
        victoryPane.setVgap(16);
        victoryPane.setPrefWrapLength(494);
        victoryPane.setId("VictoryPane");
        if(s.getChildren().getLast() instanceof GridPane) {
            victoryPane.setVisible(false);
        }
        victoryPane.getChildren().addAll(celebration,subText,btnNewLvl,btnBackToMenu,btnQuit);

        scena.getStackPane().getChildren().add(victoryPane);
    }

    public Scene getScenaLivello() {
        return scena.getScenaLivello();
    }
}
