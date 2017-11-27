package tests;

import vultureUtil.ScheduleGenerators;
import vultureUtil.TwoPlayerCombinationGenerator;
import junit.framework.TestCase;
import java.util.HashSet;
import vultureUtil.Matchup;

public class TwoPlayerCombinationGeneratorTest extends TestCase {
	
	public void testGeneratorgetTotal(){
		TwoPlayerCombinationGenerator generator =(TwoPlayerCombinationGenerator) ScheduleGenerators.getGenerator(5,2);
		assertEquals(10,generator.getTotal());
	}
	
	public void testGeneratorNumberOfGeneratedMatchups(){
		TwoPlayerCombinationGenerator generator = (TwoPlayerCombinationGenerator) ScheduleGenerators.getGenerator(6,2);
		int cnt = 0;
		while(generator.hasMore()){
			generator.getNext();
			cnt++;
		}
		assertEquals(15, cnt);
	}
	
	public void testGeneratorForRedundancy(){
		TwoPlayerCombinationGenerator generator = (TwoPlayerCombinationGenerator)ScheduleGenerators.getGenerator(6, 2);
		HashSet<Matchup> set = new HashSet<Matchup>();
		int[] matchup;
		while(generator.hasMore()){
			matchup = generator.getNext();
			set.add(new Matchup(matchup));
		}
		assertEquals(15,set.size());
	}
	
	
	
	
}
