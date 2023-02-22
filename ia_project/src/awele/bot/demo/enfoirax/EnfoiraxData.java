package awele.bot.demo.enfoirax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author Alexandre Blansché
 * Base de donnée de coups joués sur laquelle peut se baser l'apprentissage
 */
public class EnfoiraxData extends ArrayList <EnfoiraxObservation>
{
    private static final String PATH = "data/awele.data";

    /**
     * @return Les données
     */
    public static EnfoiraxData getInstance ()
    {
        EnfoiraxData instance = new EnfoiraxData ();
        return instance;
    }

    private EnfoiraxData ()
    {
        this (EnfoiraxData.PATH);
    }

    private EnfoiraxData (String path)
    {
        super ();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File (path)));
            br.readLine ();
            String string;
            while ((string = br.readLine ()) != null)
                this.add (new EnfoiraxObservation (string));
            br.close ();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
