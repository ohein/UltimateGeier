package player;


//Import der Dateien
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
* Geierlogik
* 
* @author Hohmann, Weber, Rupp
* @version  v 1.2 17.01.2012
*/

public class S952886 extends HolsDerGeierSpieler {
/** Die Elementaren ArrayListen zum Speichern von
 * Informationen über die Karten des Gegner, Stapelkarten sowie MeineKarten 
 * Die Meine Karten werden in meinPool in 5 ArrayListen gespeichert die Initalizierung 
 * passiert in initArrayListen().
 */
private ArrayList <Integer> nochVorhanden = new ArrayList<Integer>();                               
private ArrayList <Integer> gegnerBlatt = new ArrayList<Integer>();                                 
private ArrayList<ArrayList<Integer>> meinPool = new ArrayList<ArrayList<Integer>>();  

/** Durch das aufteilen der ArrayList in 5 Listen folgt die Problematik das
 * die gewünschte ArrayList keine Karten mehr enthält, hier wurde abhilfe geschafft über 
 * eine HashMap. Im Konstruktor werden die Routen festgelegt, also die Sprünge der ArrayListen
 */
private ArrayList<Map<Integer,Integer>> meineRouten = new ArrayList<Map<Integer,Integer>>();

/** Grundlegende Variablen zum Speichern vom letztenZug d.h der Karte die ich Gespielt habe sowie
 * den Wert meiner letzten Gespielten Karte
 */
private int meinLetzterZug = 0;
private int letzterWertZug = 0;

/** 
 * Konstruktor
 * Im Konstruktor werden die Routen Sprünge der Hash Map Initzialisiert
 * d.h sobald in einer der ArrayListen keine Daten mehr vorhanden sind spring er in die 
 * vor definierte Route.
 */
public S952886(){
    //Pool 0
    Map<Integer,Integer> meineRoute = new HashMap<Integer,Integer>();
    meineRoute.put(0,1);    
    meineRoute.put(1,2);
    meineRoute.put(2,3);
    meineRoute.put(3,4);
    meineRoute.put(4,0);
    meineRouten.add(meineRoute); //route für 0
    //Pool 1
    meineRoute = new HashMap<Integer,Integer>();
    meineRoute.put(1,2);    
    meineRoute.put(2,3);
    meineRoute.put(3,4);
    meineRoute.put(4,0);
    meineRoute.put(0,1);
    meineRouten.add(meineRoute); //route für 1
    //Pool 2
    meineRoute = new HashMap<Integer,Integer>();
    meineRoute.put(2,3);    
    meineRoute.put(3,4);
    meineRoute.put(4,1);
    meineRoute.put(1,0);
    meineRoute.put(0,2);
    meineRouten.add(meineRoute); //route für 2
    //Pool 3
    meineRoute = new HashMap<Integer,Integer>();
    meineRoute.put(3,4);    
    meineRoute.put(4,2);
    meineRoute.put(2,1);
    meineRoute.put(1,0);
    meineRoute.put(0,3);
    meineRouten.add(meineRoute); //route für 3
    //Pool 4
    meineRoute = new HashMap<Integer,Integer>();
    meineRoute.put(4,3);    
    meineRoute.put(3,2);
    meineRoute.put(2,1);
    meineRoute.put(1,0);
    meineRoute.put(0,4);
    meineRouten.add(meineRoute); //route für 4       
    
    initArrayListen();
}
/**
 * Zurücksetzten der ArrayListen und Aufruf der Methode initArrayListen() zusätzlich werden 
 * alle Variablen auf 0 gesetzte
 */
@Override
public void reset () {
      nochVorhanden.clear();
      gegnerBlatt.clear();
      meinPool.clear();
      initArrayListen();
      meinLetzterZug = 0;
      letzterWertZug = 0;
 }
/**
 * Vordefinierte Methode die immer aufgerufen wird und als Rückgabewert meine Karte hat die ich 
 * spielen möchte. 
 * Zusätzlich ist eine Logik vorhanden zum abdecken des Stichs(gleiche Karten im letzten Zug)
 */
 @Override
public int gibKarte(int naechsteKarte) {
     int rueckgabeKarte = 0;
   
     gegnerBlatt.remove(new Integer(letzterZug()));
     nochVorhanden.remove(new Integer(naechsteKarte));
     
     if (meinLetzterZug == letzterZug()){
         naechsteKarte += letzterWertZug;
     }    
     
     rueckgabeKarte = logik(naechsteKarte); 

     meinLetzterZug = rueckgabeKarte;
     letzterWertZug = naechsteKarte;
     return rueckgabeKarte;
 } 

