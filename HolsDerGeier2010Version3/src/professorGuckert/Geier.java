package professorGuckert;


/**
 * Beschreiben Sie hier die Klasse Geier.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Geier extends HolsDerGeierSpieler {
    /**
    /**
     * Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls Sie einen eigenen brauchen) Geier
    */


   @Override
public void reset () {
    }
   
    @Override
	public int gibKarte(int naechsteKarte) {
        letzterZug();
        if (naechsteKarte<0)
            return naechsteKarte+6;
        return naechsteKarte+5;
        
    }
    
    
}
