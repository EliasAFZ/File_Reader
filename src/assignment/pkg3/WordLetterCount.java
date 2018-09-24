package assignment.pkg3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Assignment #3
 *
 * @author Elias Afzalzada
 */
public class WordLetterCount extends Application {

    @Override
    public void start(Stage primaryStage) {

        //Variables and handles
        threadReader mt = new threadReader();
        int mainWindowSizeX = 400;
        int mainWindowSizeY = 80;
        int subWindowSizeX = 400;
        int subWindowSizeY = 500;

        //Buttons setup and positioned
        Label title = new Label("Word and Letter Counter");
        Button selectButton = new Button("Select file");
        Button generateButton = new Button("Generate Report");
        title.setLayoutX(130);
        selectButton.setLayoutX(100);
        selectButton.setLayoutY(50);
        generateButton.setLayoutX(200);
        generateButton.setLayoutY(50);

        //Pane setup
        Pane pane = new Pane();
        pane.getChildren().addAll(selectButton, generateButton, title);

        //Frame setup        
        primaryStage.setTitle("Assignment 3");
        primaryStage.setScene(new Scene(pane, mainWindowSizeX, mainWindowSizeY));
        primaryStage.setResizable(false);
        primaryStage.show();

        //File selection setup
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input Files");
        final List<File> fileList = new ArrayList<>();
        //Button event handler for select file
        selectButton.setOnAction(event -> {
            fileList.clear();
            fileList.addAll(fileChooser.showOpenMultipleDialog(primaryStage));
            if (!fileList.isEmpty()) {
                mt.setlist(fileList);
            }
        });

        //Button event handler for genereating threads
        generateButton.setOnAction(event -> {
            int numOfWindows = mt.getNumOfWindows();

            for (int i = 0; i < numOfWindows; i++) {

                //Sub window
                Stage secondStage = new Stage();
                secondStage.setTitle(fileList.get(i).getName());
                secondStage.setResizable(false);

                //Labels to display number of words and letters
                Label wordLabel = new Label();
                wordLabel.setLayoutX(50);
                wordLabel.setLayoutY(50);
                Label letterLabel = new Label();
                letterLabel.setLayoutX(250);
                letterLabel.setLayoutY(50);

                //Text area to display file text
                TextArea textArea = new TextArea(mt.getFileText());
                textArea.setWrapText(true);
                textArea.setPrefHeight(400);
                textArea.setPrefWidth(400);
                textArea.setLayoutX(5);
                textArea.setLayoutY(100);

                //Scene setup
                Pane secondPane = new Pane();
                secondPane.getChildren().addAll(textArea, wordLabel, letterLabel);
                Scene auxilaryScene = new Scene(secondPane, subWindowSizeX, subWindowSizeY);
                secondStage.setScene(auxilaryScene);
                secondStage.show();
                
                //Recieves future and prevents UI locking
                CompletableFuture<List<String>> future = mt.fileSetup(i);
                future.whenComplete((list,throwable) -> {
                    //Used to update GUI components
                    Platform.runLater(() -> {
                        textArea.setText(list.get(0));
                        letterLabel.setText("Number of letters:" + list.get(1));
                        wordLabel.setText("Number of words:" + list.get(2));
                    });
                });
                //Starts threads
                mt.start();
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
