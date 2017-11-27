package controller;

import java.io.Serializable;
import java.util.*;

public class BuzzardDeck implements Serializable {
	public static final int ASCENDENT = 0;
	public static final int DESCENDENT = 1;
	public static final int SHUFFLED = 2;
	
	
	/** Das Kartendeck */
	private ArrayList<Integer> cards;
	
	/** Sortierreihenfolge der Gewinnkarten in codierter Form */
	private int order;
	
	/** Zeiger, der auf die nächste auszuspielende Karte zeigt*/
	private int ptr;
	
	
	/** Hier werden die Werte der Gewinnkarten gespeichert (sowohl standard als auch customized-
	 *  Werte	 */
	private int cardValues[];
	
	
	/** Konstruktor - erzeugt ein standard Hols der Geier Kartenset (Karten -5 bis -1 und 1 bis 10)
	 * @param order Bezeichnet die Reihenfolge, in der die Karten sortiert sind */
	public BuzzardDeck(int order){
		cardValues = new int[15];
		int valuePtr = 0;
		for(int i=-5;i<=10;i++){
			if(i!=0){
				cardValues[valuePtr++]=i;
			}
		}
		this.order=order;
		createDeck(order);
		ptr=0;
	}
	
	/** Konstruktor - erzeugt ein Customized-Hols der Geier Deck.
	 *  @param cardValues Werte der Karten, die das Deck enthalten soll.
	 *  @param order Reihenfolge, in der die Karten sortiert sein sollen.*/
	public BuzzardDeck(int[] cardValues, int order){
		this.cardValues=cardValues;
		this.order=order;
		createDeck(order);
		ptr=0;
	}
	
	
	/** Gibt an, ob das Deck noch Karten enthält*/
	public boolean isEmpty(){
		return ptr==cards.size();
	}
	
	/** Gibt die Anzahl der Karten im Deck zurück*/
	public int size(){
		return cards.size();
	}
	
	/** Gibt die i. Karte des Decks zurück*/
	private int get(int index){
		return cards.get(index);
	}
	
	/** Gibt die nächste Karte des Decks zurück*/
	public int getNextCard(){
		return cards.get(ptr++);
	}
	
	/** Legt die Ordnung der Karten fest
	 * @param order 0 = Aufsteigende Sortierung, 1 = Absteigende Sortierung, 2 = Gemischte Karten*/
	public void setOrder(int order){
		if(!(order>=0&&order<=2)){
			throw new RuntimeException("Unbekannter Sortierungstyp");
		}else{
			this.order=order;
		}
	}
	
	/** Erzeugt ein frisches Kartendeck*/
	 void reset(){
		ptr=0;
		createDeck(order);
	}
	
	/** Erzeugt intern die Karten, verwendet dabei die gewünschten Kartenwerte (cardValues)*/
	 private void createDeck(int order){
		cards = new ArrayList<Integer>();
		if(order==0){
			for(int i=0;i<cardValues.length;i++){
				cards.add(cardValues[i]);
			}
		}
		else if(order==1){
			for(int i=cardValues.length-1;i>=0;i--){
					cards.add(cardValues[i]);
			}
		}
		else if(order==2){
			int rnd=0;
			int tmp;
			for(int i=0;i<cardValues.length;i++){
				if(i>0){
					rnd = (int)(Math.random()*cards.size());
					tmp = cards.remove(rnd);
					cards.add(rnd,cardValues[i]);
					cards.add(tmp);
				}else{
					cards.add(cardValues[i]);
				}
			}
		}
		else throw new RuntimeException("Sortiermodus unbekannt");	
	}
	

}
