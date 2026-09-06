package it.unicam.cs.mpgc.rpg123015;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.Optional;

public class MainMenu{

    Button btnNewGame = new Button("New Gaaaaaaaaaaaaaaaaaaaaame");
    Button btnGalleria = new Button("Galleria degli Eroi");
    Button btnQuit = new Button("Quit Game");
    FlowPane pannello = new FlowPane(Orientation.VERTICAL);
    Scene mmScene = new Scene(pannello, 800, 494);


    public MainMenu() {
        mmScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fontstyle.css")).toExternalForm());

        Image titleCard = new Image("icons/TitleCard.png");
        ImageView titleCardView = new ImageView(titleCard);

        btnNewGame.setOnAction(e -> {

            TextInputDialog td = new TextInputDialog("");
            td.setResizable(false);
            td.setHeaderText("Quale sarà il nome di questo eroe?");
            td.setGraphic(new ImageView(new Image("/icons/MysteryIdentity.png")));
            td.getEditor().setStyle("-fx-font-family: Power Red and Green;");
            Optional<String> result = td.showAndWait();
            if (result.isEmpty()) {

            }
            else{
                if(td.getEditor().getText().isEmpty())
                    td.getEditor().setText("Guglielmo");
                Livello livello = new Livello(td.getEditor().getText());
                Stage x = (Stage) btnNewGame.getScene().getWindow();
                x.requestFocus();
                x.setScene(livello.getScenaLivello());
                livello.GameStart();
            }

        });

        btnQuit.setOnAction(e -> {
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        pannello.setId("pannelloMM");
        pannello.setAlignment(Pos.CENTER);
        pannello.setVgap(16);
        pannello.setPrefWrapLength(494);
        pannello.getChildren().addAll(titleCardView,btnNewGame, btnGalleria, btnQuit);

        //il padding Insets è in relazione tra gli elementi del pannello con i bordi del pannello, non tra quest'ultimo e la scena.
        Insets insets = new Insets(20, 20, 60, 20);
        pannello.setPadding(insets);
        System.out.println("MAIN MENU PRESENTATO.");

    }



}
