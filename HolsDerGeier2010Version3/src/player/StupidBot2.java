package player;

public class StupidBot2 extends HolsDerGeierSpieler{
	
	private int[] feld = new int[2];
	
	public StupidBot2(String name){
		super(name);
		System.out.println("StupidBot with name " + name + " instantiated");
	}
	
	
	@Override
	public void reset(){
		
	}
	
	@Override
	public void reset2(){
		System.out.println("ICH HABE EINEN NEUEN GEGNER!!!!! WAS FÜR EINE FREUDE");
	}
	
	@Override
	public int gibKarte(int naechsteKarte){
		switch(naechsteKarte){
		case -5:
			return 1;
		case -4:
			int test = feld[45];
			return test;
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
			return 15;
		case 10:
			return 14;
		default:
			return 5;
		}
	}
	

}