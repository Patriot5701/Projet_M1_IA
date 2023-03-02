package awele.bot.onyvaawalp;

import awele.bot.demo.alphabeta.AlphaBetaData;
import awele.core.Board;
import awele.core.InvalidBotException;

/**
 * @author Alexandre Blansché
 * Noeud d'un arbre MinMax
 */
public abstract class MinMaxNodeAwalp
{
    /** Numéro de joueur de l'IA */
    private static int player;

    /** Profondeur maximale */
    private static byte maxDepth;

    /** L'évaluation du noeud */
    private byte evaluation;

    /** Évaluation des coups selon MinMax */
    private byte [] decision;

    /**
     * Constructeur...
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     */
    public MinMaxNodeAwalp (Board board, byte depth, byte alpha, byte beta)
    {
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new byte [Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = this.worst ();


        /* On parcourt toutes les coups possibles */
        for (int i = 0; i < Board.NB_HOLES; i++)
            /* Si le coup est jouable */
            if (board.getPlayerHoles () [i] != 0)
            {
                /* Sélection du coup à jouer */
                byte [] decision = new byte [Board.NB_HOLES];
                decision [i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy = (Board) board.clone ();
                try
                {
                    int score = copy.playMoveSimulationScore (copy.getCurrentPlayer (), byteArrayToDoubleArray(decision));
                    copy = copy.playMoveSimulationBoard (copy.getCurrentPlayer (), byteArrayToDoubleArray(decision));
                    /* Si la nouvelle situation de jeu est un coup qui met fin à la partie,
                       on évalue la situation actuelle */
                    if ((score < 0) ||
                            (copy.getScore (Board.otherPlayer (copy.getCurrentPlayer ())) >= 25) ||
                            (copy.getNbSeeds () <= 6))
                        this.decision [i] = this.diffScore (copy);
                        /* Sinon, on explore les coups suivants */
                    else
                    {
                        /* Si la profondeur maximale n'est pas atteinte */
                        if (depth < MinMaxNodeAwalp.maxDepth)
                        {
                            /* On construit le noeud suivant */
                            MinMaxNodeAwalp child = this.getNextNode (copy, (byte) (depth + 1), alpha, beta);
                            /* On récupère l'évaluation du noeud fils */
                            this.decision [i] = child.getEvaluation ();
                        }
                        /* Sinon (si la profondeur maximale est atteinte), on évalue la situation actuelle */
                        else
                            this.decision [i] = this.diffScore(copy);
                    }
                    /* L'évaluation courante du noeud est mise à jour, selon le type de noeud (MinNode ou MaxNode) */
                    this.evaluation = this.minmax (this.decision [i], this.evaluation);

                    /* Coupe alpha-beta */
                    if (depth > 0)
                    {
                        alpha = this.alpha (this.evaluation, alpha);
                        beta = this.beta (this.evaluation, beta);
                    }
                }
                catch (InvalidBotException e)
                {
                    this.decision [i] = 0;
                }
            }
    }

    /** Pire score pour un joueur */
    protected abstract byte worst ();

    /**
     * Initialisation
     */
    protected static void initialize (Board board, int maxDepth)
    {
        MinMaxNodeAwalp.maxDepth = (byte) maxDepth;
        MinMaxNodeAwalp.player = board.getCurrentPlayer ();
    }

    private byte diffScore (Board board)
    {
        return (byte) (board.getScore (MinMaxNodeAwalp.player) - board.getScore (Board.otherPlayer (MinMaxNodeAwalp.player)));
    }

    /**
     * Mise à jour de alpha
     * @param evaluation L'évaluation courante du noeud
     * @param alpha L'ancienne valeur d'alpha
     * @return
     */
    protected abstract byte alpha (byte evaluation, byte alpha);

    /**
     * Mise à jour de beta
     * @param evaluation L'évaluation courante du noeud
     * @param beta L'ancienne valeur de beta
     * @return
     */
    protected abstract byte beta (byte evaluation, byte beta);

    /**
     * Retourne le min ou la max entre deux valeurs, selon le type de noeud (MinNode ou MaxNode)
     * @param eval1 Un double
     * @param eval2 Un autre double
     * @return Le min ou la max entre deux valeurs, selon le type de noeud
     */
    protected abstract byte minmax (byte eval1, byte eval2);

    /**
     * Indique s'il faut faire une coupe alpha-beta, selon le type de noeud (MinNode ou MaxNode)
     * @param eval L'évaluation courante du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     * @return Un booléen qui indique s'il faut faire une coupe alpha-beta
     */
    protected abstract boolean alphabeta (byte eval, byte alpha, byte beta);

    /**
     * Retourne un noeud (MinNode ou MaxNode) du niveau suivant
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     * @return Un noeud (MinNode ou MaxNode) du niveau suivant
     */
    protected abstract MinMaxNodeAwalp getNextNode (Board board, byte depth, byte alpha, byte beta);

    /**
     * L'évaluation du noeud
     * @return L'évaluation du noeud
     */
    byte getEvaluation ()
    {
        return this.evaluation;
    }

    /**
     * L'évaluation de chaque coup possible pour le noeud
     * @return
     */
    double [] getDecision ()
    {
        return byteArrayToDoubleArray(this.decision);
    }

    /*private double getKey(Board board){
        int[] playerHoles = board.getPlayerHoles();
        int[] opponentHoles = board.getOpponentHoles();

        double key = playerHoles[0] + 10*playerHoles[1] + 100*playerHoles[2] + 1000*playerHoles[3] + 10000*playerHoles[4] + 100000*playerHoles[5] + 1000000*opponentHoles[0] + 10000000*opponentHoles[1] + 100000000*opponentHoles[2] + 1000000000*opponentHoles[3] + 10000000000.0*opponentHoles[4] + 100000000000.0*opponentHoles[5];

        return key;
    }*/

    private String getKey(Board board){
        String key = "";
        int[] playerHoles = board.getPlayerHoles();
        int[] opponentHoles = board.getOpponentHoles();

        for(int i = 0; i<Board.NB_HOLES;i++){
            key+=playerHoles[i];
        }
        for(int i = 0; i<Board.NB_HOLES;i++){
            key+=opponentHoles[i];
        }

        return key;
    }


    protected static double[] byteArrayToDoubleArray(byte[] bytes){
        double[] array = new double[bytes.length];
        for(int i = 0; i<bytes.length;i++) {
            array[i] = bytes[i];
        }
        return array;
    }


}
