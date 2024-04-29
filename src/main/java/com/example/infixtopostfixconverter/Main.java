package com.example.infixtopostfixconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Window primaryStage = null;

    File file = null;
    Group root = new Group();
    Scene scene = new Scene(root, 960, 600);
    Stack stack = new Stack();
    Stack stackFile = new Stack();
    Stack stackEquation = new Stack();
    static Stack stackBack = new Stack();
    HBox boxHorezantaal = new HBox();
    Button load = new Button("Load");
    Button Back = new Button("Back");
    TextField text = new TextField();
    VBox boxVertical = new VBox();
    ComboBox<String> textFiles = new ComboBox<String>();
    TextArea textEquations = new TextArea();
    Label Equation = new Label("Equations");
    Label Files = new Label("Files");
    String[] postfix;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Back.setDisable(true);
        readingFile(load);
        text.setMinSize(400, 20);
        boxHorezantaal.getChildren().addAll(Back, text, load);
        boxHorezantaal.setSpacing(10);
        boxHorezantaal.setLayoutX(200);
        boxHorezantaal.setLayoutY(20);
        root.getChildren().add(boxHorezantaal);

        boxVertical.getChildren().addAll(Equation, textEquations, Files, textFiles);
        boxVertical.setSpacing(10);
        boxVertical.setLayoutX(200);
        boxVertical.setLayoutY(50);
        textEquations.setMinSize(500, 200);
        textEquations.setEditable(false);

        textFiles.setMinSize(540, 30);
        root.getChildren().add(boxVertical);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    String fileContent = "";
    String tagString = "";
    String dataString = "";
    boolean tagFlag = false;
    boolean dataFlag = false;
    Stack tagStack = new Stack();
    Stack dataStack = new Stack();
    Stack dataStack2 = new Stack();
    static int count = 0;
    ArrayList<String> equationsList = new ArrayList<>();
    ArrayList<String> files = new ArrayList<>();

    String[] arrayTag;

    public void readingFile(Button load) throws IOException {
        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            run(selectedFile);
            Back.setDisable(false);
            stackBack.push(selectedFile.getPath().trim());

        });
        textFiles.setOnAction(e -> {
            if (textFiles.getSelectionModel().getSelectedItem() != null) {
                String x = textFiles.getSelectionModel().getSelectedItem().toString().trim();
                File file = new File(x);
                run(file);
                stackBack.push(file.getPath().trim());
            } else
                return;
        });
        Back.setOnAction(e -> {
            if (stackBack.isEmpty()) {
                if (!textFiles.getItems().isEmpty()) {
                    textFiles.getItems().clear();
                }
                equationsList.clear();
                files.clear();
                textEquations.setText("");
                text.setText("");
                return;
            }
            String x = stackBack.pop().trim();
            File file = new File(x);
            run(file);

        });

    }

    public void run(File file2) {
        fileContent = "";
        tagString = "";
        dataString = "";
        tagFlag = false;
        dataFlag = false;
        tagStack = new Stack();
        dataStack = new Stack();
        equationsList.clear();
        files.clear();
        textEquations.setText("");
        file = file2;
        try {
            BufferedReader readerBuffer = new BufferedReader(new FileReader(file));
            Scanner reader = new Scanner(readerBuffer);
            text.setText("" + file.getPath());
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                fileContent += line.trim() + "\n";
                if (line.equals(null))
                    break;

            }
            fileContent = fileContent.trim();
            for (int i = 0; i < fileContent.length(); i++) {
                if (fileContent.charAt(i) == '<') {
                    tagFlag = true;
                    dataFlag = false;

                    if (i == 0)
                        continue;

                    if (tagStack.peek().trim().equals("file") || tagStack.peek().trim().equals("equation"))
                        dataStack.push(dataString);

                    dataString = "";

                    continue;
                }
                if (fileContent.charAt(i) == '>') {
                    tagFlag = false;
                    dataFlag = true;
                    tagStack.push(tagString);
                    tagString = "";
                    continue;
                }
                if (tagFlag) {
                    tagString += fileContent.charAt(i);
                }
                if (dataFlag) {
                    dataString += fileContent.charAt(i);
                }

            }
            arrayTag = new String[tagStack.getSize()];
            count = 0;
            int pp = 0;
            while (!dataStack.isEmpty() && !tagStack.isEmpty()) {
                arrayTag[count] = "" + tagStack.peek();
                String tag = tagStack.pop();
                if (tag.trim().equalsIgnoreCase("file"))
                    files.add(dataStack.pop());
                else if (tag.trim().equalsIgnoreCase("equation")) {
                    equationsList.add(dataStack.pop());
                    pp++;
                }
                count++;
            }
            if (!tagStack.isEmpty()) {
                while (!tagStack.isEmpty()) {
                    arrayTag[count++] = tagStack.pop();
                }
            }
            count = 0;
            int size = equationsList.size();
            String[] arrayList = new String[size];
            while (count < equationsList.size()) {
                arrayList[count] = "" + equationsList.get((size - 1));
                count++;
                size--;
            }
            count = 0;
            while (count < equationsList.size()) {
                equationsList.set(count, "");
                equationsList.set(count, arrayList[count]);
                count++;
            }
            count = 0;
            count = arrayTag.length;
            while (count > 0) {
                tagStack.push(arrayTag[count - 1]);
                count--;
            }

            count = 0;
            String equations = "";
            if (isTagged()) {
                isBalanced();
                infixTOpostfix();
                for (int i = 0; i < equationsList.size(); i++) {
                    equations += equationsList.get(i) + "\n";
                }

            } else {
                equations = "invalid: missing tag";
            }
            if (!textFiles.getItems().isEmpty()) {
                textFiles.getItems().clear();
            }
            textEquations.setText(equations);
            textFiles.getItems().addAll(files);
            textFiles.setPromptText("the files in current file");
            count = 0;

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    public boolean isTagged() {
        String[] array = new String[tagStack.getSize()];
        Stack temp = new Stack();
        count = 0;
        while (!tagStack.isEmpty()) {
            String str = tagStack.pop().trim();
            array[count] = str;
            temp.push(str);
            count++;

        }
        int flag = 0;
        count = 0;
        while (!temp.isEmpty()) {
            tagStack.push(temp.pop());

        }

        count = 0;

        while (count <= array.length) {
            String str2 = "/" + tagStack.pop();
            for (int j = 0; j < array.length; j++) {
                if (str2.trim().equals(array[j])) {
                    array[j] = "";
                    flag++;
                }

            }

            count++;

        }

        count = 0;
        double x = ((double) (array.length)) / 2.0;
        if ((double) (flag) == x) {
            return true;

        } else {
            return false;
        }

    }

    public void isBalanced() {
        int size = equationsList.size();
        int counter = 0;
        while (counter < size) {
            Stack stack = new Stack();
            int x = 1;
            if (equationsList.get(counter).equals(null))
                x = 2;
            if (x == 1) {
                for (int i = 0; i < equationsList.get(counter).length(); i++) {

                    if (equationsList.get(counter).charAt(i) == '(') {
                        stack.push("(");
                    } else {
                        if (equationsList.get(counter).charAt(i) == ')') {
                            try {
                                char ch = stack.pop().charAt(0);
                                if (ch != '(') {
                                    break;
                                }
                            } catch (Exception e) {
                                equationsList.set(counter,
                                        "-" + equationsList.get(counter) + " ---> missing left prenthesiss");
                                break;
                            }

                        }
                    }

                }

            }
            if (!stack.isEmpty())
                equationsList.set(counter, "-" + equationsList.get(counter) + " ---> missing prenthesiss");
            counter++;
        }
        counter = 0;

    }

    public void infixTOpostfix() {
        for (int i = 0; i < equationsList.size(); ++i) {
            String prev = "";
            Stack stackPostFix = new Stack();
            String finalString = "";
            String str = equationsList.get(i).trim();
            if (str.charAt(0) == '-') {
                equationsList.set(i, equationsList.get(i).substring(1));
                continue;
            } else {
                for (int j = 0; j < str.length(); j++) {
                    char letter = str.charAt(j);
                    if (letter == ' ') {
                        prev = " ";

                    } else if (Character.isDigit(letter)) {
                        if (prev == "") {
                            prev = "" + letter;
                            finalString = finalString + letter;
                        } else {
                            if (Character.isDigit(prev.charAt(0))) {
                                finalString = finalString + letter;

                            } else {
                                finalString = finalString + " " + letter;
                            }
                            prev = "" + letter;
                        }
                    } else if (letter == '(') {
                        prev = "" + letter;
                        stackPostFix.push("" + letter);
                    } else if (letter == ')') {
                        while ((char) stackPostFix.peek().charAt(0) != '(') {
                            finalString = finalString + " " + stackPostFix.pop();
                        }
                        stackPostFix.pop();
                    } else {
                        prev = "" + letter;
                        while (!stackPostFix.isEmpty()
                                && priority(letter) <= priority((char) stackPostFix.peek().charAt(0))) {
                            finalString = finalString + " " + stackPostFix.pop();
                        }
                        stackPostFix.push("" + letter);
                    }

                }
                while (!stackPostFix.isEmpty()) {
                    if (stackPostFix.peek().charAt(0) == '(' || stackPostFix.peek().charAt(0) == ')') {
                        System.out.println("error ");
                    }
                    finalString = finalString + " " + stackPostFix.pop();
                }
                finalString = finalString.trim();
                equationsList.set(i, equationsList.get(i) + " ---> " + finalString);

            }
            String[] postfix = finalString.split(" ");
            String[] postfix2 = new String[postfix.length];
            count = 0;
            while (count < postfix.length) {
                if (postfix[count].equals(" ")) {
                    continue;
                }
                postfix2[count] = postfix[count].trim();
                count++;
            }
            evaluate(postfix2, i);
        }

    }

    public int priority(char c) {

        switch (c) {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;
        }
        return -1;

    }

    public void evaluate(String[] str, int x) {
        Stack stack = new Stack();
        for (int i = 0; i < str.length; i++) {
            String letter = str[i];
            if (letter == "") {
                continue;
            } else {

                if (letter.length() > 1) {
                    stack.push(letter);

                } else {
                    if (Character.isDigit(letter.charAt(0))) {
                        stack.push(letter);
                    } else {
                        String str1 = stack.pop();
                        String str2 = stack.pop();
                        if (str1 == null || str2 == null) {
                            break;
                        }
                        double first = Double.parseDouble(str1);
                        double second = Double.parseDouble(str2);
                        if (letter.equals("*")) {
                            stack.push("" + (second * first));

                        } else if (letter.equals("+")) {
                            stack.push("" + (second + first));

                        } else if (letter.equals("-")) {
                            stack.push("" + (second - first));

                        } else if (letter.equals("/")) {
                            stack.push("" + (second / first));
                            if (stack.peek().trim().equals("Infinity")) {
                                equationsList.set(x, equationsList.get(x) + " ---> divided by zero");
                                return;
                            }

                        }

                    }

                }
            }
        }
        if (stack.getSize() != 1) {
            equationsList.set(x, equationsList.get(x) + " ---> not balanced");
        } else {
            equationsList.set(x, equationsList.get(x) + " ---> " + stack.pop());
        }

    }
}
