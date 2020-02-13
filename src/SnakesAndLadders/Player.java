package SnakesAndLadders;

public class Player {
    private int id;
    private String playerName;
    private Coordinates coordinate;
    private boolean hasWon;

    public Player(String playerName, int id){
        this.playerName = playerName;
        this.id = id;
        coordinate = new Coordinates();
        hasWon = false;
    }

    public boolean getHasWon(){
        return hasWon;
    }

    public String getName(){
        return playerName;
    }

    public int getId(){
        return id;
    }

    public Coordinates getCoordinates(){
        return coordinate;
    }

    public void changeHasWon(boolean bool){
        hasWon = bool;
    }

    public void changePlayerCoordinates(int value){
        coordinate.changeCoordinates(value);
    }
}
