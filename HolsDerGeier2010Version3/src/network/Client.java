package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import player.HolsDerGeierSpieler;
 
/**
 * Modelliert eines Clients, der als Thread aufgerufen werden kann.
 * Die Aufgabe des Client ist es, objecte zu Serializieren und über das Netzwerk 
 * an einen Server zuschicken. 
 * Diese Klasse wurde im Zuge eines Change Requests erstellt.
 * 
 * @author Johannes Hohmann
 * @version 1
 * @datum 12.03.2012
 */

public class Client implements Runnable {
	/**
	 * Variablen für den Client
	 * @return void
	 * @param void
	 */
	HolsDerGeierSpieler player = null;
	private InetAddress ip;
	private int port;
	

	/**
	 * Konstruktor des Clients
	 * @return void
	 * @param void
	 */
	public Client(HolsDerGeierSpieler player,InetAddress ip,int port){
		this.player = player;
		this.ip = ip;
		this.port = port;
	}	
	

	/**
	 * Methode zum Aufrufen des Clients 
	 * @return void
	 * @param void
	 */
	private void clientStart(){
		try {
			Socket s = new Socket(ip, port);
			
			
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(player);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Thread-Methode zum Starten des Clients
	 * @return void
	 * @param
	 */ 
	@Override
	public void run() {
		clientStart();
	}	
	
}