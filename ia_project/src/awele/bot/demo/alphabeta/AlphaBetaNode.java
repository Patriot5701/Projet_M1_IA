package awele.bot.demo.alphabeta;

import awele.bot.onyvaawalp.MinMaxNodeAwalp;
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
                    /* Coupe alpha-beta */
                    if (depth > 0)
                    {
                        if(isMax){
                            alpha = this.alpha(this.evaluation, alpha);
                        }else{
                            beta = this.beta(this.evaluation, beta);
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
        if(isMax){
            return Math.max (decision, evaluation);
        }else{
            return Math.min(decision, evaluation);
        }
    }

    private int alpha(int evaluation, int alpha){
        return Math.max (evaluation, alpha);
    }

    private int beta(int evaluation, int beta){
        return Math.min (evaluation, beta);
    }

    private int worst(boolean isMax){
        if(isMax){
            return -Integer.MAX_VALUE;
        }else {
            return Integer.MAX_VALUE;
        }
    }
    private int heuristique (Board board)
    {
        return (board.getScore (AlphaBetaNode.player) - board.getScore (Board.otherPlayer (AlphaBetaNode.player)));
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
        AlphaBetaNode.maxDepth = maxDepth;
        AlphaBetaNode.player = board.getCurrentPlayer ();
    }

    private double[] intArrayToDoubleArray(int[] ints){
        double[] array = new double[ints.length];
        for(int i = 0; i<ints.length;i++) {
            array[i] = ints[i];
        }
        return array;
    }

}
