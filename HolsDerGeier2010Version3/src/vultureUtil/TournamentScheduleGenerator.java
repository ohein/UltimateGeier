package vultureUtil;

import java.io.Serializable;
import java.math.BigInteger;

public interface TournamentScheduleGenerator extends Serializable {
	// ------
	// Reset
	// ------

	public abstract void reset();

	// ------------------------------------------------
	// Return number of combinations not yet generated
	// ------------------------------------------------

	public abstract BigInteger getNumLeft();

	// -----------------------------
	// Are there more combinations?
	// -----------------------------

	public abstract boolean hasMore();

	// ------------------------------------
	// Return total number of combinations
	// ------------------------------------

	public abstract BigInteger getTotal();

	public abstract int[] getNext();

	public int getN(); // @Gruppe 1
}
