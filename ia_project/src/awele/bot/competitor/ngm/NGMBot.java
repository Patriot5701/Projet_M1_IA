package awele.bot.competitor.ngm;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class NGMBot extends CompetitorBot {

    /** Profondeur maximale */
    private static final int MAX_DEPTH = 8;

    /** Taille maximale de la table de transposition */
    private static final int MAX_SIZE_TT = 75;

    /**
     * @throws InvalidBotException
     */
    public NGMBot () throws InvalidBotException
    {
        this.setBotName ("NGM");
        this.addAuthor ("Anthony Munsch & Nicolas Vivier");
    }

    /**
     * Rien à faire
     */
    @Override
    public void initialize() {

    }

    /**
     * Pas d'apprentissage
     */
    @Override
    public void finish() {

    }

    /**
     * Sélection du coup selon l'algorithme NegaMax
     */
    @Override
    public double[] getDecision(Board board) {

        NGMNode.initialize (board, NGMBot.MAX_DEPTH, NGMBot.MAX_SIZE_TT);
        return new NGMNode(board).getDecision ();

    }

    /**
     * Rien à faire
     */
    @Override
    public void learn() {

    }
}
