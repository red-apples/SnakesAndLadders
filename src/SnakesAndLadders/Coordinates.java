package SnakesAndLadders;

import java.util.ConcurrentModificationException;

public class Coordinates {
    private int position;
    private int xCoordinate;
    private int yCoordinate;

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getPosition() {
        return position;
    }

    public Coordinates(){
        initialiseCoordinates();
    }

    public void initialiseCoordinates(){
        xCoordinate = 0;
        yCoordinate = 9;
        position = 100;
    }

    public int checkPosition(int position, int newPosition){
        if (newPosition < 199){
            return newPosition;
        }else if(newPosition == 199){
            return newPosition;
        }else if(newPosition > 199){            // If new position is larger than board, maintains the same position.
            return position;
        }else{
            throw new ConcurrentModificationException();
        }
    }

    public void changeCoordinates(int value){
        /** @pre. value must not be zero
         *  @post. value is sent to players coordinates
         */
        assert(value != 0): "Violation! Value is equal to '0'. ";
        int newPosition;
        newPosition = position + value;
        position = checkPosition(position, newPosition);
        xCoordinate = position % 10;

        yCoordinate = 19 - (position / 10);
        if (yCoordinate % 2 == 0) {
            xCoordinate = 9 - (position % 10);
        } else if(yCoordinate % 2 == 1){
            xCoordinate = position % 10;
        }
    }
}
