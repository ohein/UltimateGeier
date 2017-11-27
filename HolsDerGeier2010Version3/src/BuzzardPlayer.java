
import controller.DealerState;

//
public abstract class BuzzardPlayer {
	private int			nummer;
	private DealerState	dealer;
	private String		password;
	private String		name;
	

	public BuzzardPlayer(String name) {
		this.name = name;
	}

	public DealerState getDealer() {
		return dealer;
	}

	public abstract String getName();

	public int getNummer() {
		return nummer;
	}

	public abstract int gibKarte(int naechsteKarte);

	@Override
	public int hashCode() {
		return (getName() + password).hashCode();
	}

	/**
	 * Mit dieser Methode erfragt ein Spieler die von sämtlichen Gegenern im
	 * letzten Zug gelegten Karten
	 */
	public int[] letzterZug() {
		return dealer.getSetOfLastCards(nummer);
	}

	public int letzterZug(int playerNumber) {
		return dealer.getLastCardOf(playerNumber);
	}

	public void register(DealerState hdg, int nummer) {
		this.dealer = hdg;
		this.nummer = nummer;
	}

	public abstract void reset();
}
