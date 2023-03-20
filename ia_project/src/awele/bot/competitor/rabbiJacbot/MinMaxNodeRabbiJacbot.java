package awele.bot.competitor.rabbiJacbot;

import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.HashMap;

/**
 * @author Clement Lauer
 * @author Bastien Pazzaglia
 * Noeud d'un arbre MinMax
 */
public abstract class MinMaxNodeRabbiJacbot
{
    /** Numéro de joueur de l'IA */
    private static int player;

    /** Profondeur maximale */
    private static byte maxDepth;

    /** L'évaluation du noeud */
    private byte evaluation;

    /** Table de stockage qui stocke la valeur de certains noeuds */
    private static HashMap<Board, SaveTableEntry> saveTable;

    /** Taille maximum de la table de stockage */
    private static int maxSizeTable;

    /** Évaluation des coups selon MinMax */
    private byte[] decision;

    /**
     * Constructeur...
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     */
    public MinMaxNodeRabbiJacbot (Board board, byte depth, byte alpha, byte beta) {
        /* On crée un tableau des évaluations des coups à jouer pour chaque situation possible */
        this.decision = new byte[Board.NB_HOLES];
        /* Initialisation de l'évaluation courante */
        this.evaluation = this.worst();

        byte alphaOriginal = alpha;

        /* On parcourt toutes les coups possibles dans l'ordre inverse pour avoir en priorité les coups les plus proches de l'adversaire */
        for (int i = Board.NB_HOLES - 1; i >= 0; i--) {
            /* Si le coup est jouable */
            if (board.getPlayerHoles()[i] != 0) {
                /* Sélection du coup à jouer */
                byte[] decision = new byte[Board.NB_HOLES];
                decision[i] = 1;
                /* On copie la grille de jeu et on joue le coup sur la copie */
                Board copy = (Board) board.clone();
                try {
                    int score = copy.playMoveSimulationScore(copy.getCurrentPlayer(), byteArrayToDoubleArray(decision));
                    copy = copy.playMoveSimulationBoard(copy.getCurrentPlayer(), byteArrayToDoubleArray(decision));

                    /* On test l'appartenance du coup dans la table de stockage*/
                    SaveTableEntry entry = MinMaxNodeRabbiJacbot.saveTable.get(copy);

                    /* Si le coup existe dans la table de stockage, on vérifie s'il est exploitable*/
                    if (entry != null && entry.depth >= depth) {
                        if (entry.flag == SaveTableEntry.EXACT) {
                            this.decision[i] = entry.value;
                            this.evaluation = entry.value;
                            break;
                        } else if (entry.flag == SaveTableEntry.LOWERBOUND)
                            alpha = (byte) Math.max(alpha, entry.value);
                        else if (entry.flag == SaveTableEntry.UPPERBOUND)
                            beta = (byte) Math.min(beta, entry.value);

                        if (alpha >= beta) {
                            this.decision[i] = entry.value;
                            this.evaluation = entry.value;
                            break;
                        }

                    }


                /* Si la nouvelle situation de jeu est un coup qui met fin à la partie,
                   on évalue la situation actuelle selon les scores */
                    if ((score < 0) ||
                            (copy.getScore(Board.otherPlayer(copy.getCurrentPlayer())) >= 25) ||
                            (copy.getNbSeeds() <= 6))
                        this.decision[i] = diffScore(board);
                        /* Sinon, on explore les coups suivants */
                    else {
                        /* Si la profondeur maximale n'est pas atteinte */
                        if (depth < MinMaxNodeRabbiJacbot.maxDepth) {
                            /* On construit le noeud suivant */
                            MinMaxNodeRabbiJacbot child = this.getNextNode(copy, (byte) (depth + 1), alpha, beta);
                            /* On récupère l'évaluation du noeud fils */
                            this.decision[i] = child.getEvaluation();
                        }
                        /* Sinon (si la profondeur maximale est atteinte), on évalue la situation actuelle*/
                        else
                            this.decision[i] = heuristic(copy, depth);
                    }
                    /* L'évaluation courante du noeud est mise à jour, selon le type de noeud (MinNode ou MaxNode) */
                    this.evaluation = this.minmax(this.decision[i], this.evaluation);

                    /* Coupe alpha-beta */
                    if (depth > 0) {
                        if (this.alphabeta (this.evaluation, alpha, beta))
                            break;
                        alpha = this.alpha(this.evaluation, alpha);
                        beta = this.beta(this.evaluation, beta);
                    }
                } catch (InvalidBotException e) {
                    this.decision[i] = 0;
                }
            }

            /* Mis à jour de la table de stockage*/
            if(MinMaxNodeRabbiJacbot.saveTable.size() == MinMaxNodeRabbiJacbot.maxSizeTable)
                MinMaxNodeRabbiJacbot.saveTable.clear();

            byte flag;
            flag = (this.evaluation <= alphaOriginal) ? SaveTableEntry.UPPERBOUND : ((this.evaluation >= beta) ? SaveTableEntry.LOWERBOUND : SaveTableEntry.EXACT);

            MinMaxNodeRabbiJacbot.saveTable.put(board, new SaveTableEntry(flag, depth, this.evaluation));

        }
    }

