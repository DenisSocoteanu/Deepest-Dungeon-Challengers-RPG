package it.unicam.cs.mpgc.rpg123015;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ScenaLivello {

    private MediaPlayer mp;
    private BorderPane pannello = new BorderPane();
    private GridPane pannelloArena;
    private GridPane stats;
    private Scene scenaLivello;


    //Il costruttuore genera immediatamente un'arena in base al livello di difficoltà scelto casualmente.
    public ScenaLivello(int id, int lArena, int hArena) {
        genScenaLayout(id, lArena, hArena);
    }

    //Genera ed organizza gli elementi grafici della scena, come il numero delle colonne e righe, le celle ed il loro aspetto.
    private void genScenaLayout(int id, int lArena, int hArena) {
        int rightArea =200, bottomArea = 128;
        int lScena, hScena;
        lScena = rightArea+(lArena*64)+64;
        hScena = (hArena*64)+bottomArea;
        scenaLivello = new Scene(pannello, lScena, hScena);
        scenaLivello.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fontstyle.css")).toExternalForm());

        Text titoloLivello = new Text("Livello " + id);
        pannello.setTop(titoloLivello);

        stats = new GridPane(0,0);
        pannello.setRight(stats);


        //----------- Left Area ------------------------
        TextArea actionLog = new TextArea("Action Log");
        actionLog.setFocusTraversable(false);
        actionLog.setEditable(false);
        actionLog.setWrapText(true);
        actionLog.setPrefWidth(rightArea);
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
        ImageView iv = new ImageView(aux.getImage());
        iv.setId(aux.name);
        for(Node n : pannelloArena.getChildren()) {
            if ( n instanceof Pane && n.getId().equals("p" + x + "-" + y))
            {
                ((Pane) n).getChildren().add(iv);
            }
        }
    }

    public void MoveAux(Auxiliary aux, int x, int y)
    {
        ImageView f = new ImageView(aux.getImage());
        f.setId(aux.name);

        Pane p = FindPaneByChildId(aux.name);
        assert p != null;
        p.getChildren().remove(0, p.getChildren().size());
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

    public void getStats(int maxHp, int STR, int DEX, int lvl, int exp, int maxexp, ProgressBar expBar) {

        IniGrid(stats, 2, (int) Math.ceil((double) maxHp /2), 32);//Viene popolata l'area di destra con le stat del giocatore
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < (int) Math.ceil((double) maxHp /2); j++) {

                Pane p = new Pane();
                p.setStyle("-fx-background-image: url('/icons/Heart.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-image-rendering: pixelated;");
                stats.add(p, i, j);
            }
        }

        stats.add(new Text("STR " + STR), 0, 5);
        stats.add(new Text("DEX " + DEX), 1, 5);

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
}
