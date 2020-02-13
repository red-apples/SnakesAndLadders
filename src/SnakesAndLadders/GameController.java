package SnakesAndLadders;

public class GameController{
    private GameModel model;
    private GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void rollDice(){
        model.rollDice();
    }

    public void checkPlayersWon(){
        if (model.getPlayingPlayer().getHasWon()){
            disableButtons();
        }
    }

    public void disableButtons(){
        view.disableRollDiceButton();
    }


}
