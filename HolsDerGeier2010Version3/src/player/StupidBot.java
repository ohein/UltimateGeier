package player;

public class StupidBot extends HolsDerGeierSpieler{
	
	public StupidBot(String name){
		super(name);
		System.out.println("StupidBot with name " + name + " instantiated.");
	}
	
	
	@Override
	public void reset(){
		
	}
	
	@Override
	public int gibKarte(int naechsteKarte){
		System.out.println("++++++++++++++ SB: letzterKarte() : " + letzterZug());
		//logInDatei("Es geht um die Karte " + naechsteKarte);
		switch(naechsteKarte){
		case -5:
			return 1;
		case -4:
			return 2;
		case -3:
			return 3;
		case -2:
			return 4;
		case -1:
			return 5;
		case 1:
			return 6;
		case 2:
			return 7;
		case 3:
			return 8;
		case 4:
			return 9;
		case 5:
			return 10;
		case 6:
			return 11;
		case 7:
			return 12;
		case 8:
			return 13;
		case 9:
			return 14;
		case 10:
			return 15;
		default:
			return 15;
		}
	}
	

}
