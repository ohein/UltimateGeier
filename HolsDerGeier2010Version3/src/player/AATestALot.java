package player;

import java.util.*;

public class AATestALot extends HolsDerGeierSpieler {
	private int sum = 0;
	private ArrayList<ArrayList<Integer>> pools = new ArrayList<ArrayList<Integer>>();
	
	
	@Override
	public void reset() {
		for(int i = 1; i <= 15; i++){
			ArrayList<Integer> liste = new ArrayList<Integer>();
			for(int j=1;j<=3;j++){
				liste.add(i);
			}
			pools.add(liste);
		}
	}
	
	

	@Override
	public int gibKarte(int naechsteKarte) {
		if(naechsteKarte == 10){
			return 3;
		}
		return 3;
	}

}
