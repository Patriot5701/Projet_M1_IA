package awele.bot.demo.albatar;

import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * test heuristique numero 3 : nb de trous adverses avec moins de 3 graines + coef de consecutivité
 */
public class Albatar  extends DemoBot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public Albatar () throws InvalidBotException
    {
        this.setBotName ("Albatar");
        this.addAuthor ("Clément Lauer");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board) {
        MinMaxAlbatar.initialize (board, Albatar.MAX_DEPTH);
        return new MaxAlbatar(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}
