package SnakesAndLadders;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GameView extends Application implements Observer {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;

    private final Button rollDice = new Button("Roll Die");

    private final Label diceResult = new Label();
    private final Label playerOne = new Label("Player One");
    private final Label playerTwo = new Label("Player Two");
    private final Label gameWon = new Label();
    private final Label errorDisplay = new Label();


    private Color boardColourLB = Color.web("0x3BA3D0",1.0);
    private Color boardColourBlue = Color.web("0x04859D",1.0);
    private Color plyOneColourLO = Color.web("0xFFA540",1.0);
    private Color plyTwoColourDO = Color.web("0xFF8700",1.0);
    private Color snakeColour = Color.web("0xFFD773",1.0);
    private Color ladderColour = Color.web("0xFFC940",1.0);


    private final Rectangle counterOne = new Rectangle(20, 20, plyOneColourLO);
    private final Rectangle counterTwo = new Rectangle(20, 20, plyTwoColourDO);

    private GridPane board = produceBoardPane();


    private GameModel model;
    private GameController controller;

    private String errorMsg;

    public GridPane produceBoardPane() {
        GridPane gridPane = new GridPane();

        int SIZE = 10;
        int length = SIZE;
        int width = SIZE;
        int totalTiles = 100;
        int tileNum;

        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {

                Label tile = new Label();
                tile.setMinHeight(50);
                tile.setMinWidth(50);
                tile.setAlignment(Pos.BOTTOM_LEFT);

                if (y % 2 == 0) {
                    tileNum = (y * 10) + x;
                    tileNum = totalTiles - tileNum;
                } else {
                    tileNum = (y * 10) + (9 - x);
                    tileNum = totalTiles - tileNum;
                }
                if (tileNum % 2 == 0) {
                    tile.setBackground(new Background(new BackgroundFill(boardColourBlue, null, null)));
                } else {
                    tile.setBackground(new Background(new BackgroundFill(boardColourLB, null, null)));
                }

                tile.setTextFill(Color.WHITE);
                tile.setFont(Font.font("Calibri", 15));
                tile.setText(Integer.toString(tileNum));


                // Iterate the Index using the loops
                gridPane.setRowIndex(tile, y);
                gridPane.setColumnIndex(tile, x);
                gridPane.getChildren().add(tile);
            }
        }

        return gridPane;

    }


    public void placeCounter(){
        if (model.getPlayingPlayer().getId() == 1){
            board.getChildren().remove(counterOne);
            board.add(counterOne, model.getPlayer1().getCoordinates().getXCoordinate(),  model.getPlayer1().getCoordinates().getYCoordinate());
        }else if(model.getPlayingPlayer().getId() == 2){
            board.getChildren().remove(counterTwo);
            board.add(counterTwo, model.getPlayer2().getCoordinates().getXCoordinate(),  model.getPlayer2().getCoordinates().getYCoordinate());
        }else{
            model.getErrors().add("Error in 'placeCounter()'. Invalid model.getPlayingPlayer().getId().");
        }

    }

    public void placeJumpers(){
        ArrayList<Integer> xCoors = model.getJumpPosXCoor();
        ArrayList<Integer> yCoors = model.getJumpPosYCoor();
        ArrayList<Integer> jumpBy = model.getJumpBy();

        for (int i = 0; i < jumpBy.size(); i++) {
            Label lb = new Label();

            lb.setTextFill(Color.WHITE);
            lb.setMinWidth(20);
            lb.setMinHeight(20);

            if (jumpBy.get(i) > 0){

                lb.setText("+" + jumpBy.get(i).toString());
                lb.setBackground(new Background(new BackgroundFill(ladderColour, null, null)));
            } else if (jumpBy.get(i) < 0){
                lb.setText(jumpBy.get(i).toString());
                lb.setBackground(new Background(new BackgroundFill(snakeColour, null, null)));
            } else {
                model.getErrors().add("Error in 'placeJumpers()'. 'jumpBy.get(i)' returned invalid option. ");
            }
            board.add(lb, xCoors.get(i), yCoors.get(i));
            board.setValignment(lb, VPos.TOP);
            board.setHalignment(lb, HPos.RIGHT);
        }

    }

    public VBox buttons(){
        VBox vb = new VBox();

        playerOne.setBackground(new Background( new BackgroundFill(plyOneColourLO, null, null)));
        playerTwo.setBackground(new Background( new BackgroundFill(plyTwoColourDO, null, null)));

        playerOne.setAlignment(Pos.CENTER);
        playerTwo.setAlignment(Pos.CENTER);
        diceResult.setAlignment(Pos.CENTER);

        playerOne.setMinWidth((WINDOW_WIDTH/7) * 2);
        playerOne.setMinHeight(WINDOW_HEIGHT/20);
        playerTwo.setMinWidth((WINDOW_WIDTH/7) * 2);
        playerTwo.setMinHeight(WINDOW_HEIGHT/20);

        rollDice.setMinWidth((WINDOW_WIDTH/7) * 2);
        diceResult.setMinWidth((WINDOW_WIDTH/7) * 2);

        playerOne.setFont(Font.font("Calibri", 15));
        playerOne.setTextFill(Color.WHITE);
        playerTwo.setFont(Font.font("Calibri", 15));
        playerTwo.setTextFill(Color.WHITE);

        rollDice.setFont(Font.font("Calibri", 15));
        diceResult.setFont(Font.font("Calibri", 15));

        gameWon.setMinWidth((WINDOW_WIDTH/7) * 2);
        gameWon.setAlignment(Pos.CENTER);
        gameWon.setFont(Font.font("Calibri", 15));


        errorDisplay.setMinWidth((WINDOW_WIDTH/7) * 2);
        errorDisplay.setMinWidth(WINDOW_HEIGHT/3);
        errorDisplay.setAlignment(Pos.CENTER);
        errorDisplay.setFont(Font.font("Calibri", 15));

        ScrollPane sc = new ScrollPane();
        sc.setContent(errorDisplay);

        vb.getChildren().addAll(playerOne, playerTwo, rollDice, diceResult, gameWon, sc);


        rollDice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.rollDice();
            }
        });

        return vb;

    }

    public void disableRollDiceButton(){
        rollDice.setDisable(true);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        model = new GameModel();
        controller = new GameController(model, this);

        HBox root = new HBox();
        VBox buttonsBox = buttons();

        root.getChildren().addAll(board, buttonsBox);

        board.add(counterTwo, model.getPlayer2().getCoordinates().getXCoordinate(),  model.getPlayer2().getCoordinates().getYCoordinate());

        placeJumpers();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

        model.addObserver(this);
        update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        diceResult.setText(Integer.toString(model.getDiceRoll()));
        placeCounter();
        controller.checkPlayersWon();
        if(model.getPlayingPlayer().getHasWon()){
            gameWon.setText(model.getPlayingPlayer().getName() + " has won!");
        }
        if(model.getErrors().isEmpty() == false){
            errorMsg = "";
            for (int i = 0; i < model.getErrors().size(); i++){
                errorMsg = errorMsg.concat((String) model.getErrors().get(i));
            }
            errorDisplay.setText(errorMsg);
        }


    }

}
