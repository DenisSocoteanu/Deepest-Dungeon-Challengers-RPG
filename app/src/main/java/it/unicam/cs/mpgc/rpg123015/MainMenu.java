package it.unicam.cs.mpgc.rpg123015;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class MainMenu{

    Button btnNewGame = new Button("Nuova partita");
    Button btnGalleria = new Button("Galleria degli eroi");
    Button btnQuit = new Button("Esci dal gioco");
    VBox pannello = new VBox(16);
    Scene mmScene = new Scene(pannello, 800, 494);
    MediaPlayer musicPlayer;


    public MainMenu() {
        mmScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/MM_ScenaLvl.css")).toExternalForm());
        pannello.setAlignment(Pos.CENTER);

        playBGM();
        fadeInMusic();

        //------ Background Block ------
        // RICORDA DI CREDITARE: Maruu su Shutterstock.com (https://www.shutterstock.com/g/Maruu)
        BackgroundImage backgroundImage = new BackgroundImage(new Image("/icons/MainBackground.jpg"),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pannello.setBackground(background);
        //------ Background Block - Gradient Block ------
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

        Image titleCard = new Image("icons/TitleCard.png");
        ImageView titleCardView = new ImageView(titleCard);

        btnNewGame.setFont(Font.font("Power Red and Green", 24));
        btnNewGame.setTextFill(Color.ALICEBLUE);
        btnNewGame.setBackground(bg);
        btnNewGame.setBorder(b);
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

            fadeOutMusic();

            if (result.isEmpty()) {

            }
            else{
                if(td.getEditor().getText().isEmpty())
                    td.getEditor().setText("Guglielmo");

                Livello livello = new Livello(new Avatar(td.getEditor().getText()), null);
                livello.getScena().playBGM();
                livello.getScena().fadeInMusic();

                Stage x = (Stage) btnNewGame.getScene().getWindow();
                x.requestFocus();
                x.setScene(livello.getScenaLivello());
                livello.GameStart();
            }

        });

        btnGalleria.setFont(Font.font("Power Red and Green", 24));
        btnGalleria.setTextFill(Color.ALICEBLUE);
        btnGalleria.setBackground(bg);
        btnGalleria.setBorder(b);
        btnGalleria.setPrefHeight(32);
        btnGalleria.setPrefWidth(mmScene.getWidth()/3);
        btnGalleria.setAlignment(Pos.CENTER);
        btnGalleria.setOnAction(e -> {
            fadeOutMusic();
            HeroGallery h = new HeroGallery();
            Stage x = (Stage) btnGalleria.getScene().getWindow();
            x.requestFocus();
            x.setScene(h.getGalleryScene());
        });

        btnQuit.setFont(Font.font("Power Red and Green", 24));
        btnQuit.setTextFill(Color.ALICEBLUE);
        btnQuit.setBackground(bg);
        btnQuit.setBorder(b);
        btnQuit.setPrefHeight(32);
        btnQuit.setPrefWidth(mmScene.getWidth()/3);
        btnQuit.setAlignment(Pos.CENTER);
        btnQuit.setOnAction(e -> {
            fadeOutMusic();
            Stage x = (Stage) btnQuit.getScene().getWindow();x.close();
        });

        pannello.setId("pannelloMM");
        pannello.setAlignment(Pos.CENTER);
        pannello.getChildren().addAll(titleCardView,btnNewGame, btnGalleria, btnQuit);

        //il padding Insets è in relazione tra gli elementi del pannello con i bordi del pannello, non tra quest'ultimo e la scena.
        Insets insets = new Insets(20, 20, 60, 20);
        pannello.setPadding(insets);
    }

    public void fadeInMusic()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(musicPlayer.volumeProperty(),0.85))
        );
        timeline.play();
    }

    private void playBGM()
    {
        Media reposeOST = new Media(new File("src/main/resources/audio/DDC_bgm_Main.mp3").toURI().toString());
        musicPlayer = new MediaPlayer(reposeOST);
        musicPlayer.play();
    }

    public void fadeOutMusic()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(musicPlayer.volumeProperty(),0))
        );
        timeline.play();
        timeline.setOnFinished(e -> {
            musicPlayer.stop();
        });
    }

}
