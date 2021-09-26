package TextEditor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

            }
        });

        MenuItem Save = new MenuItem("Save");
        File.getItems().addAll(New,Open,Save);




        /*show*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
