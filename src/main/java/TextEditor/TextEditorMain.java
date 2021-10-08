package TextEditor;




import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


import java.io.*;

public class TextEditorMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*init scene*/
        VBox root = new VBox();
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.setTitle("Text Editor");


        /*init menu*/
        MenuBar MB = new MenuBar();
        Menu File = new Menu("File");
        Menu Search = new Menu("Search");
        Menu View = new Menu("View");
        Menu Manage = new Menu("Manage");
        Menu Print = new Menu("Print");
        Menu Help = new Menu("Help");
        MB.getMenus().addAll(File,Search,View,Manage,Help);
        root.getChildren().addAll(MB);


        /*Write*/
        TextArea TF = new TextArea();
        double height = 600;
        double width = 700;
        TF.setPrefHeight(height);
        TF.setPrefWidth(width);
        root.getChildren().addAll(TF);
        TF.setWrapText(true);
        //add start time

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        TF.appendText(dtf.format(now));


        /*File*/
        MenuItem New = new MenuItem("New");
        New.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s = new Stage();
                try{
                    start(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        MenuItem Open = new MenuItem("Open");
        Open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser FC = new FileChooser();
                FC.setTitle("open a txt");
                FC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT","*.txt"),new FileChooser.ExtensionFilter("ODT","*.odt"));
                File f = FC.showOpenDialog(primaryStage);
                if (f != null){
                    StringBuilder SB = new StringBuilder();
                    try {
                        BufferedReader BR = new BufferedReader(new FileReader(f));
                        String line = BR.readLine();
                        while (line != null){
                            SB.append(line);
                            line = BR.readLine();
                            SB.append("\n");
                        }
                        System.out.println("open complete");
                        BR.close();
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }
                    String S = SB.toString();
                    TF.appendText(S);
                }
                else {
                    System.out.println("please choose a file");
                }
            }
        });
        MenuItem Save = new MenuItem("Save");
        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser FC2 = new FileChooser();
                FC2.setTitle("save a txt");
                FC2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT","*.txt"),new FileChooser.ExtensionFilter("ODT","*.odt"));
                File f = FC2.showSaveDialog(primaryStage);
                String S2 = TF.getText();
                try{
                    BufferedWriter BW = new BufferedWriter(new FileWriter(f));
                    BW.write(S2);
                    BW.close();
                    System.out.println("save complete");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem Exit = new MenuItem("Exit");
        Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Exit");
                System.exit(0);
            }
        });

        File.getItems().addAll(New,Open,Save,Exit);


        /*Search*/

        MenuItem search = new MenuItem("Search");
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                VBox search = new VBox();
                Stage newstage = new Stage();
                newstage.setScene(new Scene(search, 200, 300));
                newstage.setTitle("Search");
                TextField newTF = new TextField();
                search.getChildren().addAll(newTF);
                newstage.show();

                newTF.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String s = newTF.getText();
                        String s2 = TF.getText();
                        String[] slist = s2.split("\\s+");
                        


                    }
                });

            }
        });
        Search.getItems().addAll(search);
        /*show*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
