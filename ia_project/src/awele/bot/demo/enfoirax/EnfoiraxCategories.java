package awele.bot.demo.enfoirax;

import java.util.ArrayList;

public class EnfoiraxCategories {
    //TODO ne fonctionnera pas car un id peut être dans une catég ou dans l'autre, mais on peut garder le système de récup des données
    ArrayList<Integer> LOSTid = new ArrayList<Integer>();
    ArrayList<Integer> WINid = new ArrayList<Integer>();

    /**
     * On accède au données et on récupère toutes les observations du joueur gagnant
     */
    public EnfoiraxCategories ()
    {
        EnfoiraxData data = EnfoiraxData.getInstance ();
        for (EnfoiraxObservation observation: data){
            int value = observation.getMove() + 6* observation.getPlayerHole(observation.getMove()) + 6*48* observation.getFinalHoleSeeds();
            if(observation.isWon()){
                if(!this.WINid.contains(value)){
                    this.WINid.add(value);
                }
            }else{
                if(!this.LOSTid.contains(value)){
                    this.LOSTid.add(value);
                }
            }
        }


    }
}
