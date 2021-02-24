import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class Main extends Application {
    // grid

    int xMax = 75;
    int yMax = 50;
    int space = 10;
    int[][] grid = new int[xMax][yMax];
    int[][] gridOld = new int[xMax][yMax];

    // button selection
    // 0: nothing 1: block 2: beehive 3: blinker 4: toad 5: glider 6: cancel
    int buttonSelected = 0;

    public void check(int i, int j) {
        int state = gridOld[i][j];
        int aliveCounter = 0;
        if(i - 1 >= 0) {
            if(j - 1 >= 0) {
                if(gridOld[i - 1][j - 1] == 1) {
                    aliveCounter++;
                }
            }
            if(gridOld[i - 1][j] == 1) {
                aliveCounter++;
            }
            if(j + 1 < yMax) {
                if(gridOld[i - 1][j + 1] == 1) {
                    aliveCounter++;
                }
            }
        }

        if(j - 1 >= 0) {
            if(gridOld[i][j - 1] == 1) {
                aliveCounter++;
            }
        }
        if(j + 1 < yMax) {
            if(gridOld[i][j + 1] == 1) {
                aliveCounter++;
            }
        }

        if(i + 1 < xMax) {
            if(j - 1 >= 0) {
                if(gridOld[i + 1][j - 1] == 1) {
                    aliveCounter++;
                }
            }
            if(gridOld[i + 1][j] == 1) {
                aliveCounter++;
            }
            if(j + 1 < yMax) {
                if(gridOld[i + 1][j + 1] == 1) {
                    aliveCounter++;
                }
            }
        }

        if(state == 1) {
            if(aliveCounter < 2 || aliveCounter > 3) {
                grid[i][j] = 0;
            } else {
                grid[i][j] = 1;
            }

        } else {
            if(aliveCounter == 3) {
                grid[i][j] = 1;
            } else {
                grid[i][j] = 0;
            }
        }
    }

    public void mutate() {
        for(int i = 0; i < xMax; i++) {
            for(int j = 0; j < yMax; j++) {
                gridOld[i][j] = grid[i][j];
            }
        }
        for(int i = 0; i < xMax; i++) {
            for(int j = 0; j < yMax; j++) {
                check(i, j);
            }
        }
    }


    public void draw(GraphicsContext gc) {
        for(int i = 0; i < xMax; i++) {
            for(int j = 0; j < yMax; j++) {
                if(grid[i][j] == 1) {
                    gc.fillRect(i * space, j * space, space, space);
                } else {
                    gc.clearRect(i * space + 1, j * space + 1, space - 2, space - 2);
                }

            }
        }
    }

    static int frameNum = 0;
    public void next(Label label, GraphicsContext gc) {

        // update the grid
        mutate();

        frameNum += 1;
        label.setText("Frame " + frameNum);


        draw(gc);

    }
    public Timeline timeline;





    @Override
    public void start(Stage stage) throws Exception {
/*        File f;
        f = new File(System.getProperty("user.dir"));
        System.out.println(f.getName());*/
//        FileInputStream blockFile = new FileInputStream (System.getProperty("user.dir") + "/resources/block.png");
       // Image blockImage = new Image(System.getProperty("user.dir")+"block.png");


        BorderPane borderPane = new BorderPane();
        GridPane toolBarButtons = new GridPane();
        Button block = new Button("Block");
        Button beehive = new Button("Beehive");
        Button blinker = new Button("Blinker");
        Button toad = new Button("Toad");
        Button glider = new Button("Glider");
        Button cancel = new Button("Cancel");



        toolBarButtons.add(block,0,0);
        toolBarButtons.add(beehive,1,0);
        toolBarButtons.add(blinker,2,0);
        toolBarButtons.add(toad,3,0);
        toolBarButtons.add(glider,4,0);
        toolBarButtons.add(cancel,5,0);

        // set gide vertical spacing
        toolBarButtons.setHgap(10);



        ToolBar toolBar = new ToolBar(toolBarButtons);
        Pane statusBar = new Pane();
        Label frame = new Label("Frame 0");

        statusBar.getChildren().add(frame);

        borderPane.setTop(toolBar);
        borderPane.setBottom(statusBar);
        final Canvas canvas = new Canvas();
        canvas.setHeight(500);
        canvas.setWidth(750);



        GraphicsContext gc = canvas.getGraphicsContext2D();
        int lineX = 0;
        int lineY = 0;
        gc.setStroke(Color.BLACK);
        while(lineX <= canvas.getHeight()) {
            gc.strokeLine(0, lineX, canvas.getWidth(), lineX);

            lineX += space;
        }

        while(lineY <= canvas.getWidth()) {
            gc.strokeLine(lineY, 0, lineY, canvas.getHeight());
            lineY += space;
        }

        for(int i = 0; i < xMax; i++) {
            for(int j = 0; j < yMax; j ++) {
                grid[i][j] = 1;
            }
        }

        draw(gc);


        for(int i = 0; i < xMax; i++) {
            for(int j = 0; j < yMax; j ++) {
                grid[i][j] = 0;
                gridOld[i][j] = 0;
            }
        }
        draw(gc);

        borderPane.setCenter(canvas);


        // timer
        EventHandler<ActionEvent> eventHandler = (now) -> next(frame, gc);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), eventHandler);
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // button clicked
        EventHandler<ActionEvent> blockClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("block selected");
                buttonSelected = 1;
            }
        };
        EventHandler<ActionEvent> beehiveClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("beehive selected");
                buttonSelected = 2;
            }
        };
        EventHandler<ActionEvent> blinkerClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("blinker selected");
                buttonSelected = 3;
            }
        };
        EventHandler<ActionEvent> toadClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("toad selected");
                buttonSelected = 4;
            }
        };
        EventHandler<ActionEvent> gliderClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("glider clicked");
                buttonSelected = 5;
            }
        };
        EventHandler<ActionEvent> cancelClicked = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                //System.out.println("cancel clicked");
                buttonSelected = 0;

                for(int i = 0; i < xMax; i++) {
                    for(int j = 0; j < yMax; j++) {
                        grid[i][j] = 0;
                    }
                }
                draw(gc);
            }
        };

        block.setOnAction(blockClicked);
        beehive.setOnAction(beehiveClicked);
        blinker.setOnAction(blinkerClicked);
        toad.setOnAction(toadClicked);
        glider.setOnAction(gliderClicked);
        cancel.setOnAction(cancelClicked);


        // canvas clicked (with button
        canvas.setOnMouseClicked(e -> {
            int xPos = (int) e.getX() / 10;
            int yPos = (int) e.getY() / 10;
            //System.out.println("canvas clicked (" + xPos + "," + yPos + ")");
            //System.out.println("with " + buttonSelected);

            // block
            if(buttonSelected == 1) {
                grid[xPos][yPos] = 1;
                if(xPos + 1 < xMax) {
                    grid[xPos + 1][yPos] = 1;
                }
                if(yPos + 1 < yMax) {
                    grid[xPos][yPos + 1] = 1;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 1] = 1;
                    }
                }
                draw(gc);
            }
            // beehive
            if(buttonSelected == 2) {
                grid[xPos][yPos] = 0;

                if(xPos + 1 < xMax) {
                    grid[xPos + 1][yPos] = 1;
                    if(xPos + 2 < xMax) {
                        grid[xPos + 2][yPos] = 1;
                        if(xPos + 3 < xMax) {
                            grid[xPos + 3][yPos] = 0;
                        }
                    }
                }
                if(yPos + 1 < yMax) {
                    grid[xPos][yPos + 1] = 1;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 1] = 0;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 1] = 0;
                            if(xPos + 3 < xMax) {
                                grid[xPos +3][yPos + 1] = 1;
                            }
                        }
                    }
                }

                if(yPos + 2 < yMax) {
                    grid[xPos][yPos + 2] = 0;

                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 2] = 1;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 2] = 1;
                            if(xPos + 3 < xMax) {
                                grid[xPos + 3][yPos + 2] = 0;
                            }
                        }
                    }
                }

                draw(gc);
            }

            //blinker
            if(buttonSelected == 3) {
                if(yPos + 1 < yMax) {
                    grid[xPos][yPos + 1] = 1;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 1] = 1;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 1] = 1;
                        }
                    }
                }
                draw(gc);
            }

            // toad
            if(buttonSelected == 4) {
                grid[xPos][yPos] = 0;
                if(xPos + 1 < xMax) {
                    grid[xPos + 1][yPos] = 1;
                    if(xPos + 2 < xMax) {
                        grid[xPos + 2][yPos] = 1;
                        if(xPos + 3 < xMax) {
                            grid[xPos + 3][yPos] = 1;
                        }
                    }
                }
                if(yPos + 1 < yMax) {
                    grid[xPos][yPos + 1] = 1;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 1] = 1;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 1] = 1;
                            if(xPos + 3 < xMax) {
                                grid[xPos + 3][yPos + 1] = 0;
                            }
                        }
                    }
                }
                draw(gc);
            }

            // glider
            if(buttonSelected == 5) {
                grid[xPos][yPos] = 0;
                if(xPos + 1 < xMax) {
                    grid[xPos + 1][yPos] = 0;
                    if(xPos + 2 < xMax) {
                        grid[xPos + 2][yPos] = 1;
                    }
                }
                if(yPos + 1 < yMax) {
                    grid[xPos][yPos + 1] = 1;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 1] = 0;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 1] = 1;
                        }
                    }
                }
                if(yPos + 2 < yMax) {
                    grid[xPos][yPos + 2] = 0;
                    if(xPos + 1 < xMax) {
                        grid[xPos + 1][yPos + 2] = 1;
                        if(xPos + 2 < xMax) {
                            grid[xPos + 2][yPos + 2] = 1;
                        }
                    }
                }


                draw(gc);
            }


            buttonSelected = 0;
            e.consume();
        });



        Scene scene = new Scene(borderPane);
        // add stuff to group here
        stage.setTitle("Conway's Game of Life (hdum)");
        stage.setScene(scene);
        stage.setResizable(false);





        stage.show();

    }
}
