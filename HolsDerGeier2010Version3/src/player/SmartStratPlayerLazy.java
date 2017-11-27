package player;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import smartStratPlayerPackage.DataHull;
import smartStratPlayerPackage.InfoLevel;
import smartStratPlayerPackage.NormalForm;
import smartStratPlayerPackage.TransformedNormalForm;

public class SmartStratPlayerLazy extends HolsDerGeierSpieler{
//ArrayList<Integer> animalCards;
DefaultMutableTreeNode infoTreeRoot;
DefaultMutableTreeNode currentNode;

ArrayList<Integer> animalCardStack;
ArrayList<Integer> myCards;
ArrayList<Integer> playerBCards;
Integer myLastCard;
Integer playerBLastCard;
Integer currentCard;

	@Override
	public void reset(){
		animalCardStack = new ArrayList<Integer>();
		myCards = new ArrayList<Integer>();
		playerBCards = new ArrayList<Integer>();
		for(int i=1; i<=4;i++){
			animalCardStack.add(i+3);
			myCards.add(i);
			playerBCards.add(i);
		}
	}
	
	@Override
	public int gibKarte(int aktTierkarte){
		if(letzterZug()<0){ // Verhalten in der Eröffnungsrunde
			if(infoTreeRoot==null){
				System.out.println("invoked");
				infoTreeRoot = builtTreeRandom(animalCardStack);
			}
			currentCard=aktTierkarte;
			animalCardStack.remove(new Integer(aktTierkarte));
			// Auffinden des richtigen Knotens
			Enumeration nodes = infoTreeRoot.breadthFirstEnumeration();
			while(nodes.hasMoreElements()){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodes.nextElement();
				if(node.isRoot())
					continue;
				if(checkNode(node)){
					currentNode = node;
				}
			}
			//-------------------------------------------------------
			// Minimax-mäßig gemischte Ausspielung der Karte
			TransformedNormalForm tnf = ((DataHull)currentNode.getUserObject()).getTransformedNormalForm();
			Double gesamtWahrscheinlichkeit = 1d;
			Integer potentielleKarte; // Karte, für die überprüft wird, ob sie ausgespielt werden soll
			Double wahrschPotentielleKarte; // Wahrscheinlichkeit, mit der die Karte ausgespielt wird
			Double modifizierteWahrschPotKarte; // tatsaechliche Wahrscheinlichkeit, mit der die karte ausgespielt wird
			Double random;// zufall
			for(int i=0;i<myCards.size();i++){
				potentielleKarte = myCards.get(i);
				wahrschPotentielleKarte = tnf.getMyPortionOf(potentielleKarte);
				modifizierteWahrschPotKarte = wahrschPotentielleKarte/gesamtWahrscheinlichkeit;
				random = Math.random();
				if(random<modifizierteWahrschPotKarte || i==(myCards.size()-1)){
					myCards.remove(potentielleKarte);
					myLastCard=potentielleKarte.intValue();
					return potentielleKarte;
				}else{
					gesamtWahrscheinlichkeit-= wahrschPotentielleKarte;
				}
			}
			throw new RuntimeException("Sollte hier nie hinkommen");
			// ----------------------------------------------------------
		}
		
		currentCard=aktTierkarte;
		animalCardStack.remove(new Integer(aktTierkarte));
		playerBLastCard = letzterZug();
		playerBCards.remove(playerBLastCard);
		
		// Auffinden des richtigen Knotens 
		// Auffinden des richtigen Knotens
		Enumeration nodes = infoTreeRoot.breadthFirstEnumeration();
		while(nodes.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodes.nextElement();
			if(checkNode(node)){
				currentNode = node;
			}
		}
		// Minimax-mäßig gemischte Ausspielung der Karte
		TransformedNormalForm tnf = ((DataHull)currentNode.getUserObject()).getTransformedNormalForm();
		Double gesamtWahrscheinlichkeit = 1d;
		Integer potentielleKarte; // Karte, für die überprüft wird, ob sie ausgespielt werden soll
		Double wahrschPotentielleKarte; // Wahrscheinlichkeit, mit der die Karte ausgespielt wird
		Double modifizierteWahrschPotKarte; // tatsaechliche Wahrscheinlichkeit, mit der die karte ausgespielt wird
		Double random;// zufall
		for(int i=0;i<myCards.size();i++){
			potentielleKarte = myCards.get(i);
			wahrschPotentielleKarte = tnf.getMyPortionOf(potentielleKarte);
			modifizierteWahrschPotKarte = wahrschPotentielleKarte/gesamtWahrscheinlichkeit;
			random = Math.random();
			if(random<modifizierteWahrschPotKarte || i==(myCards.size()-1)){
				myCards.remove(potentielleKarte);
				myLastCard=potentielleKarte.intValue();
				currentNode=null;
				return potentielleKarte;
			}else{
				gesamtWahrscheinlichkeit-= wahrschPotentielleKarte;
			}
		}
		throw new RuntimeException("Sollte hier nie hinkommen");
		// ----------------------------------------------------------
		
	}
	
	private boolean checkNode(DefaultMutableTreeNode node){
		boolean res = true;
		InfoLevel infoLevel = ((DataHull)node.getUserObject()).getInfoLevel();
		// Vergleich der Tierkarten
		ArrayList<Integer> nodeAnimalCards = infoLevel.getAnimalCardDeck();
		res = res && animalCardStack.size()==nodeAnimalCards.size();
		System.out.println("my: " + animalCardStack.size() );
		System.out.println("node: " + nodeAnimalCards.size());
		System.out.println(res);
		for(Integer i : animalCardStack){
			res = res && nodeAnimalCards.contains(i);
		}
		// Vergleich der Karten Spieler a
		ArrayList<Integer> nodePlayerACards = infoLevel.getPlayerADeck();
		res = res && nodePlayerACards.size()== myCards.size();
		for(Integer i : myCards){
			res = res && nodePlayerACards.contains(i);
		}
		// Vergleich der Karten von Spieler b
		ArrayList<Integer> nodePlayerBCards = infoLevel.getPlayerBDeck();
		res = res && nodePlayerBCards.size()==playerBCards.size();
		for(Integer i : playerBCards){
			res = res && nodePlayerBCards.contains(i);
		}
		return res;
	}
	
