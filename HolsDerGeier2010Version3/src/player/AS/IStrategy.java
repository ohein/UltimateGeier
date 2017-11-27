package player.AS;

/**
 * @author doc
 *
 */
public interface IStrategy {
	
	/**
	 * return next card
	 * @param int card current game value card
	 * @param int iLastOppCard opponents last card
	 * @return int
	 */
	public int nextCard(int card, int iLastOppCard);
}
