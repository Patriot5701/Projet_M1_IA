package awele.bot.demo.alphabeta;

import awele.bot.Bot;
import awele.bot.DemoBot;
import awele.bot.demo.random.RandomBot;
import awele.bot.onyvaawalp.OnYVaAwalp;
import awele.core.Awele;
import awele.core.Board;
import awele.core.InvalidBotException;

public class AlphaBeta extends DemoBot {
    /** Profondeur maximale */
    public static final int MAX_DEPTH = 6;

    /**
     * @throws InvalidBotException
     */
    public AlphaBeta () throws InvalidBotException
    {
        this.setBotName ("AlphaBeta");
        this.addAuthor ("Clement Lauer");
    }

    @Override
    public void initialize() {
    }

    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board){
        AlphaBetaNode.initialize (board, AlphaBeta.MAX_DEPTH);
        return new AlphaBetaNode(board, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, true).getDecision();
    }

    @Override
    public void learn() {
        try{
            Bot otherBot = (Bot) OnYVaAwalp.class.getConstructors()[0].newInstance();
            Awele awele = new Awele(this, otherBot);
            awele.play();
        }catch(Exception e){
            RandomBot random = null;
            try
            {
                random = new RandomBot ();
                random.learn ();
                Awele awele = new Awele(this, random);
                awele.play();
            }
            catch (Exception e2)
            {
            }
        }

    }

}
