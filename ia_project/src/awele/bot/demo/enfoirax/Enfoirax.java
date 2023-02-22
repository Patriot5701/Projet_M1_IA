package awele.bot.demo.enfoirax;

import awele.bot.DemoBot;
import awele.bot.demo.saligaud.MaxSaligaud;
import awele.bot.demo.saligaud.MinMaxSaligaud;
import awele.core.Board;
import awele.core.InvalidBotException;


/**
 * Test de l'ajout de prises de décisions fatales
 * Légèrement + rapide, meilleure que celle du prof
 */
public class Enfoirax extends DemoBot {

    private EnfoiraxCategories data;

    /** Profondeur maximale */
    private static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public Enfoirax () throws InvalidBotException
    {
        this.setBotName ("Enfoirax");
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
        MinMaxEnfoirax.initialize (board, Enfoirax.MAX_DEPTH);
        return new MaxEnfoirax(board).getDecision ();
    }

    @Override
    public void learn() {
        this.data = new EnfoiraxCategories();
    }
}
