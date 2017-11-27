package vultureUtil;

public class ScheduleGenerators {
	
	private ScheduleGenerators(){
		
	}
	
	  public static TournamentScheduleGenerator getGenerator(int numPlayers, int playersPerMatch){
		  if(playersPerMatch <= 2){
			  return new TwoPlayerCombinationGenerator(numPlayers);
		  }
		  else{
			  return new CombinationGenerator(numPlayers, playersPerMatch);
		  }
	  }
}
