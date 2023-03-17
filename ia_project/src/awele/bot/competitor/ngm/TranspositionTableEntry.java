package awele.bot.competitor.ngm;

public class TranspositionTableEntry {

    public static final int EXACT = 0;
    public static final int LOWERBOUND = 1;
    public static final int UPPERBOUND = 2;

    /** Flag de l'entrée de la table de transposition */
    public int flag;
    /** Profondeur de l'entrée de la table de transposition */
    public int depth;
    /** Valeur de l'entrée de la table de transposition */
    public double value;

    TranspositionTableEntry(int flag, int depth, double value){
        this.flag = flag;
        this.depth = depth;
        this.value = value;
    }
}
