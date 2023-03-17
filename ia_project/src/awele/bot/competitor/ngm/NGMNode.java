package awele.bot.competitor.ngm;

import awele.core.Board;
import awele.core.InvalidBotException;
import java.util.HashMap;

/**
 * Noeud d'un arbre NegaMax
 */
public class NGMNode
{
    /** Numéro de joueur de l'IA */
    private static int player;

    /** Profondeur maximale */
    private static int maxDepth;

    /** Table de transposition qui stock la valeur de certains noeuds */
    private static HashMap<Board, TranspositionTableEntry> transpoTable;

    /** Taille maximum de la table de transposition */
    private static int maxSizeTT;

    /** L'évaluation du noeud */
    private double evaluation;

    /** Évaluation des coups selon NegaMax */
    private double [] decision;

    /**
     * Constructeur pour un noeud initial
     * @param board La situation de jeu pour laquelle il faut prendre une décision
     */
    public NGMNode (Board board)
    {
        this (board, NGMNode.maxDepth, 1, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    /**
     * Constructeur...
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param color La couleur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     */
    public NGMNode (Board board, int depth, int color, double alpha, double beta)
    {
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new double [Board.NB_HOLES];

        /* Initialisation de l'évaluation courante */
        this.evaluation = -Double.MAX_VALUE;

        double alphaOrig = alpha;

        /* On parcourt toutes les coups possibles */
        for (int i = 0; i < Board.NB_HOLES; i++)
            /* Si le coup est jouable */
            if (board.getPlayerHoles () [i] != 0)
            {
                /* Sélection du coup à jouer */
                double [] decision = new double [Board.NB_HOLES];
                decision [i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy = (Board) board.clone ();
                try
                {
                    int score = copy.playMoveSimulationScore (copy.getCurrentPlayer (), decision);
                    copy = copy.playMoveSimulationBoard (copy.getCurrentPlayer (), decision);

                    /* On test l'appartenance du coup dans la table de transposition */
                    TranspositionTableEntry ttEntry = NGMNode.transpoTable.get(copy);

                    /* Si le coup existe dans la table de transposition, on vérifie s'il est exploitable */
                    if(ttEntry != null && ttEntry.depth >= depth){
                        if(ttEntry.flag == TranspositionTableEntry.EXACT){
                            this.decision[i] = ttEntry.value;
                            this.evaluation = ttEntry.value;
                            break;
                        }
                        else if(ttEntry.flag == TranspositionTableEntry.LOWERBOUND)
                            alpha = Math.max(alpha, ttEntry.value);
                        else if(ttEntry.flag == TranspositionTableEntry.UPPERBOUND)
                            beta = Math.min(beta, ttEntry.value);

                        if(alpha >= beta){
                            this.decision[i] = ttEntry.value;
                            this.evaluation = ttEntry.value;
                            break;
                        }

                    }

                    /*
                    Si l'exploitation de la table de transposition n'a pas été concluante, on déroule NegaMax :
                    Si la nouvelle situation de jeu est un coup qui met fin à la partie ou si on a atteint la profondeur maximale, on évalue la situation actuelle
                    */
                    if ((score < 0) ||
                            (copy.getScore (Board.otherPlayer (copy.getCurrentPlayer ())) >= 25) ||
                            (copy.getNbSeeds () <= 6)||
                            depth == 0)
                        this.decision [i] = color * this.diffScore (copy);
                        /* Sinon, on explore les coups suivants */
                    else
                    {
                            /* On construit le noeud suivant */
                            NGMNode child = this.getNextNode (copy, depth - 1, -color,  -beta, -alpha);
                            /* On récupère l'évaluation du noeud fils */
                            this.decision [i] = -child.getEvaluation ();
                    }
                    /* L'évaluation courante du noeud est mise à jour */
                    this.evaluation = Math.max(this.decision [i], this.evaluation);

                    /* Coupe alpha-beta */
                    if (depth < NGMNode.maxDepth)
                    {
                        alpha = Math.max (this.evaluation, alpha);
                        if(this.evaluation >= beta)
                            break;
                    }
                }
                catch (InvalidBotException e)
                {
                    this.decision [i] = 0;
                }
            }

            /* Mis à jour de la table de transposition */
            if(NGMNode.transpoTable.size() == NGMNode.maxSizeTT)
                NGMNode.transpoTable.clear();

            int flag;
            if(this.evaluation <= alphaOrig)
                flag = TranspositionTableEntry.UPPERBOUND;
            else if(this.evaluation >= beta)
                flag = TranspositionTableEntry.LOWERBOUND;
            else
                flag = TranspositionTableEntry.EXACT;

            NGMNode.transpoTable.put(board, new TranspositionTableEntry(flag, depth, this.evaluation));
    }

    /**
     * Initialisation
     */
    protected static void initialize (Board board, int maxDepth, int maxSizeTT)
    {
        NGMNode.maxDepth = maxDepth;
        NGMNode.player = board.getCurrentPlayer ();
        NGMNode.transpoTable = new HashMap<Board, TranspositionTableEntry>();
        NGMNode.maxSizeTT = maxSizeTT;
    }

    private int diffScore (Board board)

    {
        int coeffScorePlayer = 4;
        int coeffScoreOpponent = 4;
        int coeffKrou = 1;
        int coeffHoles = 1;

        int krousPlayer = 0;
        int krousOpponent = 0;
        int takeableHolesPlayer = 0;
        int takeableHolesOpponent = 0;

        for (int i = 0; i < board.getOpponentHoles().length; i++) {
            if(board.getOpponentHoles()[i]>11)
                krousOpponent++;
            if((board.getOpponentHoles()[i]<3)&&(board.getOpponentHoles()[i]>0))
                takeableHolesOpponent++;
        }

        for (int i = 0; i < board.getPlayerHoles().length; i++) {
            if(board.getPlayerHoles()[i]>11)
                krousPlayer++;
            if((board.getPlayerHoles()[i]<3)&&(board.getPlayerHoles()[i]>0))
                takeableHolesPlayer++;
        }

        return (board.getScore(NGMNode.player) * coeffScorePlayer + krousPlayer * coeffKrou
                + takeableHolesPlayer * coeffHoles)
                - (board.getScore(Board.otherPlayer(NGMNode.player)) * coeffScoreOpponent + krousOpponent * coeffKrou
                + takeableHolesOpponent * coeffHoles);
    }

    /**
     * Retourne un noeud du niveau suivant
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param color La couleur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     * @return Un noeud (MinNode ou MaxNode) du niveau suivant
     */
    protected NGMNode getNextNode (Board board, int depth, int color, double alpha, double beta){
        return new NGMNode(board, depth, color, alpha, beta);
    }

    /**
     * L'évaluation du noeud
     * @return L'évaluation du noeud
     */
    double getEvaluation ()
    {
        return this.evaluation;
    }

    /**
     * L'évaluation de chaque coup possible pour le noeud
     * @return L'évaluation de chaque coup possible pour le noeud
     */
    double [] getDecision ()
    {
        return this.decision;
    }
}
