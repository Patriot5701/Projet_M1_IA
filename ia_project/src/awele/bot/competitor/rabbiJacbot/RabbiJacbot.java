package awele.bot.competitor.rabbiJacbot;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Clement Lauer
 * @author Bastien Pazzaglia
 * Bot Rabbi JacBot
 */
public class RabbiJacbot extends CompetitorBot {
    /** Profondeur de base */
    private static final byte MAX_DEPTH = 8;

    /** Taille maximale de la table de stockage */
    private static final byte MAX_SIZE_SAVE = 70;



    /**
     * @throws InvalidBotException
     */
    public RabbiJacbot () throws InvalidBotException
    {
        this.setBotName ("RabbiJacbot");
        this.addAuthor ("Clément Lauer");
        this.addAuthor ("Bastien Pazzaglia");
    }

    /**
     * Méthode d'initialisation vide
     */
    @Override
    public void initialize() {

    }

    /**
     * Méthode de fin vide
     */
    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board) {
        MinMaxNodeRabbiJacbot.initialize (board, RabbiJacbot.MAX_DEPTH, RabbiJacbot.MAX_SIZE_SAVE);
        return new MaxNodeRabbiJacbot(board).getDecision ();
    }

    /**
     * Méthode d'apprentissage vide
     */
    @Override
    public void learn() {

    }
}
