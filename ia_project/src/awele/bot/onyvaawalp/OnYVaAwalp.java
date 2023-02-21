package awele.bot.onyvaawalp;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class OnYVaAwalp extends CompetitorBot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public OnYVaAwalp () throws InvalidBotException
    {
        this.setBotName ("OnYVaAwalp");
        this.addAuthor ("Clément Lauer");
        this.addAuthor ("Bastien Pazzaglia");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board) {
        MinMaxNodeAwalp.initialize (board, OnYVaAwalp.MAX_DEPTH);
        return new MaxNodeAwalp(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}
