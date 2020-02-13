package SnakesAndLadders;

public class GameCLIController {
    private GameModel model;
    private GameCLIView view;

    public GameCLIController(GameModel model, GameCLIView view) {
        this.model = model;
        this.view = view;
    }

    public void rollDice(){
        model.rollDice();
    }

    public void disableRoll(){
        view.disableRoll();
    }

    public void checkPlayersWon(){
        if (model.getPlayingPlayer().getHasWon()){
            disableRoll();
        }
    }
}
