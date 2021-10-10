package TextEditor;



import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.ArrayList;




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
        MB.getMenus().addAll(File,Search,View,Manage,Help,Print);
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
        TF.appendText("\n");


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
                if (TF.getText().isEmpty()) {
                    System.out.println("Exit");
                    System.exit(0);
                    return;
                }
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Exit without saving?",
                        ButtonType.YES,
                        ButtonType.CANCEL
                );
                alert.setTitle("Confirm");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    System.exit(0);
                }
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
                newstage.setScene(new Scene(search, 200, 100));
                newstage.setTitle("Search");
                Label label1 = new Label("Press enter");
                Label label2 = new Label("Support continuous search");
                TextField newTF = new TextField();
                search.getChildren().addAll(newTF,label1,label2);
                newstage.show();


                newTF.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String s = newTF.getText();
                        String s2 = TF.getText();
                        String[] slist = s2.split(" ");
                        ArrayList<Integer> longthgroup = new ArrayList<>();
                        ArrayList<Integer> lwgroup = new ArrayList<>();
                        for (int i = 0; i < slist.length; i++) {
                            if (slist[i].equals(s)) {
                                StringBuilder SB2 = new StringBuilder();
                                for (int j = 0; j < i; j++) {
                                    SB2.append(slist[j]);
                                    SB2.append(" ");
                                }
                                int longth = SB2.toString().length();
                                int lw = s.length();
                                longthgroup.add(longth);
                                lwgroup.add(lw);
                            }
                        }

                        if (longthgroup.size()>0) {
                            TF.selectRange(longthgroup.get(0), longthgroup.get(0) + lwgroup.get(0));
                            final int[] n = {1};

                            newTF.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    if (n[0] < longthgroup.size()) {
                                        TF.selectRange(longthgroup.get(n[0]), longthgroup.get(n[0]) + lwgroup.get(n[0]));
                                        n[0] = n[0] + 1;
                                    } else {
                                        System.out.println("Search complete");
                                    }
                                }
                            });
                        }else{
                            System.out.println("Search complete");
                        }

                    }
                });

            }
        });
        Search.getItems().addAll(search);


        /*Help*/
        MenuItem about = new MenuItem("about");
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                VBox search = new VBox();
                Stage newstage = new Stage();
                newstage.setScene(new Scene(search, 300, 100));
                newstage.setTitle("about");
                Label label1 = new Label("Team members: Ziqi Gao, Siyuan Du" + "\n" + "This is a text editor");
                search.getChildren().addAll(label1);
                newstage.show();
            }
        });
        Help.getItems().addAll(about);



        /*print*/
        MenuItem print = new MenuItem("Print");
        print.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ObservableSet<Printer> printers = Printer.getAllPrinters();
                for(Printer printer : printers){
                    TF.appendText(printer.getName()+"\n");
                }
            }
        });
        VBox prt = new VBox(10);
        /*
        final TextArea textArea = new TextArea();
        prt.getChildren().addAll(button,textArea);
        prt.setPrefSize(400,250);
        prt.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        Scene scene = new Scene(prt);
        primaryStage.setScene(scene);
        primaryStage.setTitle("show all printers");

         */

        /*show*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
