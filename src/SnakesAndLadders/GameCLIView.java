package SnakesAndLadders;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class GameCLIView extends Application implements Observer {

    private GameModel model;
    private GameCLIController controller;

    private final String playerOne = "P1";
    private final String playerTwo = "P2";

    private boolean diceEnabled = true;

    private ArrayList<String> board = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(args);
    }

    public void writeToBoard(String string, int xCoor, int yCoor){

        int arrayIndex = (2 * yCoor) + 1;   // Calculates board position relative to array position
        int charIndex = (xCoor * 10) + 6;   // Calculates character index in a row

        String tempLine;
        String newLine;

        int stringLen = string.length();

        tempLine = board.get(arrayIndex);
        newLine = tempLine.substring(0, charIndex) + string + tempLine.substring(charIndex + stringLen);
        board.set(arrayIndex, newLine);

    }

    public void placeJumpers(){         // Jumpers are a collective name for Snakes and Ladders - as they jump players around the board.
        ArrayList<Integer> xCoors = model.getJumpPosXCoor();
        ArrayList<Integer> yCoors = model.getJumpPosYCoor();
        ArrayList<Integer> jumpBy = model.getJumpBy();

        for (int i = 0; i < jumpBy.size(); i++) {
            String jumper;

            if (jumpBy.get(i) > 0){
                jumper = "  +" + jumpBy.get(i).toString();
                writeToBoard(jumper, xCoors.get(i), yCoors.get(i));
            } else if (jumpBy.get(i) < 0){
                jumper = "  " + jumpBy.get(i).toString();
                writeToBoard(jumper, xCoors.get(i), yCoors.get(i));
            } else {
                model.getErrors().add("Error in 'placeJumpers()'. Invalid jumpBy.get()");
            }
        }
    }

    public void placeCounter(){
        int xCoor = model.getPlayingPlayer().getCoordinates().getXCoordinate();
        int yCoor = model.getPlayingPlayer().getCoordinates().getYCoordinate();

        if (model.getPlayingPlayer().getId() == 1){
            writeToBoard(playerOne, xCoor, yCoor);
        } else if(model.getPlayingPlayer().getId() == 2){
            writeToBoard(playerTwo, xCoor, yCoor);
        } else{
            model.getErrors().add("Error in 'placeCounter()'. Invalid model.getPlayingPlayer().getId().");
        }
        writeBoard();
    }

    public void removeCounter(){
        System.out.println("Playing player: " + model.getPlayingPlayer().getId());

        int xCoor = model.getPlayingPlayer().getCoordinates().getXCoordinate();
        int yCoor = model.getPlayingPlayer().getCoordinates().getYCoordinate();
        writeToBoard("  ", xCoor, yCoor);
    }

    public void readInput(){
        String option;
        Scanner scanner = new Scanner(System.in);
        option = scanner.next();
        switch (option){
            case "1":
                removeCounter();
                controller.rollDice();
                break;
            case "2":
                displayErrors();
                produceMenu();
                break;
            default:
                System.out.println("Please enter a valid option.");
                model.getErrors().add("Error in 'readInput()'. Invalid option entered by user. '" + option + "' was entered.");
                produceMenu();
        }

    }

    public void displayErrors(){        //Displays each of the errors in the model
        model.getErrors().forEach(System.out::println);
    }

    public void produceMenu(){          // Displays menu in command line to show menu options
        System.out.println(model.getPlayingPlayer().getName() + "s turn.");
        System.out.println("Total turns had: " + model.getTurnsHad());
        System.out.println("Enter '1' to roll dice. ");
        System.out.println("Enter '2' to display errors. ");
        System.out.println("Please enter your choice: ");
        readInput();
    }

    public void produceBoard(){     // Writes board as array of strings
        String line = "";
        int totalTiles = 100;
        int tile;
        String boardLine = "|---------|---------|---------";
        boardLine = boardLine.concat(boardLine + boardLine);
        for (int i = 0; i < 10; i++){
            board.add(" " + boardLine + "|---------|");
            for (int j = 0; j < 50; j++){
                line = line.concat(" ");
                if (j % 5 == 0){
                    line = line.concat("|");
                    if (i % 2 == 0){
                        tile = (i * 10) + (j/5);
                        tile = totalTiles - tile;
                        String formatTile = String.format("%03d", tile);
                        line = line.concat(formatTile);

                    } else{
                        tile = (i * 10) + (9 - (j/5));
                        tile = totalTiles - tile;
                        String formatTile = String.format("%03d", tile);
                        line = line.concat(formatTile);
                    }
                    line = line.concat("|");
                }
            }
            line = line.concat(" |");
            board.add(line);
            line = "";
        }
        board.add(" " + boardLine + "|---------|");

    }

    public void rollDiceOut(){
        System.out.println("***********************");
        if (model.getDiceRoll() == 0){
            System.out.println("Start Game");
        } else if (model.getDiceRoll() != 0){
            System.out.println(model.getPlayingPlayer().getName() + " has rolled " + model.getDiceRoll());
        } else{
            model.getErrors().add("Error in 'rollDiceOut()'. No 'diceRoll' result available. ");
        }
        System.out.println("***********************");
    }

    public void writeBoard(){
        System.out.println("|====================================================================================================|");
        board.forEach(System.out::println);
        rollDiceOut();
    }

    public void disableRoll(){
        diceEnabled = false;
        System.out.println("|==============|");
        System.out.println(model.getPlayingPlayer().getName() + " has won the game! ");
        System.out.println("|==============|");
    }

    @Override
    public void start(Stage stage) {
        model = new GameModel();
        controller = new GameCLIController(model, this);

        produceBoard();
        placeJumpers();

        writeToBoard(playerOne, 0, 9);



        model.addObserver(this);
        update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        rollDiceOut();
        placeCounter();
        controller.checkPlayersWon();
        if (diceEnabled){
            produceMenu();
        }
    }
}
