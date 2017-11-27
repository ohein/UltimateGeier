

public class SimpleBuzzardPlayer extends BuzzardPlayer {
    /**
    /**
     * Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls Sie einen eigenen brauchen) Geier
    */
	
	public SimpleBuzzardPlayer(String name){
		super(name);
		System.out.println("SimpleBuzzardPlayer with name " + name + "instantiated.");
	}

   
	
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
    
    @Override
	public String getName(){
    	return "Simpel";
    }
}
