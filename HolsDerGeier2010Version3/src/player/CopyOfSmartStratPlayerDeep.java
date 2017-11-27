package player;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import smartStratPlayerPackage.DataHull;
import smartStratPlayerPackage.InfoLevel;
import smartStratPlayerPackage.NormalForm;
import smartStratPlayerPackage.TransformedNormalForm;

public class CopyOfSmartStratPlayerDeep extends HolsDerGeierSpieler {
	ArrayList<Integer> _animalCards;
	ArrayList<Integer> _playerACards;
	ArrayList<Integer> _playerBCards;
	Integer _pointDelta;
	Integer _drawPoints;
	Integer _meineLetzteKarte;
	Integer _wertLetzteRunde;
	
	@Override
	public void reset(){
		_animalCards = new ArrayList<Integer>();
		_playerACards = new ArrayList<Integer>();
		_playerBCards = new ArrayList<Integer>();
		for(int i=1;i<=4;i++){
			_animalCards.add(i+3);
			_playerACards.add(i);
			_playerBCards.add(i);
		}
		_pointDelta=0;
		_drawPoints=0;
		_meineLetzteKarte=0;
		_wertLetzteRunde=0;
	}
	
	@Override
	public int gibKarte(int aktuelleTierkarte){
		Integer letzteKarteGegner = letzterZug();
		// Auswertung der letzten Runde
		if(letzteKarteGegner>0){
			Integer aktuellerRundenwert = _drawPoints+_wertLetzteRunde;
			if(aktuellerRundenwert>=0){
				if(_meineLetzteKarte>letzteKarteGegner){
					_pointDelta+=aktuellerRundenwert;
					_drawPoints=0;
				}else if(_meineLetzteKarte<letzteKarteGegner){
					_pointDelta-=aktuellerRundenwert;
					_drawPoints=0;
				}else{
					_drawPoints=aktuellerRundenwert;
				}
			}else{
				if(_meineLetzteKarte>letzteKarteGegner){
					_pointDelta-=aktuellerRundenwert;
					_drawPoints=0;
				}else if(_meineLetzteKarte<letzteKarteGegner){
					_pointDelta+=aktuellerRundenwert;
					_drawPoints=0;
				}else{
					_drawPoints=aktuellerRundenwert;
				}
			}
		}
		// Ende Auswertung der letzten Runde 
		_wertLetzteRunde = aktuelleTierkarte;
		if(letzteKarteGegner>0)
			_playerBCards.remove(letzteKarteGegner);
		DefaultMutableTreeNode root = builtTree(_animalCards,_playerACards,_playerBCards,_pointDelta, _drawPoints);
		DefaultMutableTreeNode child = null;
		/** Finde das Kind mit der aktuellen Karte als aktueller Tierkarte*/
		for(int i=0;i<root.getChildCount();i++){
			child = (DefaultMutableTreeNode)root.getChildAt(i);
			if( ((DataHull)child.getUserObject()).getInfoLevel().getCurrentAnimalCard().equals(new Integer(aktuelleTierkarte)))
				break;
		}
		TransformedNormalForm tnf = ((DataHull)child.getUserObject()).getTransformedNormalForm();
		Double gesamtWahrscheinlichkeit = 1d;
		Integer potentielleKarte;
		Double wahrschPotentielleKarte;
		Double modifizierteWahrschPotKarte;
		Double random;
		for(int i=0;i<_playerACards.size();i++){
			potentielleKarte = _playerACards.get(i);
			wahrschPotentielleKarte = tnf.getMyPortionOf(potentielleKarte);
			modifizierteWahrschPotKarte = wahrschPotentielleKarte/gesamtWahrscheinlichkeit;
			random = Math.random();
			if(random<modifizierteWahrschPotKarte || i==(_playerACards.size()-1)){
				_animalCards.remove(new Integer(aktuelleTierkarte));
				_playerACards.remove(potentielleKarte);
				_meineLetzteKarte=potentielleKarte.intValue();
				return potentielleKarte;
			}else{
				gesamtWahrscheinlichkeit-= wahrschPotentielleKarte;
			}
		}
		throw new RuntimeException("Sollte hier nie hinkommen");
	}
	
	public CopyOfSmartStratPlayerDeep(){
		
	}
	
