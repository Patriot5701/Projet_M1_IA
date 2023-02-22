package awele.bot.demo.saligaud;

import awele.bot.DemoBot;
import awele.bot.onyvaawalp.MaxNodeAwalp;
import awele.bot.onyvaawalp.MinMaxNodeAwalp;
import awele.bot.onyvaawalp.OnYVaAwalp;
import awele.core.Board;
import awele.core.InvalidBotException;


/**
 * Idée : La profondeur change selon le moment dans la partie
 * --> légèrement + rapide, moins de coups, mais moins fort face aux gros
 */
public class Saligaud extends DemoBot {
    /** Profondeur maximale */
    private static final int MAX_DEPTH = 4;
    private static final int MAX_DEPTH_MID = 6;
    private int TURNS = 0;
    private int SEEDS = 48;
    private static final int START_MID_GAME = 4;    //en nb de tours depuis begin
    private static final int END_MID_GAME = 18;     //en nb de graines qui restent

    /**
     * @throws InvalidBotException
     */
    public Saligaud () throws InvalidBotException
    {
        this.setBotName ("Saligaud");
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
        this.TURNS++;
        this.SEEDS = board.getNbSeeds();
        if(this.TURNS <= this.START_MID_GAME || this.SEEDS<=this.END_MID_GAME){
            MinMaxSaligaud.initialize (board, Saligaud.MAX_DEPTH);
        }else{
            MinMaxSaligaud.initialize (board, Saligaud.MAX_DEPTH_MID);
        }
        return new MaxSaligaud(board).getDecision ();
    }

    @Override
    public void learn() {

    }
}
