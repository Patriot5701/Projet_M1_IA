package awele.bot.demo.houlahoula;

import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class Houlahoula extends DemoBot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public Houlahoula () throws InvalidBotException
    {
        this.setBotName ("Houlahoula");
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
        MinMaxHoulahoula.initialize (board, Houlahoula.MAX_DEPTH);
        return new MaxHoulahoula(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}