 /**
  * Die Logik der Karten die von mir Gespielt werden. 
  * Diese Methode wird durch gibKarte() aufgerufen und bekommt den Wert um den es gerade geht rein 
  * und gibt die Karte die Gespielt wird zurück 
  */   
 private int logik(int karte){    
    int rueckgabe = 0; 
    ArrayList<Integer> pool = null;
    
    if (karte <= -7){ 
        pool = getPool(3);
        rueckgabe = pool.get(pool.size()-1);
    }    

    if (karte >= -6 && karte <= -4){
        pool = getPool(0);
        rueckgabe = pool.get(0);           
    }

    if (karte >= -3 && karte <= -1){
        pool = getPool(2);
        rueckgabe = pool.get(pool.size()-1);
    }

    if (karte >= 1 && karte <= 2){
        pool = getPool(0);
        rueckgabe = pool.get(pool.size()-1);
    }
    
    if (karte >= 3 && karte <= 5){
        pool = getPool(3);
        switch(karte){
          case 3:rueckgabe = pool.get(0);
          case 4:rueckgabe = pool.get(0);
          case 5:rueckgabe = pool.get(pool.size()-1) ; 
        }      
    }
    if (karte >= 6 && karte <= 8){
     	   pool = getPool(4);
            rueckgabe = pool.get(pool.size()-1);              
    }

    if (karte >= 9 && karte <= 11){
        if (maxKarte() == true){ 
     	   pool = getPool(3);
            rueckgabe = pool.get(pool.size()-1);              
        }
        else{ 
     	   pool = getPool(1);
            rueckgabe = pool.get(0);
        }
    }
    
    if (karte >= 12){
       pool = getPool(4);
       rueckgabe = pool.get(pool.size()-1);              
    }
    
    if(pool != null)
     pool.remove(new Integer(rueckgabe));
  
    System.out.println(rueckgabe); 
    return rueckgabe;
 }    

 /**
  * In der Methode wird berechnet ob ich eine Höhere Karte auf der Hand habe als der Gegner
  */
 private boolean maxKarte(){
     int maxPool = 0;
     
     for(ArrayList<Integer> liste : meinPool){
         for(Integer i : liste){
          maxPool = (i > maxPool) ? i : maxPool;
         }
     }
     
     int maxGegner = 0;
     
     for(Integer i : gegnerBlatt){
          maxGegner = (i > maxGegner) ? i : maxGegner;
     }
     
     return maxPool > maxGegner;
 }    

 /**
  * Overload der Klasse getPool().
  */
 
 private ArrayList<Integer> getPool(int pool){
     return getPool(pool, meineRouten.get(pool));
 }
 private ArrayList<Integer> getPool(int pool, Map<Integer,Integer> route){
     if(meinPool.get(pool).isEmpty()){
         return getPool(route.get(pool),route);
     }
     return meinPool.get(pool);
 }
/**
* Befüllen der ArrayListen
*/  
 private void initArrayListen(){                                                                                 
    for(int j = 0; j <= 4;j++){
        ArrayList<Integer> x = new ArrayList<Integer>();
        int initial = (j == 0) ? 1 : (3*j)+1;
        for (int i = initial ;i < initial+3 ; i++){
             x.add(i);
         }
         meinPool.add(x);
    } 
    
    for (int i =1 ;i <= 15; i++){
         gegnerBlatt.add(i);
    }   
    for (int i = -5; i <= 10; i++){
         if (i != 0)
             nochVorhanden.add(i);
    }  
 }    
}
