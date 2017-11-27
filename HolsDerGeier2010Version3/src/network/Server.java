package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import enrollGUI.NetworkEnrollDialog;
import enrollGUI.NetworkPlayerList;

import player.HolsDerGeierSpieler;

/**
 * Modelliert eines Servers, der als Thread aufgerufen werden kann.
 * die Aufgabe des Servers ist es, Serializierte Objecte über das Netzwerk 
 * zu erhalten und diese in einen Container zuspeichern.
 * Diese Klasse wurde im Zuge eine Change-Request erstellt. 
 * 
 * @author Johannes Hohmann
 * @version 1
 * @datum 25.06.2012
 */
public class Server implements Runnable{
			
	/**
	 * Variablen die benötigt werden zum Speichern des Dialogs, der Spieler
	 * die über das Netzwerk verschickt wurden sowie Netzwerk wichtige dinge.
	 *
	 */
	private ArrayList<HolsDerGeierSpieler> player = new ArrayList<HolsDerGeierSpieler>();
	private NetworkEnrollDialog dialog;
	private Socket socket = null;
	private ServerSocket serverSocket;
	private NetworkPlayerList neList;
	private InetAddress ip;
	private int port;
	
	/**
	 * Konstruktor der Klasse
	 */
	public Server(NetworkEnrollDialog dialog, NetworkPlayerList neList,InetAddress ip,int port){
		this.dialog = dialog;
		player = new ArrayList<HolsDerGeierSpieler>();
		this.neList = neList;
		this.port = port;
		this.ip = ip;
	}
	
	/**
	 * Rückgabe einer JList die dem Konstruktor übergeben wurde
	 * @return neList
	 */
	public NetworkPlayerList getNeList(){
		return neList;
	}
	
	/**
	 * Öffentliche Methode die zum Starten des Servers verwendet wird.
	 * @return void
	 */
	public void ServerStart(){
		try{
			serverSocket = new ServerSocket(port);
			while(true){
				socket = serverSocket.accept();		
				InputStream is = socket.getInputStream();
				ObjectInputStream ios = new ObjectInputStream(is);
				addPlayer((HolsDerGeierSpieler) ios.readObject());	
				neList.refreshData(player);
			}
		}catch(Exception e){
			e.printStackTrace();
			
			System.out.println("Der Port ist noch offen !!!");
		}
		socketClose();
	}
	
	/**
	 * Methode die den Spieler in dem Container steckt
	 * @return void
	 * @param Übergabe Spieler
	 */
	public void addPlayer(HolsDerGeierSpieler player){
	     this.player.add(player);
	}

	/**
	 * Methode die Spieler als Container zurück gibt
	 * @return Container Spieler
	 * @param void
	 */
	public ArrayList<HolsDerGeierSpieler> getPlayer(){
		return this.player;	
	}
	
	/**
	 * Öffentliche Methode zum Schliessen des Sockets
	 * @return void
	 * @param void
	 */
	public void socketClose(){
		try {
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Thread-Methode zum Starten des Servers
	 * @return void
	 * @param void
	 */
	@Override
	public void run() {
		ServerStart();
	}	
}