    /** Pire score pour un joueur */
    protected abstract byte worst ();

    /**
     * Initialisation
     */
    protected static void initialize (Board board, byte depth, byte maxSizeTable)
    {
        /* Initialisation de la profondeur : profondeur originelle + (nombre_de_graines_totales / nombre_de_graines_restantes) */
        MinMaxNodeRabbiJacbot.maxDepth = (byte) (depth + (int)Math.round(48.0/board.getNbSeeds()));
        MinMaxNodeRabbiJacbot.player = board.getCurrentPlayer ();
        MinMaxNodeRabbiJacbot.saveTable = new HashMap<Board, SaveTableEntry>();
        MinMaxNodeRabbiJacbot.maxSizeTable = maxSizeTable;
    }

    /**
     * Première heuristique : différence de scores
     * @param board
     * @return
     */
    private byte diffScore (Board board)
    {
        return (byte) (board.getScore (MinMaxNodeRabbiJacbot.player) - board.getScore (Board.otherPlayer (MinMaxNodeRabbiJacbot.player)));
    }

    /**
     * Heuristique complète
     * @param board
     * @param depth
     * @return
     */
    private byte heuristic(Board board, int depth){

        byte scoreSeeds = 0;

        byte playerKrou = 0;
        byte opponentKrou = 0;

        //Détermine si coup max ou coup min
        boolean isMax;
        isMax = (depth%2 == 0) ? true : false;

        for(int i = 0; i < Board.NB_HOLES; i++) {
            //Vérification des cases en danger
            if(board.getPlayerHoles()[i] < 3 ) {
                scoreSeeds = (byte) (isMax ? (scoreSeeds+1) : (scoreSeeds-1));
            }

            if(board.getOpponentHoles()[i] < 3 ) {
                scoreSeeds = (byte) (isMax ? (scoreSeeds-1) : (scoreSeeds+1));
            }

            //Vérification de la présence de Krous
            if(board.getPlayerHoles()[i] >= 11) {
                playerKrou = 1;
            }

            if(board.getOpponentHoles()[i] >= 11) {
                opponentKrou = 1;
            }

        }

        return (byte) (diffScore(board) + scoreSeeds + opponentKrou - playerKrou);
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
    protected abstract MinMaxNodeRabbiJacbot getNextNode (Board board, byte depth, byte alpha, byte beta);

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

    /**
     * Convertit les tableaux de bytes en tableaux de Double
     * @param bytes
     * @return
     */
    protected static double[] byteArrayToDoubleArray(byte[] bytes){
        double[] array = new double[bytes.length];
        for(int i = 0; i<bytes.length;i++) {
            array[i] = bytes[i];
        }
        return array;
    }


}


