package player;

public class SmartStratPlayerFourOpp extends HolsDerGeierSpieler {
	
	@Override
	public void reset(){
		System.out.println("I have nothing to reset!");
	}
	
	@Override
	public int gibKarte(int animalCard){
		switch(animalCard){
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
