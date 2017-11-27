package player;


public class TestOpponent1 extends HolsDerGeierSpieler{
	
	@Override
	public void reset(){
		
	}
	
	@Override
	public int gibKarte(int aktTierkarte){
		switch(aktTierkarte){
			case 4:
				return 1;
			case 5:
				return 2;
			case 6:
				return 3;
			default:
				return 4;
		}
	}
}
