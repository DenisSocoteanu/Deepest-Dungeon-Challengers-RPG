package it.unicam.cs.mpgc.rpg123015;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class ScenaLivello {

    private MediaPlayer mp;
    private StackPane rootStackPane = new StackPane();
    private BorderPane pannello = new BorderPane();
    private GridPane pannelloArena;
    private GridPane stats;
    private Scene scenaLivello;


    //Il costruttuore genera immediatamente un'arena in base al livello di difficoltà scelto casualmente.
    public ScenaLivello(String nEroe, int nVittorie, int lArena, int hArena) {
        genScenaLayout(nEroe, nVittorie, lArena, hArena);
    }

    //Genera ed organizza gli elementi grafici della scena, come il numero delle colonne e righe, le celle ed il loro aspetto.
    private void genScenaLayout(String nEroe, int nVittorie, int lArena, int hArena) {
        int leftArea =200, bottomArea = 128;
        int lScena, hScena;
        lScena = leftArea+(lArena*64)+64;
        hScena = (hArena*64)+bottomArea+12;

        rootStackPane.getChildren().add(pannello);
        scenaLivello = new Scene(rootStackPane,lScena,hScena);

        scenaLivello.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fontstyle.css")).toExternalForm());

        //------------ Top Area -----------------------
        Text titoloLivello = new Text("Discesa di " + nEroe + " - Piano B-" + nVittorie);
        pannello.setTop(titoloLivello);

        //------------ Right Area -----------
        stats = new GridPane(0,0);
        stats.setId("statsPane");
        pannello.setRight(stats);


        //----------- Left Area ------------------------
        TextArea actionLog = new TextArea("Action Log");
        actionLog.setFont(Font.font("Power Red and Green", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 16));
        actionLog.appendText("\nSei sceso più a fondo nel dungeon...");
        actionLog.setId("actionLogPane");
        actionLog.setFocusTraversable(false);
        actionLog.setEditable(false);
        actionLog.setWrapText(true);
        actionLog.setPrefWidth(leftArea);
        pannello.setLeft(actionLog);

        //----------- Center Area -------------------------
        pannelloArena = new GridPane(0,0);
        pannelloArena.setFocusTraversable(false);
        GridPane.setValignment(pannelloArena, VPos.CENTER);
        GridPane.setHalignment(pannelloArena, HPos.CENTER);
        BorderPane.setAlignment(pannelloArena, Pos.CENTER);
        pannello.setCenter(pannelloArena);

        //----------- Bottom Area ------------------------
        VBox bottomPane = new VBox();
        bottomPane.setStyle("-fx-background-color: blue;");
        bottomPane.setSpacing(10);
        bottomPane.setPrefHeight(bottomArea);
        pannello.setBottom(bottomPane);

        actionLog.setOnMouseClicked(event -> {
            pannelloArena.requestFocus();
        });
    }


    public Scene getScenaLivello() {
        return scenaLivello;
    }


    public void genArena(int lArena, int hArena){
        pannelloArena = new GridPane(0,0);
        Insets marginiArena = new Insets(0,0,0,0);
        GridPane.setMargin(pannelloArena, marginiArena);
        pannello.setCenter(pannelloArena);

        pannelloArena.setStyle("-fx-background-color: #848484;");

        //Viene popolata l'arena di gioco colonna per colonna, riga per riga
        IniGrid(pannelloArena, lArena, hArena, 64);

        for (int i = 0; i < lArena; i++) {
            for (int j = 0; j < hArena; j++) {

                Pane p = new Pane();
                p.setId("p" + i + "-" + j);
                p.setStyle("-fx-background-image: url('/icons/Cobblestone.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-image-rendering: pixelated;");
                pannelloArena.add(p, i, j);
            }
        }

    }

    private void IniGrid(GridPane griglia, int colonne, int righe, int ris)
    {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(ris);
        column1.setPrefWidth(ris);
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(ris);
        row1.setPrefHeight(ris);

        for (int i = 0; i < colonne; i++) {
            griglia.getColumnConstraints().add(column1);
        }
        for (int i = 0; i < righe; i++) {
            griglia.getRowConstraints().add(row1);
        }

    }

    private Pane FindPaneByChildId(String id)
    {
        for(Node n : pannelloArena.getChildren()) {
            if (n instanceof Pane && !((Pane) n).getChildren().isEmpty())
            {
                if(((Pane) n).getChildren().stream().anyMatch(o -> o.getId().equals(id)))
                {
                    return (Pane) n;
                }
            }
        }

        return null;
    }

    public void SpawnEntity(Image icon, String id, int x, int y) {
        ImageView iv = new ImageView(icon);
        iv.setId(id);
        for(Node n : pannelloArena.getChildren()) {
          if ( n instanceof Pane && n.getId().equals("p" + x + "-" + y))
          {
              ((Pane) n).getChildren().add(iv);
          }
        }

    }

    public void MoveEntity(Image icon, String id, int x, int y)
    {
        ImageView f = new ImageView(icon);
        f.setId(id);

        Pane p = FindPaneByChildId(id);
        assert p != null;
        p.getChildren().remove(0, p.getChildren().size());
        ((Pane) pannelloArena.getChildren().stream()
                .filter(o -> o instanceof Pane && o.getId().equals("p" + x + "-" + y))
                .findFirst().get()).getChildren().add(f);
    }

    public void RemoveEntity(String id)
    {
        Pane p = FindPaneByChildId(id);
        assert p != null;
        p.getChildren().remove(0, p.getChildren().size());
        System.out.println("ELIMINATO l'elemento");
    }

    public void SpawnAux(Auxiliary aux, int x, int y) {

        if(aux.auxType == AuxType.ANIMATION)
        {
            Timeline timeline = new Timeline();

            ImageView iv = new ImageView();
            iv.setId(aux.name);
            Duration totalDelay = Duration.ZERO;

            KeyFrame frame = new KeyFrame(totalDelay, e -> iv.setImage(new Image(aux.getImageDir())));
            timeline.getKeyFrames().add(frame);
            totalDelay = totalDelay.add(new Duration(480));
            timeline.getKeyFrames().add(new KeyFrame(totalDelay, e -> iv.setImage(null)));

            for(Node n : pannelloArena.getChildren()) {
                if ( n instanceof Pane && n.getId().equals("p" + x + "-" + y))
                {
                    ((Pane) n).getChildren().add(iv);
                    timeline.play();
                    timeline.setOnFinished(e -> {pannello.getChildren().remove(iv);});
                }
            }
        }

    }

    //AUX NON SI MUOVE PIU' DI 1 CASELLA FIX IT
    public void MoveAux(Auxiliary aux, int x, int y)
    {
        ImageView f = new ImageView(aux.getImage());
        f.setId(aux.name);

        Pane p = FindPaneByChildId(aux.name);
        assert p != null;
        p.getChildren().removeLast();
        ((Pane) pannelloArena.getChildren().stream()
                .filter(o -> o instanceof Pane && o.getId().equals("p" + x + "-" + y))
                .findFirst().get()).getChildren().add(f);
    }

    public void RemoveAux(Auxiliary aux)
    {
        Pane p = FindPaneByChildId(aux.name);
        assert p != null;

        p.getChildren().removeLast();
        System.out.println("ELIMINATO l'aux");
    }


    public void AttachObj(String id, Node whatever)
    {
        Pane p = FindPaneByChildId(id);
        assert p != null;
        if(!p.getChildren().contains(whatever))
            p.getChildren().add(whatever);
    }


    public void ReturnToMM() {
        MainMenu mm = new MainMenu();
        Stage x = (Stage) pannello.getScene().getWindow();
        x.setScene(mm.mmScene);
    }

    public void PlayOOBsound(){
        Media thompThomp = new Media(new File("src/main/resources/audio/ThompThomp.mp3").toURI().toString());
        mp = new MediaPlayer(thompThomp);
        mp.play();
    }

    public void getStats(int hp, int maxHp, int STR, int DEX, int lvl, int exp, int maxexp, ProgressBar expBar) {

        System.out.println(hp + "/" + maxHp);
        IniGrid(stats, 2, (int) Math.ceil((double) maxHp /2)+1, 32);//Viene popolata l'area di destra con le stat del giocatore

        TextFlow ps = new TextFlow(), pd = new TextFlow();
        ps.setId("strPane");pd.setId("dexPane");

        ps.setTextAlignment(TextAlignment.CENTER);
        pd.setTextAlignment(TextAlignment.CENTER);

        Text ts = new Text(""+STR), td = new Text(""+DEX);
        ts.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 40));
        td.setFont(Font.font("Power Red and Green", FontWeight.NORMAL, 40));
        ts.setFill(Color.WHITE); td.setFill(Color.WHITE);
        ts.setStrokeWidth(2); td.setStrokeWidth(2);
        ts.setStroke(Color.BLACK); td.setStroke(Color.BLACK);
        ps.getChildren().add(ts); pd.getChildren().add(td);

        stats.add(ps, 0, 0);
        stats.add(pd, 1, 0);
        ps.setStyle("-fx-background-image: url('/icons/AvatarSprite.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-image-rendering: pixelated;");
        pd.setStyle("-fx-background-image: url('/icons/SpeedBoot.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-image-rendering: pixelated;");


        for (int i = 0; i < maxHp; i++)
        {
            Pane p = new Pane();ImageView iv;
            if(i<hp)
            {
                iv = new ImageView(new Image("/icons/Heart.png"));
                p.setId("hp"+i);

            }else{
                iv = new ImageView(new Image("/icons/BrokenHeart.png"));
                p.setId("bh"+i);

            }
            iv.setId("hi"+i);
            p.getChildren().add(iv);
            System.out.println((i%2)+ ","+ (int) Math.floor((double) i /2));
            stats.add(p, (i%2), (int) Math.floor((double) i /2)+1 );
        }

        HBox expBox = new HBox(10);

        expBox.setStyle("-fx-background-color:green;");
        expBox.setPadding(new Insets(10,4,10,10));
        expBox.setPrefWidth(scenaLivello.getWidth());
        expBar.setPrefWidth(scenaLivello.getWidth());

        Text lvlText = new Text(""+lvl);
        Text expText = new Text(exp + " / " + maxexp);
        Text legenda = new Text("[W][A][S][D] muoviti [Z] attacco a distanza [ESC] pausa");
        lvlText.setStyle("-fx-font-family:'Power Red and Green';-fx-font-size:24;-fx-fill:#ffffff;");
        expText.setStyle("-fx-font-family:'Power Red and Green';-fx-font-size:24;-fx-fill:#ffffff;");
        legenda.setStyle("-fx-font-family:'Power Red and Green';-fx-font-size:24;-fx-fill:#ffffff;");

        expBox.getChildren().addAll(lvlText, expBar, expText);
        ((Pane)(pannello.getBottom())).getChildren().addAll(expBox,legenda);
        System.out.println(pannello.getBottom().getStyle());
    }

    public void updateEXP(int lvl, int exp, int maxexp)
    {
        ((Text)((HBox)((Pane)pannello.getBottom()).getChildren().getFirst()).getChildren().getFirst()).setText(""+lvl);
        ((Text)((HBox)((Pane)pannello.getBottom()).getChildren().getFirst()).getChildren().getLast()).setText(exp + " / " + maxexp);
    }

    public void updateHP(int mod)
    {
        if (mod>0) // mod > 0 = danni
        {
            List<Node> l = new java.util.ArrayList<>(stats.getChildren().stream().filter(n -> n.getId().contains("hp")).toList());
            Collections.reverse(l);

            int i = 0;
            for ( Node n :  l ) {
                if(i<mod){
                    n.setId(n.getId().replace("hp", "bh"));
                    ((ImageView)((Pane)n).getChildren().getFirst()).setImage(new Image("/icons/BrokenHeart.png"));
                    i++;
                }
                else {
                    break;
                }
            }
        }
        else if (mod<0) //mod < 0 = cura
        {
            int healed = 0;
            for(Node n : stats.getChildren())
            {
                //System.out.println(n.getClass() + " | " + n.getId() + " | " + healed + "/"+ -mod);
                if(n instanceof Pane && n.getId().contains("bh") && healed < -mod)
                {
                    ((ImageView)((Pane) n).getChildren().getFirst()).setImage(new Image("/icons/Heart.png"));
                    n.setId(n.getId().replace("bh", "hp"));
                    healed++;
                }
            }
        }

    }

    public void updateMaxHP(int m)
    {
        m--;
        Pane p = new Pane();
        ImageView iv = new ImageView(new Image("/icons/BrokenHeart.png"));
        iv.setId("hi"+m);
        p.getChildren().add(iv);
        p.setId("bh"+m);
        System.out.println((m%2)+ ","+ (int) Math.floor((double) m /2));
        stats.add(p, (m%2), (int) Math.floor((double) m /2)+1 );
    }

    public void updateSTR(int str)
    {
        Text t = (Text) ((TextFlow)stats.getChildren().get(0)).getChildren().getFirst();
        t.setText(""+str);
        ((TextFlow)stats.getChildren().get(1)).getChildren().set(0, t);
    }

    public void updateDEX(int dex)
    {
        Text t = (Text) ((TextFlow)stats.getChildren().get(1)).getChildren().getFirst();
        t.setText(""+dex);
        ((TextFlow)stats.getChildren().get(1)).getChildren().set(0, t);
    }

    public void logAction(String action)
    {
        ((TextArea)pannello.getLeft()).appendText("\n"+action);
    }

    public StackPane getStackPane() {
        return rootStackPane;
    }
}
