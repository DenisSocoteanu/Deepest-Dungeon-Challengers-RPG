package it.unicam.cs.mpgc.rpg123015;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class HeroGallery {
    BorderPane pannello = new BorderPane();
    Scene galleryScene = new Scene(pannello, 800, 494);
    GridPane areaSalvataggi = new GridPane();
    List<FlowPane> pSalvataggi = new ArrayList<>();
    int pageNumber = 0;

    public HeroGallery()
    {

        galleryScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE ->  {
                    returnToMM();
                }
            }
        });

        loadAllSaves();
        initAreaSalvataggi();
        showSaves();

    }

    private void initAreaSalvataggi()
    {
        galleryScene.setFill(Color.YELLOW);

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

        System.out.println("PAGENUMBER "+pageNumber);

        if ((pageNumber+1)*6 >= pSalvataggi.size()) {
            scrollRightButton.setDisable(true);
        }
        else {
            scrollRightButton.setDisable(false);
        }

        if(pageNumber == 0){
            System.out.println("Dis L");
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
        File dir = new File("..\\app\\src\\main\\resources\\saves\\");
        File[] listOfFiles = Objects.requireNonNull(dir.listFiles());

        SaveLoadParser slp = new SaveLoadParser();
        for( File file : listOfFiles)
        {
            if (file.getName().endsWith(".json"))
            {
                Avatar a = slp.loadSave(file.getName().replace(".json",""));
                System.out.println(file.getName() + " | " + a.getName());

                Label lNome = new Label(a.getName());
                lNome.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 21));
                Label lLvl = new Label("Livello: "+a.lvl); lLvl.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 14));
                Label lVitt = new Label("Piano B-" + a.getVittorie()); lVitt.setFont(Font.font("Power Red and Green", FontWeight.BOLD, 14));
                ImageView iv = new ImageView(a.getIcon());


                FlowPane p = new FlowPane(Orientation.HORIZONTAL,32,16);
                p.setStyle("-fx-background-color: rgb(216,216,216);");
                p.getChildren().addAll(iv,lNome,lLvl, lVitt);
                if(a.getIsAlive())
                {
                    Button loadSave = new Button("Carica Eroe");
                    loadSave.setOnAction(event -> {
                        Livello livello = new Livello(a);
                        Stage x = (Stage) loadSave.getScene().getWindow();
                        x.requestFocus();
                        x.setScene(livello.getScenaLivello());
                        livello.GameStart();
                    });

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
