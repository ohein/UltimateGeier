package player;

import java.util.ArrayList;

public class IntelligentBuzzardPlayer extends HolsDerGeierSpieler{
    private ArrayList<Integer> nochZuGewinnen=new ArrayList<Integer>();
    private ArrayList<Integer> vomGegnerNochNichtGelegt=new ArrayList<Integer>();
    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
    
    /**
     * Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls Sie einen eigenen brauchen) IntelligentererGeier
    */
    public IntelligentBuzzardPlayer(String name) {
    	super(name);
    	reset();
    	System.out.println("IntelligentBuzzardPlayer with name " + name + "instantiated");
    }
            
    @Override
	public String getName(){
    	return name;
    }
    
    @Override
	public void reset() {
        nochZuGewinnen.clear();
        for (int i=10;i>-6;i--)
            nochZuGewinnen.add(i);
        vomGegnerNochNichtGelegt.clear();        
        for (int i=15;i>0;i--)
            vomGegnerNochNichtGelegt.add(i);
        for (int i=15;i>0;i--)            
            nochNichtGespielt.add(i);                                 
    }
    
    @Override
	public int gibKarte(int naechsteKarte) {
       System.out.println("IBP: Ich spiele um die" + naechsteKarte);
       int ret=-99;
       int letzeKarteGegner = letzterZug();
       int letzteKarteGegner=letzeKarteGegner;
       if (letzteKarteGegner!=-1000 && letzteKarteGegner!=-1001&&letzteKarteGegner!=-1002)
          vomGegnerNochNichtGelegt.remove(vomGegnerNochNichtGelegt.indexOf(letzteKarteGegner));
       // Lösche dieser Karte
       nochZuGewinnen.remove(nochZuGewinnen.indexOf(naechsteKarte));
       // Wenn die karte negativ ist  => spiele die letzte Karte
       if (naechsteKarte>0) {           
           // Kommt die hoechste Karte und der gener hat die gleiche höchste => spiele mittlere Karte
           if ((nochZuGewinnen.indexOf(naechsteKarte)==0)&&(vomGegnerNochNichtGelegt.get(0)==nochNichtGespielt.get(0))) {
                   ret=nochNichtGespielt.get(nochZuGewinnen.size()/2);
                   //System.out.println(">>> A");
           // Steht die zu gewinnende Karte in der vorderen Haelfte der Reihenfolge und hat der gegner => Spiele die höchste Karte
        } else if (nochZuGewinnen.indexOf(naechsteKarte)>nochZuGewinnen.size()/2) {
                   ret=nochNichtGespielt.get(0);
                   //System.out.println(">>> B");                   
                } else { // sonst greife in die Mitte
                   ret=nochNichtGespielt.get(nochZuGewinnen.size()/2);
                   //System.out.println(">>> C");                   
                }
       } else {
           ret=nochNichtGespielt.get(nochNichtGespielt.size()-1);
           //System.out.println(">>> D");                              
       }
       nochNichtGespielt.remove(nochNichtGespielt.indexOf(ret));
       System.out.println("Ich biete dafür die " + ret);
       return ret;
    }
}
