package TextEditor;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javafx.print.*;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.ArrayList;
import java.util.List;


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
                FC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT","*.txt"),new FileChooser.ExtensionFilter("ODT","*.odt"),new FileChooser.ExtensionFilter("RTF","*.rtf"));
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
                FC2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT","*.txt"),new FileChooser.ExtensionFilter("ODT","*.odt"),new FileChooser.ExtensionFilter("RTF","*.rtf"));
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
                        String[] slist = s2.split("[\n ]");
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

        /*Print*/
        MenuItem print = new MenuItem("Print");
        print.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final Node node = root;
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER,PageOrientation.PORTRAIT,Printer.MarginType.DEFAULT);
                double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
                double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
                node.getTransforms().add(new Scale(scaleX,scaleY));
                PrinterJob job = PrinterJob.createPrinterJob();
                if (job != null){
                    boolean success = job.printPage(node);
                    if (success){ job.endJob();
                    }else {
                        System.out.println("printing failed");
                    }}else {
                    System.out.println("could not create a printer job");

                }
            }
        });
        Print.getItems().addAll(print);

        /*PDF  manage*/
        MenuItem PDF = new MenuItem("PDF");
        PDF.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String s = TF.getText();

                PDDocument doc = null;
                PDPage page1 = null;
                try{
                    doc = new PDDocument();
                    float wid = 700;
                    float hei = 600;
                    PDRectangle pdRectangle = new PDRectangle(wid,hei);
                    page1 = new PDPage(pdRectangle);
                    doc.addPage(page1);
                    s = s.replace("\n", "").replace("\r", "");

                    PDPageContentStream contentStream = new PDPageContentStream(doc,page1,PDPageContentStream.AppendMode.APPEND,true,false);
                    PDFont font = PDType1Font.TIMES_BOLD_ITALIC;
                    contentStream.beginText();

                    contentStream.newLineAtOffset(0,580);
                    contentStream.setFont(font,12);
                    contentStream.setNonStrokingColor(0,0,0);
                    contentStream.showText(s);
                    contentStream.endText();
                    contentStream.close();

                    doc.save("PDF.pdf");
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("Pdf generation complete");
                }
            }
        });
        Manage.getItems().addAll(PDF);

        /*Open .java file*/
        MenuItem javafile = new MenuItem("Open .java file");
        javafile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser FC = new FileChooser();
                FC.setTitle("open a .java file");
                FC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JAVA","*.java"));
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
                    String[] slist = S.split(" ");

                    JTextPane textPane = new JTextPane();
                    JScrollPane jsp = new JScrollPane(textPane);
                    Document doc = textPane.getDocument();
                    Style style = textPane.addStyle("I'm a Style", null);
                    StyleConstants.setFontSize(style,20);








                    List<String> checklist = new ArrayList<String>();
                    checklist.add("package");
                    checklist.add("import");
                    checklist.add("public");
                    checklist.add("private");
                    checklist.add("protected");
                    checklist.add("class");
                    checklist.add("extends");
                    checklist.add("void");
                    checklist.add("throws");
                    checklist.add("new");
                    checklist.add("int");
                    checklist.add("float");
                    checklist.add("double");
                    checklist.add("ture");
                    checklist.add("false");
                    checklist.add("final");
                    checklist.add(";");
                    checklist.add("try");
                    checklist.add("catch");
                    checklist.add("null");
                    checklist.add("if");
                    checklist.add("while");
                    checklist.add("else");
                    checklist.add("return");
                    checklist.add(",");
                    checklist.add("finally");
                    checklist.add("static");


                    for(String s:slist){
                        String flag = "black";

                        if (s.contains(",") || s.contains(";")){
                            String s1 = s.substring(0,s.length()-1);
                            String s2 = s.substring(s.length()-1,s.length());
                            StyleConstants.setForeground(style, Color.black);
                            try {
                                doc.insertString(doc.getLength(), s1, style);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                            StyleConstants.setForeground(style, Color.orange);
                            try {
                                doc.insertString(doc.getLength(), s2, style);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                            flag = ", or ;";
                        }


                        if (checklist.contains(s)){
                            StyleConstants.setForeground(style, Color.orange);
                            try {
                                doc.insertString(doc.getLength(), s, style);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                            flag = "orange";
                        }

                        if (flag.equals("black")){
                            StyleConstants.setForeground(style, Color.black);
                            try {
                                doc.insertString(doc.getLength(), s, style);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            doc.insertString(doc.getLength(), " ", style);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }

                    }


                    /*StyleConstants.setForeground(style, Color.red);
                    try {
                        doc.insertString(doc.getLength(), "dasdas", style);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }*/



                    JFrame fr = new JFrame("Open .java file");
                    fr.setSize(700,600);
                    fr.getContentPane().add(jsp);
                    fr.setVisible(true);






                }
                else {
                    System.out.println("please choose a .java file");
                }
            }
        });
        File.getItems().addAll(javafile);



        /*show*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
