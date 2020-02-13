package SnakesAndLadders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GameModel extends Observable {
    private int diceRoll;
    private int turnsHad;

    private Player player1;
    private Player player2;
    private Player playingPlayer;

    private BufferedReader file;
    private ArrayList<Integer> jumpPositions = new ArrayList<>();
    private ArrayList<Integer> jumpPosXCoor = new ArrayList<>();
    private ArrayList<Integer> jumpPosYCoor = new ArrayList<>();
    private ArrayList<Integer> jumpBy = new ArrayList<>();

    private ArrayList<String> errors = new ArrayList<>();

    public int getDiceRoll(){
        return diceRoll;
    }

    public int getTurnsHad(){
        return turnsHad;
    }

    public Player getPlayingPlayer(){
        return playingPlayer;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    public ArrayList getJumpBy(){
        return jumpBy;
    }

    public ArrayList getJumpPosXCoor(){
        return jumpPosXCoor;
    }

    public ArrayList getJumpPosYCoor(){
        return jumpPosYCoor;
    }

    public ArrayList getJumpPositions(){
        return jumpPositions;
    }

    public ArrayList getErrors(){
        return errors;
    }

    public void checkPlayerWin(){
        if (getPlayingPlayer().getCoordinates().getPosition() == 199){
            getPlayingPlayer().changeHasWon(true);
        }
    }

    public void checkPosition(){
        checkPlayerWin();
        if(jumpPositions.contains(playingPlayer.getCoordinates().getPosition())){
            int index = jumpPositions.indexOf(playingPlayer.getCoordinates().getPosition());
            int value = jumpBy.get(index);
            addDiceResult(playingPlayer, value);
        }
    }

    public void rollDice(){
        playerTurn();
        Random rand = new Random();
        diceRoll = rand.nextInt(6) + 1; // Obtain a number between [1 - 6]
        addDiceResult(playingPlayer, diceRoll);
        checkPosition();
        turnsHad++;
        setChanged();
        notifyObservers();
    }

    public void addDiceResult(Player player, int value){
        /** @pre. player must not be null
         *  @pre. value must not be zero
         *  @post. value is sent to players coordinates
         */
        assert(player != null && value != 0): "Violation! A parameter is empty. ";
        player.changePlayerCoordinates(value);

    }

    public void playerTurn() {
        if (turnsHad % 2 == 0){
            playingPlayer = getPlayer1();
        }else if (turnsHad % 2 == 1){
            playingPlayer = getPlayer2();
        } else{
            errors.add("Error in playerTurn(). 'turnsHad' 2 cannot modulus 'turnsHad'. ");
        }
    }

    public void addJumpersCoor(int position){
        /** @pre. jumpers position must be between 1 - 99
         *  @post. coordinates produced for jumpers
         */
        assert(1 < position && position < 99): "Violation! A parameter is empty. ";
        int xCoor;
        int yCoor;
        position = position + 99;

        xCoor = position % 10;

        yCoor = 19 - (position / 10);
        if (yCoor % 2 == 0) {
            xCoor = 9 - (position % 10);
        }
        else if(yCoor % 2 == 1){
            xCoor = position % 10;
        }else{
            errors.add("Error in 'addJumperCoor'. Invalid modulus 2 of coordinate. ");
        }

        jumpPosXCoor.add(xCoor);
        jumpPosYCoor.add(yCoor);
    }

    public void createPlayers(){
        player1 = new Player("Player1", 1);
        player1.getCoordinates().initialiseCoordinates();
        player2 = new Player("Player2", 2);
        player2.getCoordinates().initialiseCoordinates();
    }

    public void readFile(String fileName){
        /** @pre. file must exist
         *   @post. snakes and ladders are inputted into jumper array
         */
        try {
            file = new BufferedReader(new FileReader(fileName));       // read hardcoded file
            String line;
            while((line= file.readLine())!= null){
                if(validateFileFormat(line)){
                    String [] res = line.split(",");
                    jumpPositions.add(Integer.parseInt(res[0]) + 99);
                    jumpBy.add(Integer.parseInt(res[1]));
                    addJumpersCoor(Integer.parseInt(res[0]));
                }
            }
            file.close();
            if(checkNumberOfJumpers() == false){
                errors.add("Error in 'checkNumberOfJumpers'. Snakes and ladders are not 2 or more, or less. ");
            }
        } catch (FileNotFoundException ex) {
            errors.add("Error in readFile(). File not found. ");
//            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            errors.add("Error in readFile(). IOException. ");
//            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkNumberOfJumpers(){
        int nSnakes = 0;
        int nLadders = 0;
        for (int i = 0; i < jumpBy.size(); i++){
            if (jumpBy.get(i) < 0 ){
                nSnakes++;
            } else if (jumpBy.get(i) > 0){
                nLadders++;
            } else{
                errors.add("Error in 'checkNumberOfJumpers()'. jumpBy is not less than zero or greater than zero. ");
            }
        }
        if (nSnakes < 2 || nLadders < 2){
            return false;
        } else if (nSnakes > 2 && nLadders > 2){
            return true;
        } else{

            return false;
        }
    }

    private boolean validateFileFormat(String line){
        if (line.isEmpty()){                                                    // Checks that line is not null
            errors.add("Error in validating file format. Text file line is empty. ");
            return false;
        }
        String [] res = line.split(",");
        int totalSum = Integer.parseInt(res[0]) + Integer.parseInt(res[1]);

        if (line.length() > 6){                                                // Checks the length of the string is less than 5
            errors.add("Error in validating file format. Text file line is too long. ");
            return false;
        }
        if (line.length() < 6){
            errors.add("Error in validating file format. Text file line is too short. ");
        }
        if (Integer.parseInt(res[0]) >= 99 || Integer.parseInt(res[0]) <= 0){   // Checks the size of the position
            errors.add("Error in validating file format. Position is not on board. ");
            return false;
        }
        if (Integer.parseInt(res[1]) >= 99 || Integer.parseInt(res[1]) <= -99){ // Checks the size of the jumper
            errors.add("Error in validating file format. Snake or ladder is the incorrect size. ");
            return false;
        }
        if (totalSum > 99 || totalSum < 1){                                     // Checks sum of position and jumper
            errors.add("Error in validating file format. Position plus snake or ladder, give position not on the board.  ");
            return false;
        }
        if (jumpPositions.contains(Integer.parseInt(res[0]) + 99)){             // Checks the position hasnt been used before
            errors.add("Error in validating file format. There is already a snake or ladder on this position. ");
            return false;
        }
        if (jumpPositions.contains(totalSum + 99)){                             // Checks there is no jumper at top of jump
            errors.add("Error in validating file format. There is already a snake or ladder where this snake or ladder ends. ");
            return false;
        }
        for (int i = 0; i < jumpPositions.size(); i++){
            if ((jumpPositions.get(i) + jumpBy.get(i)) == (Integer.parseInt(res[0]) + 99)){
                return false;
            }
        }

        return true;
    }

    public GameModel(){
        readFile("src/SnakesAndLadders/BoardSetup.txt");
        createPlayers();
        playerTurn();

    }

}
