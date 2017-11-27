package player;

public class TestBot2 extends HolsDerGeierSpieler {

	
	public TestBot2(String name){
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
    	return name;
    }
}
