package awele.bot.competitor.rabbiJacbot;

public class SaveTableEntry {
    public static final byte EXACT = 0;
    public static final byte LOWERBOUND = 1;
    public static final byte UPPERBOUND = 2;

    /** Flag de l'entrée de la table de stockage */
    public byte flag;
    /** Profondeur de l'entrée de la table de stockage */
    public byte depth;
    /** Valeur de l'entrée de la table de stockage */
    public byte value;

    SaveTableEntry(byte flag, byte depth, byte value){
        this.flag = flag;
        this.depth = depth;
        this.value = value;
    }
}
