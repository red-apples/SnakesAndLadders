package SnakesAndLadders;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel model;

    @org.junit.jupiter.api.Test
    void validateCorrectFileFormat(){
        model = new GameModel();

        model.readFile("src/SnakesAndLadders/BoardSetup.txt");
        ArrayList<Integer> actualList = model.getJumpPositions();

        ArrayList<Integer> expectedList = new ArrayList<>();
        expectedList.add(04+99);
        expectedList.add(44+99);

        assertEquals(expectedList, actualList, "Validation of file is incorrect. ");
    }

    @org.junit.jupiter.api.Test
    void validateIncorrectFileFormat(){
        model = new GameModel();

        model.readFile("src/SnakesAndLadders/BoardSetupInvalid.txt");
        ArrayList<Integer> actualList = model.getJumpPositions();

        ArrayList<Integer> expectedList = new ArrayList<>();
        expectedList.add(04+99);
        expectedList.add(45+99);

        assertEquals(expectedList, actualList, "Validation of file is incorrect. ");
    }

    @org.junit.jupiter.api.Test
    void validateFileFound(){
        model = new GameModel();

        model.readFile("src/SnakesAndLadders/doesntExist.txt");
        String expectedErrorMessage = "Error in readFile(). File not found. ";
        String actualErrorMessage = (String) model.getErrors().get(model.getErrors().size() - 1);
        assertEquals(expectedErrorMessage, actualErrorMessage, "Error message has not been correctly logged. ");
    }

    @org.junit.jupiter.api.Test
    void validateFilesNumberOfJumpers(){
        model = new GameModel();

        model.readFile("src/SnakesAndLadders/BoardSetupInvalid2.txt");
        String expectedErrorMessage = "Error in 'checkNumberOfJumpers'. Snakes and ladders are not 2 or more, or less. ";
        String actualErrorMessage = (String) model.getErrors().get(model.getErrors().size() - 1);
        assertEquals(expectedErrorMessage, actualErrorMessage, "Error message has not been correctly logged. ");
    }

}