	public DefaultMutableTreeNode builtTree(ArrayList<Integer> animalCards, 
			ArrayList<Integer> playerACards, ArrayList<Integer> playerBCards,Integer pointDelta, Integer drawPoints){
		if(animalCards.size() != playerACards.size() || 
				playerACards.size()!=playerBCards.size()){
			throw new RuntimeException("All arrays must be of equal size!");
			
		}
		InfoLevel infoLevel = new InfoLevel();
		infoLevel.setAnimalCardDeck(animalCards);
		//------------------------------------------------------------------------
		//infoLevel.setCurrentAnimalCard(infoLevel.getAnimalCardDeck().remove(0));
		//------------------------------------------------------------------------
		infoLevel.setPlayerADeck(playerACards);
		infoLevel.setPlayerBDeck(playerBCards);
		infoLevel.setDrawPoints(drawPoints);
		infoLevel.setScoreDelta(pointDelta);
		
		DataHull dataHull = new DataHull();
		dataHull.setInfoLevel(infoLevel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.setUserObject(dataHull);
		//------------------------------------------------------------------------------------------
		for(int i=0; i<infoLevel.getAnimalCardDeck().size();i++){
			InfoLevel infoCopy = cloneInfoLevel(infoLevel);
			infoCopy.setCurrentAnimalCard(infoCopy.getAnimalCardDeck().remove(i));
			DataHull dataHull2 = new DataHull();
			dataHull2.setInfoLevel(infoCopy);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode();
			child.setUserObject(dataHull2);
			root.add(child);
			builtChildrenRandom(child);
			getPGameValueRandom(child);
		}
		//------------------------------------------------------------------------------------------
		return root;
	}
	
	public Double getPGameValueRandom(DefaultMutableTreeNode node){
		DataHull dataHull = (DataHull)node.getUserObject();
		InfoLevel infoLevel = dataHull.getInfoLevel();
		/*Wenn es sich um ein Blatt handelt, ist das Ergebnis klar. (Ein
		 * Blatt des Baumes zeichnet sich unter anderem dadurch aus, dass
		 * beide Spieler keine Bietekarte mehr besitzen, worauf die bedingte
		 * Anweisung schliesslich auch prueft*/
		if(dataHull.getInfoLevel().getPlayerADeck().size()==0 &&
	        dataHull.getInfoLevel().getPlayerBDeck().size()==0){
			Integer ScoreDelta = dataHull.getInfoLevel().getScoreDelta();
			if(ScoreDelta > 0){
				return 1d;
				//return new Double(ScoreDelta);
			}
			else if(ScoreDelta < 0){
				return -1d;
				//return new Double(ScoreDelta);
			}
			else{
				return 0d;
			}
		}
		
		/* An dieser Stelle wird die Normalform aufgebaut*/
		ArrayList<Integer> playerACards = infoLevel.getPlayerADeck();
		ArrayList<Integer> playerBCards = infoLevel.getPlayerBDeck();
		
		String[] rowHeader = new String[playerACards.size()];
		String[] colHeader = new String[playerBCards.size()];
		Double[][] data = new Double[playerACards.size()][playerBCards.size()];
		
		
		for(int i = 0; i < playerACards.size();i++){
			rowHeader[i] = playerACards.get(i).toString();
			for(int j = 0; j < playerBCards.size();j++){
				colHeader[j] = playerBCards.get(j).toString();
				//data[i][j] = getPGameValue(
						//(DefaultMutableTreeNode)getChildOriginatingFrom(node,
								//rowHeader[i],colHeader[j]));
				ArrayList<DefaultMutableTreeNode> list = getChildrenOriginatingFrom(node,
						rowHeader[i], colHeader[j]);
				data[i][j] = calculateArithmeticAverage(list);
			}
		}
		NormalForm nf = new NormalForm();
		
		nf.setRowHeader(rowHeader);
		nf.setColHeader(colHeader);
		nf.setData(data);
		dataHull.setNormalForm(nf);
		
		TransformedNormalForm tnf = new TransformedNormalForm(nf,100);
		dataHull.setTransformedNormalForm(tnf);
		
		return tnf.getGameValue();
	}
	
	
	public ArrayList<DefaultMutableTreeNode> getChildrenOriginatingFrom(DefaultMutableTreeNode node, String playerACard, String playerBCard){
		ArrayList<DefaultMutableTreeNode> res = new ArrayList<DefaultMutableTreeNode>();
		for(int i=0; i < node.getChildCount(); i++){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
			InfoLevel infoLevel = ((DataHull)child.getUserObject()).getInfoLevel();
			if(infoLevel.getLastCardOfA().toString().equals(playerACard)&& infoLevel.getLastCardOfB().toString().equals(playerBCard))
				res.add(child);
		}
		return res;
	}
	
	public Double calculateArithmeticAverage(ArrayList<DefaultMutableTreeNode> list){
		Double sum = 0d;
		for(DefaultMutableTreeNode node : list){
			sum += getPGameValueRandom(node);
		}
		if(list.size()==0){
			throw new RuntimeException("Not possible");
		}
		return sum/list.size();
	}
	
	public void builtChildrenRandom(DefaultMutableTreeNode dmtn){
		InfoLevel original = ((DataHull)dmtn.getUserObject()).getInfoLevel();
		
		for(int i=0;i < original.getPlayerADeck().size();i++){
			for(int j=0;j<original.getPlayerBDeck().size();j++){
				
				/* Zuerst muss das originale InfoBooth-Objekt kopiert werden, wobei
				 * darauf zu achten ist, dass nicht lediglich die Referenzen, sondern 
				 * tatsächlich die Objekte kopiert werden*/
				InfoLevel infoLevel = cloneInfoLevel(((DataHull)dmtn.getUserObject()).getInfoLevel());
				/*Ist das InfoBooth-Objekt kopiert, kann nun damit gearbeitet werden*/ 

				Integer cardA = infoLevel.getPlayerADeck().remove(i);
				Integer cardB = infoLevel.getPlayerBDeck().remove(j);
				infoLevel.setLastCardOfA(cardA);
				infoLevel.setLastCardOfB(cardB);
				Integer valueAtStake = infoLevel.getCurrentAnimalCard() + infoLevel.getDrawPoints();
				
				if(cardA.intValue()>cardB.intValue()){
					if(valueAtStake >= 0)
						infoLevel.setScoreDelta(infoLevel.getScoreDelta()+valueAtStake);
					else
						infoLevel.setScoreDelta(infoLevel.getScoreDelta()-valueAtStake);
					infoLevel.setDrawPoints(0);
				}
				else if(cardB.intValue()>cardA.intValue()){
					if(valueAtStake>=0)
						infoLevel.setScoreDelta(infoLevel.getScoreDelta()-valueAtStake);
					else
						infoLevel.setScoreDelta(infoLevel.getScoreDelta()+valueAtStake);
					infoLevel.setDrawPoints(0);
				}
				else{
					infoLevel.setDrawPoints(infoLevel.getDrawPoints()+infoLevel.getCurrentAnimalCard());
				}
				
				//if(infoLevel.getAnimalCardDeck().size()>0){
					//infoLevel.setCurrentAnimalCard(infoLevel.getAnimalCardDeck().remove(0));
			        //break;
				//}
				
				if(infoLevel.getAnimalCardDeck().size()>0){
					for(int k=0;k < infoLevel.getAnimalCardDeck().size();k++){
						InfoLevel copyCopy = cloneInfoLevel(infoLevel);
						copyCopy.setCurrentAnimalCard(copyCopy.getAnimalCardDeck().remove(k));
						DataHull dataHull = new DataHull();
						dataHull.setInfoLevel(copyCopy);
						DefaultMutableTreeNode child = new DefaultMutableTreeNode();
						child.setUserObject(dataHull);
						dmtn.add(child);
						builtChildrenRandom(child);
					}
				}
				else{
					DataHull dataHull = new DataHull();
					dataHull.setInfoLevel(infoLevel);
					DefaultMutableTreeNode child = new DefaultMutableTreeNode();
					child.setUserObject(dataHull);
					dmtn.add(child);
					builtChildrenRandom(child);
				}
				
			}
		}
	}
	
	public InfoLevel cloneInfoLevel(InfoLevel infoBooth){
		ArrayList<Integer> animalCards = new ArrayList<Integer>();
		for(Integer i : infoBooth.getAnimalCardDeck() ){
			animalCards.add(new Integer(i.intValue()));
		}
		
		ArrayList<Integer> playerACards = new ArrayList<Integer>();
		for(Integer i : infoBooth.getPlayerADeck()){
			playerACards.add(new Integer(i.intValue()));
		}
		
		ArrayList<Integer> playerBCards = new ArrayList<Integer>();
		for(Integer i : infoBooth.getPlayerBDeck()){
			playerBCards.add(new Integer(i.intValue()));
		}
		
		InfoLevel res = new InfoLevel();
		
		res.setAnimalCardDeck(animalCards);
		res.setPlayerADeck(playerACards);
		res.setPlayerBDeck(playerBCards);
		if(infoBooth.getLastCardOfA()!=null)
			res.setLastCardOfA(infoBooth.getLastCardOfA().intValue());
		if(infoBooth.getLastCardOfB()!=null)
			res.setLastCardOfB(infoBooth.getLastCardOfB().intValue());
		if(infoBooth.getCurrentAnimalCard()!=null)
			res.setCurrentAnimalCard(infoBooth.getCurrentAnimalCard().intValue());
		res.setScoreDelta(infoBooth.getScoreDelta().intValue());
		res.setDrawPoints(infoBooth.getDrawPoints().intValue());
		
		return res;
		
	}

}
