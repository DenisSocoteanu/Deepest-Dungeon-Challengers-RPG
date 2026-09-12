package it.unicam.cs.mpgc.rpg123015;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Avatar extends Personaggio {

    private boolean isAlive = true;
    public int exp;
    public int expToLvlUp;
    public int lvl;
    public ProgressBar expBar;
    private int vittorie;

    public Image iconaArco = new Image("/icons/ArcherSprite.png");
    public Auxiliary attackAnimation2 = new Auxiliary("attAnimArco", "/icons/QuickPierce.gif", AuxType.ANIMATION);

    public Auxiliary target = new Auxiliary("targetSelection","/icons/TargetSelection.gif", AuxType.ALTRO);
    public ScenaLivello scena;

    //Questo costruttore viene chiamato quando si crea un nuovo personaggio, AKA un nuovo file di salvataggio
    public Avatar(String nome)
    {
        setNome(nome);
        setIcon("/icons/AvatarSprite.png");
        setAttackAnim1("Avatar_AttAni_1","/icons/QuickSlash.gif");
        setAttackSfx("src/main/resources/audio/sfx_Slash.mp3");
        setSTR(6);
        setDEX(2);
        setActionsPerTurn(getDEX());
        maxHp = 10;
        hp = maxHp;

        hpbar = new ProgressBar();
        hpbar.setVisible(false);

        lvl = 1;
        exp = 0;
        expToLvlUp = 5;

        expBar = new ProgressBar();
        expBar.setPrefHeight(24);
        expBar.setStyle("-fx-accent: green;");
        vittorie = 0;
    }

    public Avatar(String nome, String icon, String aa, int str, int dex, int maxhp, int hp, int l, int xp, int exptlu, int v, boolean isA)
    {
        name = nome;
        setIcon(icon);
        setAttackAnim1("Avatar_AttAni_1",aa);
        setAttackSfx("src/main/resources/audio/sfx_Slash.mp3");
        setSTR(str);
        setDEX(dex);
        setActionsPerTurn(getDEX());
        maxHp = maxhp;
        this.hp = hp;

        hpbar = new ProgressBar();
        hpbar.setVisible(false);

        lvl = l;
        exp = xp;
        expToLvlUp = exptlu;

        expBar = new ProgressBar();
        expBar.setPrefHeight(24);
        expBar.setStyle("-fx-accent: green;");
        vittorie = v;

        isAlive = isA;
    }

    public boolean getIsAlive() {
        return isAlive;
    }
    public void KILL() {
        isAlive = false;
        setIcon("/icons/AvatarDead.png");
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
        Media sound = new Media(new File("src/main/resources/audio/DDC_level_up.mp3").toURI().toString());
        scena.playSound(sound);
        StackPane s = scena.getStackPane();
        //------ Gradient Block ------
        Stop[] stop = {new Stop(0, Color.color(0.372,0.533,0.518)),
                new Stop(1, Color.color(0.184,0.314,0.372))};

        LinearGradient linear_gradient = new LinearGradient(0, 0,
                0, 1, true, CycleMethod.NO_CYCLE, stop);
        BackgroundFill backgroundFill = new BackgroundFill(linear_gradient, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(backgroundFill);
        //------ Gradient Block - Border Block ------
        BorderStrokeStyle bss = new BorderStrokeStyle(StrokeType.OUTSIDE, StrokeLineJoin.ROUND, StrokeLineCap.ROUND, 0,0, null);
        BorderStroke bs = new BorderStroke(Color.ALICEBLUE, bss, CornerRadii.EMPTY, new BorderWidths(3));
        Border b = new Border(bs);
        //------ Border Block ------

        scena.logAction("Sei salito di livello!");

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
        STRb.setBorder(b);DEXb.setBorder(b);mhpbox.setBorder(b);healbox.setBorder(b);

        Label lf = new Label("Forza +2");lf.setTextFill(Color.ALICEBLUE);
        lf.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 20));
        STRb.getChildren().addAll(lf, new ImageView(new Image("/icons/AvatarSprite.png")));
        Label ld = new Label("Destrezza +1");ld.setTextFill(Color.ALICEBLUE);
        ld.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 20));
        DEXb.getChildren().addAll(ld, new ImageView(new Image("/icons/SpeedBoot.png")));
        Label lm = new Label("HP massimi +1");lm.setTextFill(Color.ALICEBLUE);
        lm.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 20));
        mhpbox.getChildren().addAll(lm, new ImageView(new Image("/icons/Heart64x.png")));
        Label lh = new Label("Cura 4 HP");lh.setTextFill(Color.ALICEBLUE);
        lh.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 20));
        healbox.getChildren().addAll(lh, new ImageView(new Image("/icons/HealHeart.png")));
        STRb.setBorder(b);DEXb.setBorder(b);
        mhpbox.setBorder(b);healbox.setBorder(b);
        STRb.setBackground(bg);DEXb.setBackground(bg);
        mhpbox.setBackground(bg);healbox.setBackground(bg);
        STRb.setAlignment(Pos.CENTER);
        DEXb.setAlignment(Pos.CENTER);
        mhpbox.setAlignment(Pos.CENTER);
        healbox.setAlignment(Pos.CENTER);

        STRb.setOnMouseClicked(event -> {
            AumentaSTR(2);
            closeTab(cardsGrid);
        });
        DEXb.setOnMouseClicked(event -> {
            AumentaDEX(1);
            closeTab(cardsGrid);
        });
        mhpbox.setOnMouseClicked(event -> {
            AumentaMaxHP(1);
            RegainHP(1);
            closeTab(cardsGrid);
        });
        healbox.setOnMouseClicked(event -> {
            RegainHP(4);
            closeTab(cardsGrid);
        });

        Text congarats = new Text("Sei salito di livello!");
        congarats.setFill(Color.ALICEBLUE);
        congarats.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 48));
        cardsGrid.add(congarats,2,0);
        cardsGrid.add(STRb, 1, 1);
        cardsGrid.add(DEXb, 2, 1);
        cardsGrid.add(mhpbox, 3, 1);
        cardsGrid.add(healbox, 4, 1);

        s.getChildren().add(cardsGrid);
    }

    private void closeTab(GridPane cardsGrid)
    {
        StackPane s = scena.getStackPane();
        if (!s.getChildren().getLast().isVisible())
            s.getChildren().getLast().setVisible(true);
        scena.getStackPane().getChildren().remove(cardsGrid);
    }

    public void RegainHP(int hp){
        this.hp+=hp;
        if(this.hp>maxHp){
            this.hp=maxHp;
        }
        scena.updateHP(-hp);
        scena.logAction("Ti senti invigorito, recuperi " + hp + " cuori!");
    }

    public void AumentaMaxHP(int hp){
        this.maxHp+=hp;
        scena.updateMaxHP(maxHp);
        scena.logAction("La tua costituzione aumenta di un po'!");
    }

    public void AumentaSTR(int s){
        setSTR(getSTR()+s);
        scena.updateSTR(this.getSTR());
        scena.logAction("La tua forza è aumentata!");
    }

    public void AumentaDEX(int s){
        setDEX(getDEX()+s);
        scena.updateDEX(this.getDEX());
        scena.logAction("Sei diventato più agile!");
    }
}
