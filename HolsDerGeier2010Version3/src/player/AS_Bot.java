package player;
import player.AS.*;

public class AS_Bot  extends HolsDerGeierSpieler {
	
	/**
	 * @var gamestrategy
	 */
	private IStrategy oStrategy;
	//TODO dynamic strategy changes

	/**
	 * @return IStrategy
	 */
	public IStrategy getStrategy() { return this.oStrategy;	}

	/**
	 * set Strategy
	 * @param oStrategy
	 * @return AS_Bot this Object
	 */
	public AS_Bot setStrategy(IStrategy oStrategy) {
		this.oStrategy = oStrategy;
		return this;
	}

	/**
	 * construct
	 * resetting bot
	 */
	public AS_Bot() { this.reset();	}
	
	/**
	 * reset current object
	 */
	@Override
	public void reset() { this.setStrategy(new StrategyLearning());	}
	
	/**
	 * @param int naechsteKarte next value card
	 * @return int card to play
	 */
	@Override
	public int gibKarte(int naechsteKarte) { return this.oStrategy.nextCard(naechsteKarte, this.letzterZug()); }
}