package awele.bot.demo.alphabeta;

import java.util.HashMap;

public class AlphaBetaData {
    private static final int CACHE_SIZE = 500000;
    private HashMap<String, Integer> transpositionTable = new HashMap<>(CACHE_SIZE);

    private AlphaBetaData() {}

    private static AlphaBetaData INSTANCE = null;

    public static AlphaBetaData getInstance()
    {
        if (INSTANCE == null)
        {   INSTANCE = new AlphaBetaData();
        }
        return INSTANCE;
    }

    public HashMap<String, Integer> getTranspositionTable() {
        return transpositionTable;
    }

    public void put(String key, int evaluation){
        this.transpositionTable.put(key, evaluation);
    }

    public void clear(){
        this.transpositionTable.clear();
    }
}
