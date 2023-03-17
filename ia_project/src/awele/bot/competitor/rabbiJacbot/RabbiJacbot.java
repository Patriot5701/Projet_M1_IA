package awele.bot.competitor.rabbiJacbot;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class RabbiJacbot extends CompetitorBot {
    /** Profondeur maximale */
    private static final byte MAX_DEPTH = 8;

    /** Taille maximale de la table de transposition */
    private static final byte MAX_SIZE_SAVE = 75;



    /**
     * @throws InvalidBotException
     */
    public RabbiJacbot () throws InvalidBotException
    {
        this.setBotName ("RabbiJacbot");
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
        MinMaxNodeRabbiJacbot.initialize (board, RabbiJacbot.MAX_DEPTH, RabbiJacbot.MAX_SIZE_SAVE);
        return new MaxNodeRabbiJacbot(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}