	public SmartStratPlayerLazy(){
		
	}
	
	protected DefaultMutableTreeNode builtTree(ArrayList<Integer> animalCards){
		ArrayList<Integer> playerACards = new ArrayList<Integer>();
		ArrayList<Integer> playerBCards = new ArrayList<Integer>();
		
		for(int i=1;i<=animalCards.size();i++){
			playerACards.add(i);
			playerBCards.add(i);
		}
		
		InfoLevel infoLevel = new InfoLevel();
		infoLevel.setAnimalCardDeck(animalCards);
		infoLevel.setCurrentAnimalCard(animalCards.remove(0));
		infoLevel.setPlayerADeck(playerACards);
		infoLevel.setPlayerBDeck(playerBCards);
		infoLevel.setDrawPoints(0);
		infoLevel.setScoreDelta(0);
		
		DataHull dataHull = new DataHull();
		dataHull.setInfoLevel(infoLevel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.setUserObject(dataHull);
		builtChildren(root);
		getPGameValue(root);
		return root;
	}
	
	public void builtChildren(DefaultMutableTreeNode dmtn){
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
				
				if(infoLevel.getAnimalCardDeck().size()>0){
					infoLevel.setCurrentAnimalCard(infoLevel.getAnimalCardDeck().remove(0));
					//break;
				}
			
				DataHull dataHull = new DataHull();
				dataHull.setInfoLevel(infoLevel);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode();
				child.setUserObject(dataHull);
				dmtn.add(child);
				builtChildren(child);
				
			}
		}
	}
	
	
	public Double getPGameValue(DefaultMutableTreeNode node){
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
			}
			else if(ScoreDelta < 0){
				return -1d;
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
				data[i][j] = getPGameValue(getChildOriginatingFrom(node,rowHeader[i],colHeader[j]));
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
			}
			else if(ScoreDelta < 0){
				return -1d;
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
	
	public DefaultMutableTreeNode getChildOriginatingFrom(DefaultMutableTreeNode node, String playerACard, String playerBCard){
		for(int i=0; i < node.getChildCount(); i++){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
			InfoLevel infoBooth = ((DataHull)child.getUserObject()).getInfoLevel();
			if(infoBooth.getLastCardOfA().toString().equals(playerACard) && infoBooth.getLastCardOfB().toString().equals(playerBCard)){
				return child;
			}
		}
		throw new RuntimeException("The specified Tree Node does not contain the specified Values");
	}
	
	protected DefaultMutableTreeNode builtTreeWithoutMinMaxValuesRandom(ArrayList<Integer> animalCards){
		ArrayList<Integer> playerACards = new ArrayList<Integer>();
		ArrayList<Integer> playerBCards = new ArrayList<Integer>();
		
		for(int i=1;i<=animalCards.size();i++){
			playerACards.add(i);
			playerBCards.add(i);
		}
		
		InfoLevel infoLevel = new InfoLevel();
		infoLevel.setAnimalCardDeck(animalCards);
		//------------------------------------------------------------------------
		//infoLevel.setCurrentAnimalCard(infoLevel.getAnimalCardDeck().remove(0));
		//------------------------------------------------------------------------
		infoLevel.setPlayerADeck(playerACards);
		infoLevel.setPlayerBDeck(playerBCards);
		infoLevel.setDrawPoints(0);
		infoLevel.setScoreDelta(0);
		
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
			//getPGameValueRandom(child);
		}
		//------------------------------------------------------------------------------------------
		return root;
	}
	
	protected DefaultMutableTreeNode builtTreeRandom(ArrayList<Integer> animalCards){
		ArrayList<Integer> playerACards = new ArrayList<Integer>();
		ArrayList<Integer> playerBCards = new ArrayList<Integer>();
		
		for(int i=1;i<=animalCards.size();i++){
			playerACards.add(i);
			playerBCards.add(i);
		}
		
		InfoLevel infoLevel = new InfoLevel();
		infoLevel.setAnimalCardDeck(animalCards);
		System.out.println("animalCardSize: " +animalCards.size());
		//------------------------------------------------------------------------
		//infoLevel.setCurrentAnimalCard(infoLevel.getAnimalCardDeck().remove(0));
		//------------------------------------------------------------------------
		infoLevel.setPlayerADeck(playerACards);
		infoLevel.setPlayerBDeck(playerBCards);
		infoLevel.setDrawPoints(0);
		infoLevel.setScoreDelta(0);
		
		DataHull dataHull = new DataHull();
		dataHull.setInfoLevel(infoLevel);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.setUserObject(dataHull);
		//------------------------------------------------------------------------------------------
		for(int i=0; i<infoLevel.getAnimalCardDeck().size();i++){
			InfoLevel infoCopy = cloneInfoLevel(infoLevel);
			infoCopy.setCurrentAnimalCard(infoCopy.getAnimalCardDeck().remove(i));
			System.out.println(infoCopy.getAnimalCardDeck().size());
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
	
	
//	public ArrayList<Integer> copyAnimalCardDeck(ArrayList<Integer> original){
//		ArrayList<Integer> copy = new ArrayList<Integer>();
//		for(Integer i : original){
//			copy.add(new Integer(i.intValue()));
//		}
//		return copy;
//	}
	
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
