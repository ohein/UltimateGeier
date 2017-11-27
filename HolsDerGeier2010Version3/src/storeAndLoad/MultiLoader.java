package storeAndLoad;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.*;
import player.*;

public class MultiLoader {
	
	@Deprecated
	public static void loadClass(File directory, String fileName){
		Class<?> c = null;
		try{
			URL url = directory.toURI().toURL();
			URLClassLoader cl = new URLClassLoader(new URL[]{url});
			System.out.print("FileName: ");
			System.out.println(fileName);
			c = cl.loadClass(fileName);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		System.out.println(c.toString());
	}
	


	
	/** Erzeugt eine neue Instanz einer Buzzard-Player Klasse mit individuell festgelegtem Namen. 
	 * Diese Methode wird aufgerufen, wenn vom Benutzer kein Pfad fuer Spieler/Strategie-Klassen
	 * angegebe wurde.
	 * @param className Name der zu instanzierenden Klasse
	 * @param objectName Frei wählbarer Name, den die Spieler-Instanz erhalten soll
	 */
	public static Object newInstance(String className, String objectName){
			return newInstance(null, className, objectName);
	}
	
	
	/** Erzeugt eine neue Instanz einer Buzzard-Player-Klasse mit individuell festgelegtem Namen.
	 * Diese Methode wird mit loadPath=null aufgerufen, wenn vom Benutzer kein Pfad fuer 
	 * Spieler/Strategie-Klassen definiert wurde. 
	 * @param loadPath Der Pfad, unter dem die zu instanzierende Klasse abgelegt ist
	 * @param className Name der zu instanzierenden Klasse
	 * @param objectName Frei wählbarer Name, den die Spieler-Instanz erhalten soll
	 */
	public static Object newInstance(String loadPath, String className, String objectName){
		Class<?> c = null;
		Object res;
		URLClassLoader cl=null;
		String fileName=null;
		try{
			/* Damit der Ladevorgang funktioniert, muss die ".class"-Erweiterung abgeschnitten werden*/
			fileName = className.substring(0,className.lastIndexOf("."));
			
			System.out.println(fileName);
			/* Aus dem loadPath wird eine URL erzeugt, in der der URLClassLoader nach der zu instanziierenden KLasse suchen wird */
			File file2;
			URL url2;
			// Wenn vom Benutzer ein eigener Pfad fuer Spieler/Strategie-Klassen definiert wurde:
			if(loadPath!=null){
				URL url = new File(loadPath).toURI().toURL();
				file2 = new File("src/standardPlayers");
				//file2 = new File("../standardPlayers");
				url2 = file2.toURI().toURL();
				//url2 = MultiLoader.class.getResource("player");
				System.out.println(url2);
				cl = new URLClassLoader(new URL[]{url2,url});
			}
			// Wenn der Benutzer keinen eigenen Pfad fuer Spieler/Strategie-Klassen angegeben hat....
			else{
				//file2 = new File("../standardPlayers");
				//url2 = file2.toURI().toURL();
				
				url2 = Thread.currentThread().getContextClassLoader().getResource("/player");
				cl = new URLClassLoader(new URL[]{url2});
				System.out.println("URL: "+url2);
			}
			
			/* Im naechsten Schritt wird das Klassenobjekt geladen*/
			c = cl.loadClass(fileName);
			System.out.println("Class " + fileName + " loaded successfully");
			System.out.println("In Variante 1");
			/* Teil der Reflection-API: Wir besorgen uns saemtliche verfuegbare Konstruktoren*/
			Constructor[] constructors = c.getConstructors();
			for(int i=0;i<constructors.length;i++){
				if(constructors[i].getParameterTypes().length!=1){	// Wenn der Konstruktor mehr als einen Parameter erwartet, dann 
					System.err.println("Sie brauchen einen Konstruktor, der einen Strng als Parameter entgegennimmt");
					//=*=*=*=*=*= Ab hier hab ich rumgespielt *=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=
					try {
						Object player = constructors[i].newInstance();
						HolsDerGeierSpieler playerRes = (HolsDerGeierSpieler)player;
						playerRes.setName(objectName);
						return playerRes;
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					//=*=*=*=*=*= Ab hier hab ich rumgespielt *=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=
					continue;									    // handelt es sich nicht um den Konstruktor, den wir benötigen
				}
				else{
					Class[] parame = constructors[i].getParameterTypes();
					if(!(parame[0].getName().equals("java.lang.String"))){ // Wenn es sich bei dem einen vom Konstruktor er-
						System.err.println(parame[0].getName());
						System.err.println("Continue2");
						continue;              // warteten Parameter nicht um einen String handelt, sind wir beim falschen Konst
					}else{
						try{
							return constructors[i].newInstance(objectName);
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}
		System.err.println("Hier sollte ich nicht sein....");
		return null;
		}catch(MalformedURLException me){
			me.printStackTrace();
		}
		catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		catch(NoClassDefFoundError err){
			try{
				c = cl.loadClass("player."+fileName);
				System.out.println("Class " + fileName + " loaded successfully");
				System.out.println("In Variante 2!");
				Constructor[] constructors = c.getConstructors();
				for(int i=0;i<constructors.length;i++){
					if(constructors[i].getParameterTypes().length!=1){	// Wenn der Konstruktor mehr als einen Parameter erwartet, dann 
						//=*=*=*=*=*= Ab hier hab ich rumgespielt *=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=
						try {
							Object player = constructors[i].newInstance();
							HolsDerGeierSpieler playerRes = (HolsDerGeierSpieler)player;
							playerRes.setName(objectName);
							return playerRes;
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
						//=*=*=*=*=*= Ab hier hab ich rumgespielt *=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=
					else{
						Class[] parame = constructors[i].getParameterTypes();
						if(!(parame[0].getName().equals("java.lang.String"))){ // Wenn es sich bei dem einen vom Konstruktor er-
							System.err.println(parame[0].getName());
							System.err.println("Continue2");
							continue;              // warteten Parameter nicht um einen String handelt, sind wir beim falschen Konst
						}else{
							try{
								return constructors[i].newInstance(objectName);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			}catch(ClassNotFoundException ee){
				ee.printStackTrace();
			}
		}
		return null;
	}
	
	@Deprecated
	public static Object newInstance(File f){
		Class<?> c;
		Object res;
		try{
			/* Pfad, unter dem die zu ladende Datei zu finden ist*/
			File parent = f.getParentFile();
			/* Dateiname plus Typbezeichnung*/
			String fileNameFull = f.getName();
			/* Dateiname ohne Typbezeichnung*/
			String fileName = fileNameFull.substring(0,fileNameFull.lastIndexOf("."));
			
			System.out.println("fileNameFull: " + fileNameFull);
			
			System.out.println("Trying to load " + fileName + " from " + parent);
			/* Umwandeln des Pfades, unter dem die zu ladende Datei liegt in ein 
			 * URL-Objekt			 */
			URL url= parent.toURI().toURL();
			
			URLClassLoader cl = new URLClassLoader(new URL[]{url});
			c = cl.loadClass(fileName);
			res = c.newInstance();
			return res;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	

}
