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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.Optional;

public class MainMenu{

    Button btnNewGame = new Button("Nuova partita");
    Button btnGalleria = new Button("Galleria degli eroi");
    Button btnQuit = new Button("Esci dal gioco");
    VBox pannello = new VBox(16);
    Scene mmScene = new Scene(pannello, 800, 494);


    public MainMenu() {
        mmScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/MM_ScenaLvl.css")).toExternalForm());
        pannello.setAlignment(Pos.CENTER);

        Image titleCard = new Image("icons/TitleCard.png");
        ImageView titleCardView = new ImageView(titleCard);

        btnNewGame.setFont(Font.font("Power Red and Green", 24));
        btnNewGame.setPrefHeight(32);
        btnNewGame.setPrefWidth(mmScene.getWidth()/3);
        btnNewGame.setAlignment(Pos.CENTER);
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
                Livello livello = new Livello(new Avatar(td.getEditor().getText()));
                Stage x = (Stage) btnNewGame.getScene().getWindow();
                x.requestFocus();
                x.setScene(livello.getScenaLivello());
                livello.GameStart();
            }

        });

        btnGalleria.setFont(Font.font("Power Red and Green", 24));
        btnGalleria.setPrefHeight(32);
        btnGalleria.setPrefWidth(mmScene.getWidth()/3);
        btnGalleria.setAlignment(Pos.CENTER);
        btnGalleria.setOnAction(e -> {
            HeroGallery h = new HeroGallery();
            Stage x = (Stage) btnGalleria.getScene().getWindow();
            x.requestFocus();
            x.setScene(h.getGalleryScene());
        });

        btnQuit.setFont(Font.font("Power Red and Green", 24));
        btnQuit.setPrefHeight(32);
        btnQuit.setPrefWidth(mmScene.getWidth()/3);
        btnQuit.setAlignment(Pos.CENTER);
        btnQuit.setOnAction(e -> {
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        pannello.setId("pannelloMM");
        pannello.setAlignment(Pos.CENTER);
        pannello.getChildren().addAll(titleCardView,btnNewGame, btnGalleria, btnQuit);

        //il padding Insets è in relazione tra gli elementi del pannello con i bordi del pannello, non tra quest'ultimo e la scena.
        Insets insets = new Insets(20, 20, 60, 20);
        pannello.setPadding(insets);
        System.out.println("MAIN MENU PRESENTATO.");
        System.out.println(pannello.getChildren().size());
    }



}
