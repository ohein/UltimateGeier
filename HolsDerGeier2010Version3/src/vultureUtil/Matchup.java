package vultureUtil;



public class Matchup {

	int player1;
	int player2;

	public Matchup() {

	}
	
	public Matchup(int[] players){
		this.player1 = players[0];
		this.player2 = players[1];
	}

	public Matchup(int player1, int player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Matchup) {
			Matchup m = (Matchup) o;
			return (player1 == m.player1 && player2 == m.player2 || player1 == m.player2 && player2 == m.player1);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return (player1 * player1 + player2 * player2) * player1 + 3 * player2;
	}

}
