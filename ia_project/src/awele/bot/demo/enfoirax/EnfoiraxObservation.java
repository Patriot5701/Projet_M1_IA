package awele.bot.demo.enfoirax;

import awele.core.Board;

/**
 * @author Alexandre Blansché
 * Classe représentant une observation de la base de données de coups jouées
 */
public class EnfoiraxObservation
{
    private int [] playerHoles;
    private int [] oppenentHoles;
    private int move;
    private boolean won;

    EnfoiraxObservation (String string)
    {
        String [] strings = string.split (",");
        this.playerHoles = new int [Board.NB_HOLES];
        for (int i = 0; i < Board.NB_HOLES; i++)
            this.playerHoles [i] = Integer.parseInt (strings [i]);
        this.oppenentHoles = new int [Board.NB_HOLES];
        for (int i = 0; i < Board.NB_HOLES; i++)
            this.oppenentHoles [i] = Integer.parseInt (strings [i + 6]);
        this.move = Integer.parseInt (strings [12].charAt (1) + "");
        this.won = strings [13].equals ("G");
    }

    /**
     * @return Le nombre de graines dans les trous du joueur courant
     */
    public int [] getPlayerHoles ()
    {
        return this.playerHoles;
    }

    public int getPlayerHole(int hole){return this.playerHoles[hole-1];}

    public int getFinalHoleSeeds(){
        int nbSeedsStart = getPlayerHole(getMove());
        int places = nbSeedsStart+getMove();
        int k = places / 12;

        places = places-12*k;
        if(places==0){
            if(getMove()==6){
                return getOppenentHoles()[0]+1+k;
            }else{
                return getPlayerHole(getMove()+1)+1+k;
            }
        }else if(places>6){
            return getOppenentHoles()[places-6-1]+1+k;
        }else{
            return getPlayerHole(places)+1+k;
        }
    }

    /**
     * @return Le nombre de graines dans les trous du joueur adverse
     */
    public int [] getOppenentHoles ()
    {
        return this.oppenentHoles;
    }

    /**
     * Trou de départ
     * @return Le coup joué par le joueur courant
     */
    public int getMove ()
    {
        return this.move;
    }

    /**
     * @return Le joueur courant a-t-il gagné la partie ?
     */
    public boolean isWon ()
    {
        return this.won;
    }
}

