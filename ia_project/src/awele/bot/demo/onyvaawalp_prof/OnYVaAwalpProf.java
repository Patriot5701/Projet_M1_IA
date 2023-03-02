package awele.bot.demo.onyvaawalp_prof;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class OnYVaAwalpProf extends CompetitorBot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public OnYVaAwalpProf () throws InvalidBotException
    {
        this.setBotName ("OnYVaAwalpProf");
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
        MinMaxNodeAwalpProf.initialize (board, OnYVaAwalpProf.MAX_DEPTH);
        return new MaxNodeAwalpProf(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}
