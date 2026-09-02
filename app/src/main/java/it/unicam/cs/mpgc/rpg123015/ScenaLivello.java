package it.unicam.cs.mpgc.rpg123015;

import java.io.File;
import java.util.List;

import com.sun.tools.javac.Main;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
    public ScenaLivello(int id) {
        genScenaLayout(id);
    }

    //Genera ed organizza gli elementi grafici della scena, come il numero delle colonne e righe, le celle ed il loro aspetto.
    private void genScenaLayout(int id) {
        scenaLivello = new Scene(pannello, 800, 494);

        Text titoloLivello = new Text("Livello " + id);
        pannello.setTop(titoloLivello);

        stats = new GridPane(0,0);
        pannello.setRight(stats);


        TextArea actionLog = new TextArea("Action Log");
        actionLog.setFocusTraversable(false);
        actionLog.setEditable(false);
        actionLog.setWrapText(true);
        actionLog.setPrefWidth((double)scenaLivello.getWidth()/4);
        pannello.setLeft(actionLog);

        pannelloArena = new GridPane(0,0);
        pannelloArena.setFocusTraversable(false);
        GridPane.setValignment(pannelloArena, VPos.CENTER);
        GridPane.setHalignment(pannelloArena, HPos.CENTER);
        BorderPane.setAlignment(pannelloArena, Pos.CENTER);
        pannello.setCenter(pannelloArena);

        Text legenda = new Text("[W][A][S][D] muoviti [Z] attacco a distanza [ESC] pausa");
        pannello.setBottom(legenda);

        actionLog.setOnMouseClicked(event -> {
            pannelloArena.getChildren().getFirst().requestFocus();
        });
    }



    public Scene getScenaLivello() {
        return scenaLivello;
    }

    public void genArena(int lArena, int hArena){
        pannelloArena = new GridPane(0,0);
        Insets marginiArena = new Insets(10,14,10,14);
        GridPane.setMargin(pannelloArena, marginiArena);
        pannello.setCenter(pannelloArena);

        pannelloArena.setStyle("-fx-background-color: #ffff00;");

        //QUESTE 4 RIGHE FANNO GIRARE IL GIOCO
        Image placeholder = new Image("icons/Cobblestone.png");
        ImageView inputFocus = new ImageView(placeholder);
        pannelloArena.getChildren().add(inputFocus);
        inputFocus.requestFocus();


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

        //Viene popolata l'area di destra con le stat del giocatore
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {

                Pane p = new Pane();
                p.setStyle("-fx-background-image: url('/icons/Heart.png'); -fx-background-repeat: no-repeat; -fx-background-size: cover; -fx-image-rendering: pixelated;");
                stats.add(p, i, j);
            }
        }

        stats.add(new Text("STR 1"), 0, 5);
        stats.add(new Text("DEX 2"), 1, 5);
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

    public void SpawnEntity(Image icon, String id, int x, int y) {
        ImageView iv = new ImageView(icon);
        iv.setId(id);
        for(Node n : pannelloArena.getChildren()) {
          if ( n instanceof Pane && n.getId().equals("p" + x + "-" + y))
          {
              System.out.println("trovato " + n.getId());
              ((Pane) n).getChildren().add(iv);
          }
        }

    }

    public void MoveEntity(Image icon, String id, int x, int y)
    {
        ImageView f = new ImageView(icon);
        f.setId(id);

        for(Node n : pannelloArena.getChildren()) {
            if (n instanceof Pane && !((Pane) n).getChildren().isEmpty())
            {
                System.out.println(n.getId() + " | " + ((Pane) n).getChildren().size());
                if(((Pane) n).getChildren().stream().anyMatch(o -> o.getId().equals(f.getId())))
                {
                    System.out.println("SNIPED" + n.getId());
                    ((Pane) n).getChildren().remove(0, ((Pane) n).getChildren().size());
                    ((Pane) pannelloArena.getChildren().stream().filter(p -> p instanceof Pane && p.getId().equals("p" + x + "-" + y)).findFirst().get()).getChildren().add(f);
                    break;
                }
            }
        }
    }

    public void RemoveEntity(String id)
    {
        for (Node e: pannelloArena.getChildren())
        {
            if (e instanceof Pane && !((Pane)e).getChildren().isEmpty())
            {
                if(((Pane) e).getChildren().stream().anyMatch(o -> o.getId().equals(id)))
                {
                    System.out.println("ELIMINATO l'elemento");
                    ((Pane) e).getChildren().remove(0, ((Pane) e).getChildren().size());
                    break;
                }
            }
        };

    }

    public void AttachObj(String id, Object whatever)
    {
        for (Node e: pannelloArena.getChildren())
        {
            if (e instanceof Pane)
            {
                System.out.println("PANNELLO " + ((Pane) e).getChildren().size());
                if(((Pane)e).getChildren().stream().findFirst().isPresent())
                {
                    System.out.println(((Pane)e).getChildren().stream().findFirst().get().getId());
                    ImageView iv = (ImageView) ((Pane)e).getChildren().stream().findAny().get();
                    if(iv.getId().equals(id))
                    {
                        System.out.println("TROVATO IL PANNELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOo");
                    }
                }
            }
        };
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

    public void getStats(int maxHp, int STR, int DEX) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+ (int) Math.ceil((double) maxHp /2));
        IniGrid(stats, 2, (int) Math.ceil((double) maxHp /2), 32);
    }
}
