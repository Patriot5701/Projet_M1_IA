package awele.bot.demo.alphabeta;

import awele.core.Board;
import awele.core.InvalidBotException;

public class AlphaBetaNode {
    private int [] decision;
    private int evaluation;
    /** Numéro de joueur de l'IA */
    private static int player;

    /** Profondeur maximale */
    private static int maxDepth;

    public AlphaBetaNode(Board board, int depth, int alpha, int beta, boolean isMax){
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new int [Board.NB_HOLES];
        /* initialisation de l'état courant */
        this.evaluation = worst(isMax);

        String key = getKey(board);
        // Check the transposition table for an existing evaluation
        if (AlphaBetaData.getInstance().getTranspositionTable().containsKey(key)) {
            this.evaluation = AlphaBetaData.getInstance().getTranspositionTable().get(key);
            return;
        }

        /* On parcourt toutes les coups possibles */
        for(int i = 0; i<Board.NB_HOLES; i++) {
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                int [] decision = new int [Board.NB_HOLES];
                decision [i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy = (Board) board.clone ();
                try{
                    int score = copy.playMoveSimulationScore (copy.getCurrentPlayer (), intArrayToDoubleArray(decision));
                    copy = copy.playMoveSimulationBoard (copy.getCurrentPlayer (), intArrayToDoubleArray(decision));
                    /* Si la nouvelle situation de jeu est un coup qui met fin à la partie,
                       on évalue la situation actuelle */
                    if ((score < 0) ||
                            (copy.getScore (Board.otherPlayer (copy.getCurrentPlayer ())) >= 25) ||
                            (copy.getNbSeeds () <= 6)) {
                        this.decision[i] = this.heuristique(copy);
                    }else{
                        if(depth < AlphaBeta.MAX_DEPTH){
                            if(isMax){
                                /* On construit le noeud suivant */
                                AlphaBetaNode child = new AlphaBetaNode(copy, (depth + 1), alpha, beta, false);
                                /* On récupère l'évaluation du noeud fils */
                                this.decision [i] = child.getEvaluation ();
                            }else{
                                /* On construit le noeud suivant */
                                AlphaBetaNode child = new AlphaBetaNode(copy, (depth + 1), alpha, beta, true);
                                /* On récupère l'évaluation du noeud fils */
                                this.decision [i] = child.getEvaluation ();
                            }
                        }else{
                            this.decision [i] = this.heuristique(copy);
                        }
                    }
                    /* L'évaluation courante du noeud est mise à jour, selon le type de noeud (MinNode ou MaxNode) */
                    this.evaluation = this.minmax(this.decision [i], this.evaluation, isMax);

                    AlphaBetaData.getInstance().put(key, this.evaluation);


                    /* Coupe alpha-beta */
                    if (depth > 0)
                    {
                        if(isMax){
                            alpha = (evaluation >= alpha) ? evaluation : alpha;
                        }else{
                            beta = (evaluation <= beta) ? evaluation : beta;
                        }
                    }
                }
                catch(InvalidBotException e){
                    this.decision [i] = 0;
                }
            }
        }
    }

    private int minmax(int decision, int evaluation, boolean isMax){
        return isMax ? Math.max(decision, evaluation) : Math.min(decision, evaluation);
    }

    private int worst(boolean isMax){
        return isMax ? -Integer.MAX_VALUE : Integer.MAX_VALUE;
    }
    private int heuristique (Board board)
    {
        return (board.getScore (awele.bot.demo.alphabeta.AlphaBetaNode.player) - board.getScore (Board.otherPlayer (awele.bot.demo.alphabeta.AlphaBetaNode.player)));
    }

    private String getKey(Board board){
        String key = "";
        for(int i = 0; i<board.getPlayerHoles().length;i++){
            key+=board.getPlayerHoles()[i];
            key+=board.getOpponentHoles()[i];
        }
        return key;
    }



    public double[] getDecision(){
        return intArrayToDoubleArray(this.decision);
    }

    public int getEvaluation(){
        return this.evaluation;
    }

    /**
     * Initialisation
     */
    protected static void initialize (Board board, int maxDepth)
    {
        awele.bot.demo.alphabeta.AlphaBetaNode.maxDepth = maxDepth;
        awele.bot.demo.alphabeta.AlphaBetaNode.player = board.getCurrentPlayer ();
    }

    private static double[] intArrayToDoubleArray(int[] ints){
        double[] array = new double[ints.length];
        for(int i = 0; i<ints.length;i++) {
            array[i] = ints[i];
        }
        return array;
    }


}
