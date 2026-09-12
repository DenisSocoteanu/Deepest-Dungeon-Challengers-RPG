package it.unicam.cs.mpgc.rpg123015;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class HeroGallery {
    MediaPlayer musicPlayer;
    BorderPane pannello = new BorderPane();
    Scene galleryScene = new Scene(pannello, 800, 494);
    GridPane areaSalvataggi = new GridPane();
    List<FlowPane> pSalvataggi = new ArrayList<>();
    int pageNumber = 0;

    public HeroGallery()
    {
        playBGM();
        fadeInMusic();

        galleryScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/HeroGallery.css")).toExternalForm());

        galleryScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE ->  {
                    fadeOutMusic();
                    returnToMM();
                }
            }
        });

        loadAllSaves();
        initAreaSalvataggi();
        showSaves();

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
        Media reposeOST = new Media(new File("src/main/resources/audio/DDC_bgm_FileSelect.mp3").toURI().toString());
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

    private void initAreaSalvataggi()
    {
        pannello.setId("HeroGallery");

        Button scrollLeftButton = new Button("<");
        scrollLeftButton.setId("scrollLeftButton");
        scrollLeftButton.setPrefHeight(galleryScene.getHeight()); scrollLeftButton.setPrefWidth(16);
        scrollLeftButton.setFont(Font.font("Power Red and Green", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 16));

        Button scrollRightButton = new Button(">");
        scrollRightButton.setId("scrollRightButton");
        scrollRightButton.setPrefHeight(galleryScene.getHeight());  scrollRightButton.setPrefWidth(16);
        scrollRightButton.setFont(Font.font("Power Red and Green", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 16));

        scrollLeftButton.setOnAction(e -> {
            pageNumber--;
            showSaves();
            checkDisability(scrollLeftButton, scrollRightButton);
        });

        scrollRightButton.setOnAction(e -> {
            pageNumber++;
            showSaves();
            checkDisability(scrollLeftButton, scrollRightButton);
        });

        checkDisability(scrollLeftButton, scrollRightButton);

        pannello.setLeft(scrollLeftButton);
        pannello.setRight(scrollRightButton);
        pannello.setPadding(new Insets(2,4,4,4));

        areaSalvataggi.setHgap(8);
        areaSalvataggi.setVgap(8);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth((galleryScene.getWidth()-64-32)/3);
        column1.setPrefWidth((galleryScene.getWidth()-64-32)/3);
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight((galleryScene.getHeight()-24)/2);
        row1.setPrefHeight((galleryScene.getHeight()-24)/2);

        areaSalvataggi.getColumnConstraints().addAll(column1, column1, column1);
        areaSalvataggi.getRowConstraints().addAll(row1, row1);

        GridPane.setMargin(areaSalvataggi, new Insets(4,8,4,8));
        areaSalvataggi.setAlignment(Pos.CENTER);

        pannello.setCenter(areaSalvataggi);
    }

    private void checkDisability(Button scrollLeftButton, Button scrollRightButton){

        if ((pageNumber+1)*6 >= pSalvataggi.size()) {
            scrollRightButton.setDisable(true);
        }
        else {
            scrollRightButton.setDisable(false);
        }

        if(pageNumber == 0){
            scrollLeftButton.setDisable(true);
        }
        else {
            scrollLeftButton.setDisable(false);
        }
    }

    public Scene getGalleryScene() {
        return galleryScene;
    }

    private void loadAllSaves()
    {
        //------ Blocco quale definisce il gradiente dei pulsanti ------
        // 180,231,188 177,216,189 // 95,136,132 47,80,95
        Stop[] stop = {new Stop(0, Color.color(0.372,0.533,0.518)),
                new Stop(1, Color.color(0.184,0.314,0.372))};

        LinearGradient linear_gradient = new LinearGradient(0, 0,
                0, 1, true, CycleMethod.NO_CYCLE, stop);
        BackgroundFill backgroundFill = new BackgroundFill(linear_gradient, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(backgroundFill);
        //------ bg può essere aggiunto a un qualunque button.setBackground() - Border Block ------
        BorderStrokeStyle bss = new BorderStrokeStyle(StrokeType.OUTSIDE, StrokeLineJoin.ROUND, StrokeLineCap.ROUND, 0,0, null);
        BorderStroke bs = new BorderStroke(Color.color(0.706,0.906,0.737), bss, CornerRadii.EMPTY, new BorderWidths(3));
        Border b = new Border(bs);
        //------ Border Block ------

        File dir = new File("..\\app\\src\\main\\resources\\saves\\");
        File[] listOfFiles = Objects.requireNonNull(dir.listFiles());

        SaveLoadParser slp = new SaveLoadParser();
        for( File file : listOfFiles)
        {
            if (file.getName().endsWith(".json"))
            {
                Avatar a = slp.loadSave(file.getName().replace(".json",""));

                Label lNome = new Label(a.getName());
                lNome.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 23));
                Label lLvl = new Label("Livello: "+a.lvl); lLvl.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 16));
                Label lVitt = new Label("Piano B-" + a.getVittorie()); lVitt.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 16));
                ImageView iv = new ImageView(a.getIcon());


                FlowPane p = new FlowPane(Orientation.HORIZONTAL,32,16);
                FlowPane.setMargin(iv, new Insets(8,0,0,8));
                FlowPane.setMargin(lLvl, new Insets(8,0,0,8));
                p.setStyle("-fx-background-color: rgba(216,216,216,0.85);");
                p.getChildren().addAll(iv,lNome,lLvl, lVitt);
                if(a.getIsAlive())
                {
                    Button loadSave = new Button("Carica Eroe");
                    loadSave.setFont(Font.font("Power Red and Green", FontWeight.BOLD, FontPosture.REGULAR, 16));
                    loadSave.setBorder(b);
                    loadSave.setTextFill(Color.WHITE);
                    loadSave.setBackground(bg);
                    loadSave.setOnAction(event -> {
                        fadeOutMusic();
                        Livello livello = new Livello(a,null);
                        livello.getScena().playBGM();
                        livello.getScena().fadeInMusic();

                        Stage x = (Stage) loadSave.getScene().getWindow();
                        x.requestFocus();
                        x.setScene(livello.getScenaLivello());
                        livello.GameStart();
                    });
                    FlowPane.setMargin(loadSave, new Insets(8,0,0,8));

                    p.getChildren().add(loadSave);
                }

                pSalvataggi.add(p);

            }
        }
    }


    private void showSaves()
    {
        areaSalvataggi.getChildren().clear();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 2; j++){
                int nSave = (i*2)+j+(pageNumber*6);
                if(nSave < pSalvataggi.size())
                    areaSalvataggi.add(pSalvataggi.get(nSave), i, j);
            }
        }
    }

    private void returnToMM()
    {
        MainMenu mm = new MainMenu();
        Stage x = (Stage) galleryScene.getWindow();
        x.setScene(mm.mmScene);
    }
}
