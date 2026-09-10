package it.unicam.cs.mpgc.rpg123015;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Avatar extends Personaggio {

    public int exp = 0;
    public int expToLvlUp = 5;
    private int lvl = 1;
    public ProgressBar expBar;
    private int vittorie;

    public Auxiliary target = new Auxiliary("targetSelection","/icons/TargetSelection.gif", AuxType.ALTRO);
    public ScenaLivello scena;

    /* Vecchio costruttore, possibilmente 100% rimuovibile
    public Avatar(String nome, ScenaLivello scena)
    {
        maxHp = 10;
        hp = maxHp;
        setNome(nome);
        setIcon("/icons/AvatarSprite.png");
        setSTR(6);
        setDEX(2);
        setActionsPerTurn(getDEX());
        setAttackAnim1("Avatar_AttAni_1","/icons/QuickSlash.gif");

        this.scena = scena;

        hpbar = new ProgressBar();
        hpbar.setVisible(false);

        expBar = new ProgressBar();
        expBar.setPrefHeight(24);
        expBar.setStyle("-fx-accent: green;");
        vittorie = 0;

    }*/

    public Avatar(String nome)
    {
        setNome(nome);
        setIcon("/icons/AvatarSprite.png");
        setAttackAnim1("Avatar_AttAni_1","/icons/QuickSlash.gif");
        setSTR(6);
        setDEX(2);
        setActionsPerTurn(getDEX());
        maxHp = 10;
        hp = maxHp;

        hpbar = new ProgressBar();
        hpbar.setVisible(false);

        expBar = new ProgressBar();
        expBar.setPrefHeight(24);
        expBar.setStyle("-fx-accent: green;");
        vittorie = 0;
    }

    public int getVittorie()
    {
        return vittorie;
    }
    public void setVittorie(int v){ vittorie = v;}
    public void addVittoria()
    {
        vittorie++;
    }

    public void SelectTarget()
    {
        target.Spawn(getX(), getY());
        scena.SpawnEntity(target.getImage(), target.name, getX(), getY() );
    }

    public void ShareStats() {
        scena.getStats(hp,maxHp,getSTR(),getDEX(), lvl, exp, expToLvlUp, expBar);
        updateExpBar();
    }

    public void RegHit(int dmg)
    {
        super.RegHit(dmg);
        scena.updateHP(dmg);
    }

    public void gainExp(int xp)
    {
        System.out.println("GUADAGNATI EXP: " + xp);
        exp = exp + xp;
        if (exp >= expToLvlUp)
        {
            lvlUp();
        }
        updateExpBar();
    }

    private void updateExpBar()
    {
        double percentile = ((double) exp / expToLvlUp);
        expBar.setProgress(percentile);
        scena.updateEXP(lvl, exp, expToLvlUp);
    }

    private void lvlUp()
    {
        lvl++;
        exp = exp - expToLvlUp;
        expToLvlUp = expToLvlUp + (int) Math.sqrt(expToLvlUp + lvl);
        lvlUpPopUp();
    }

    private void lvlUpPopUp()
    {
        scena.logAction("Sei salito di livello!");

        //Dialog s = new Dialog();
        StackPane s = scena.getStackPane();

        GridPane cardsGrid = new GridPane();
        cardsGrid.setStyle("-fx-background-color: rgba(52, 199, 112, 0.3);");
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth((scena.getScenaLivello().getWidth()-104)/4);
        column1.setPrefWidth((scena.getScenaLivello().getWidth()-104)/4);
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(scena.getScenaLivello().getHeight()/3);
        row1.setPrefHeight(scena.getScenaLivello().getHeight()/3);

        cardsGrid.getColumnConstraints().add(new ColumnConstraints(26,26,26));
        for (int i = 0; i < 4; i++) {
            cardsGrid.getColumnConstraints().add(column1);
        }
        for (int i = 0; i < 3; i++) {
            cardsGrid.getRowConstraints().add(row1);
        }
        cardsGrid.getColumnConstraints().add(new ColumnConstraints(26,26,26));

        GridPane.setMargin(cardsGrid, new Insets(160));
        cardsGrid.setHgap(26);
        cardsGrid.setAlignment(Pos.CENTER);


        VBox STRb= new VBox(12), DEXb =new VBox(12), mhpbox =new VBox(12), healbox =new VBox(12);
        STRb.getChildren().addAll(new Label(" Forza +1"), new ImageView(new Image("/icons/AvatarSprite.png")));
        DEXb.getChildren().addAll(new Label(" Destrezza +1"), new ImageView(new Image("/icons/SpeedBoot.png")));
        mhpbox.getChildren().addAll(new Label("HP massimi +1"), new ImageView(new Image("/icons/Heart64x.png")));
        healbox.getChildren().addAll(new Label("Cura 4 HP"), new ImageView(new Image("/icons/HealHeart.png")));
        STRb.setStyle("-fx-background-color: rgb(201,201,201);-fx-font-size:20;");
        DEXb.setStyle("-fx-background-color: rgb(201,201,201);-fx-font-size:20;");
        mhpbox.setStyle("-fx-background-color: rgb(201,201,201);-fx-font-size:20;");
        healbox.setStyle("-fx-background-color: rgb(201,201,201);-fx-font-size:20;");
        STRb.setAlignment(Pos.CENTER);
        DEXb.setAlignment(Pos.CENTER);
        mhpbox.setAlignment(Pos.CENTER);
        healbox.setAlignment(Pos.CENTER);

        STRb.setOnMouseClicked(event -> {
            AumentaSTR(1);
            scena.getStackPane().getChildren().removeLast();
        });
        DEXb.setOnMouseClicked(event -> {
            AumentaDEX(1);
            scena.getStackPane().getChildren().removeLast();
        });
        mhpbox.setOnMouseClicked(event -> {
            AumentaMaxHP(1);
            scena.getStackPane().getChildren().removeLast();
        });
        healbox.setOnMouseClicked(event -> {
            RegainHP(4);
            scena.getStackPane().getChildren().removeLast();
        });

        Text congarats = new Text("Sei salito di livello!");
        congarats.setStyle("-fx-font-size:48;-fx-font-family:'Power Red and Green';");
        cardsGrid.add(congarats,2,0);
        cardsGrid.add(STRb, 1, 1);
        cardsGrid.add(DEXb, 2, 1);
        cardsGrid.add(mhpbox, 3, 1);
        cardsGrid.add(healbox, 4, 1);

        /*s.getDialogPane().setContent(cardsGrid);
        s.showAndWait();*/
        s.getChildren().add(cardsGrid);
    }

    public void RegainHP(int hp){
        this.hp+=hp;
        if(this.hp>maxHp){
            this.hp=maxHp;
        }
        System.out.println("HP: "+this.hp);
        scena.updateHP(-hp);
        scena.logAction("Ti senti invigorito, recuperi " + hp + " cuori!");
    }

    public void AumentaMaxHP(int hp){
        this.maxHp+=hp;
        System.out.println("hp: "+ this.hp + "/" +this.maxHp);
        scena.updateMaxHP(maxHp);
        scena.logAction("La tua costituzione aumenta di un po'!");
    }

    public void AumentaSTR(int s){
        setSTR(getSTR()+s);
        System.out.println("STR: "+this.getSTR());
        scena.updateSTR(this.getSTR());
        scena.logAction("La tua forza è aumentata!");
    }

    public void AumentaDEX(int s){
        setDEX(getDEX()+s);
        System.out.println("DEX: "+this.getDEX());
        scena.updateDEX(this.getDEX());
        scena.logAction("Sei diventato più agile!");
    }
}
