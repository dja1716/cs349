import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.shape.*;

import java.util.ArrayList;


public class Main extends Application {
    // toolToggleInt == 0 if toolToggle deselected
    int toolToggleInt = 0;


    // thickness and type
    int lineType = 0;
    float lineThickness = 4;
    String lineColor = "#000000";


    boolean path = false;
    Path currentPath;

    // testing purpose
    boolean isTest = true;

    // control line



    // for save and load



    // deleting path
    public void deletePath(Path path, Group group) {
        group.getChildren().remove(path);
    }

    @Override
    public void start(Stage stage) throws Exception {



        MenuBar menubar = new MenuBar();

        Menu fileMenu = new Menu("File");
        // file -> new load save quit
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem quitMenuItem = new MenuItem("Quit");
        fileMenu.getItems().addAll(newMenuItem, loadMenuItem, saveMenuItem, quitMenuItem);




        Menu helpMenu = new Menu("Help");
        // help -> about
        MenuItem aboutMenuItem = new  MenuItem("About");
        helpMenu.getItems().addAll(aboutMenuItem);
        menubar.getMenus().addAll(fileMenu, helpMenu);


        // group
        Group group = new Group();

        // Canvas
        final Canvas canvas = new Canvas();
        canvas.setHeight(700);
        canvas.setWidth(1000);



        group.getChildren().addAll(canvas);

        // toolbar
        GridPane toolPalette = new GridPane();
        toolPalette.setVgap(10);

        // button tool
        ToggleGroup toolToggles = new ToggleGroup();
        ToggleButton penButton = new ToggleButton("pen");
        ToggleButton selectionButton = new ToggleButton("selection");
        ToggleButton pointButton = new ToggleButton("point");
        ToggleButton eraseButton = new ToggleButton("erase");
        toolToggles.getToggles().addAll(penButton, selectionButton, pointButton, eraseButton);
        penButton.setUserData(1);
        selectionButton.setUserData(2);
        pointButton.setUserData(3);
        eraseButton.setUserData(4);



        toolPalette.add(penButton, 0, 0);
        toolPalette.add(selectionButton, 0, 1);
        toolPalette.add(pointButton, 0, 2);
        toolPalette.add(eraseButton, 0, 3);
        ToolBar toolPaletteBar = new ToolBar(toolPalette);

        toolToggles.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == null) {
                    toolToggleInt = 0;
                } else {
                    toolToggleInt = (int) newValue.getUserData();
                }

                if(toolToggleInt != 1) {
                    path = false;
                }
                if(isTest) {
                    System.out.println(toolToggleInt);
                }
            }
        });


        // colorpicker

        // mouse action

            group.setOnMouseClicked(e -> {
                // pen
                if(toolToggleInt == 1) {
                    if(path == false) {
                        if(isTest) {
                            System.out.println(path);
                        }
                        path = true;
                        Path tempPath = new Path();
                        currentPath = tempPath;

                        currentPath.setStrokeWidth(4);
                        group.getChildren().add(currentPath);
                        currentPath.getElements().add(new MoveTo(e.getX(), e.getY()));

                        currentPath.setOnMouseClicked(e1 -> {
                            if(toolToggleInt == 4) {
                                deletePath(tempPath, group);
                            }

                        });

                    } else {
                        currentPath.getElements().add(new LineTo(e.getX(), e.getY()));
                    }
                }
                e.consume();
            });


        VBox vBox = new VBox(toolPaletteBar);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        BackgroundFill backgroundFill =
                new BackgroundFill(
                        Color.LIGHTGRAY,
                        new CornerRadii(0),
                        new Insets(0)
                );
        Background background = new Background(backgroundFill);
        vBox.setBackground(background);
        vBox.setSpacing(10);


        // scene building
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menubar);
        borderPane.setCenter(group);
        borderPane.setLeft(vBox);

        stage.setTitle("Bezier Curve (hdum)");
        Scene scene = new Scene(borderPane);


        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                if(toolToggleInt == 1) {
                    path = false;
                }
            }
        });

        stage.setScene(scene);
        stage.show();

    }
}


