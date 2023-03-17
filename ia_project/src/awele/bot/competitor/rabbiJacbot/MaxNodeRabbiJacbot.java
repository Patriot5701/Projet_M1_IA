package awele.bot.competitor.rabbiJacbot;

import awele.core.Board;

public class MaxNodeRabbiJacbot extends MinMaxNodeRabbiJacbot{
    /**
     * Constructeur pour un noeud initial
     * @param board La situation de jeu pour laquelle il faut prendre une décision
     */
    MaxNodeRabbiJacbot (Board board)
    {
        this (board, (byte) 0, (byte) -Byte.MAX_VALUE, Byte.MAX_VALUE);
    }

    /**
     * Constructeur d'un noeud interne
     * @param board La situation de jeu pour le noeud
     * @param depth La profondeur du noeud
     */
    MaxNodeRabbiJacbot (Board board, byte depth, byte alpha, byte beta)
    {
        super (board, depth, alpha, beta);
    }

    /**
     * Retourne le max
     * @param eval1 Un double
     * @param eval2 Un autre double
     * @return Le max entre deux valeurs, selon le type de noeud
     */
    @Override
    protected byte minmax (byte eval1, byte eval2)
    {
        return (byte) Math.max (eval1, eval2);
    }

    /**
     * Indique s'il faut faire une coupe alpha-beta
     * (si l'évaluation courante du noeud est supérieure à l'évaluation courante du noeud parent)
     * @param eval L'évaluation courante du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     * @return Un booléen qui indique s'il faut faire une coupe alpha-beta
     */
    @Override
    protected boolean alphabeta (byte eval, byte alpha, byte beta)
    {
        return eval >= beta;
    }

    /**
     * Retourne un noeud MinNode du niveau suivant
     * @param board L'état de la grille de jeu
     * @param depth La profondeur du noeud
     * @param alpha Le seuil pour la coupe alpha
     * @param beta Le seuil pour la coupe beta
     * @return Un noeud MinNode du niveau suivant
     */
    @Override
    protected MinMaxNodeRabbiJacbot getNextNode (Board board, byte depth, byte alpha, byte beta)
    {
        return new MinNodeRabbiJacbot(board, depth, alpha, beta);
    }

    /**
     * Mise à jour de alpha
     * @param evaluation L'évaluation courante du noeud
     * @param alpha L'ancienne valeur d'alpha
     * @return
     */
    @Override
    protected byte alpha (byte evaluation, byte alpha)
    {
        return (byte) Math.max (evaluation, alpha);
    }

    /**
     * Mise à jour de beta
     * @param evaluation L'évaluation courante du noeud
     * @param beta L'ancienne valeur de beta
     * @return
     */
    @Override
    protected byte beta (byte evaluation, byte beta)
    {
        return beta;
    }

    /** Pire score : une petite valeur */
    @Override
    protected byte worst ()
    {
        return -Byte.MAX_VALUE;
    }
}
