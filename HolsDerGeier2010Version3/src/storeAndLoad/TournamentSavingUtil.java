package storeAndLoad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import controller.BuzzardTournamentInfo;

/**
 * Ist fuer das Speichern von Turnieren auf die Festplatte verantwortlich
 * @author Gruppe 2
 *
 */
public class TournamentSavingUtil {

	/**
	 * Speichert das Turnier auf die Festplatte
	 * @param selectedFile Datei, in die gespeichert werden soll
	 * @param info Das Turnier
	 * @throws IOException Wird geworfen, wenn nicht gespeichert werden konnte oder null übergeben wird
	 */
	public static void save(File selectedFile, BuzzardTournamentInfo info) throws IOException{
		if(info==null||selectedFile==null) throw new IllegalArgumentException("null value");
		String path=selectedFile.getAbsolutePath();
		if(!path.endsWith(".hdg")) {
			path=path+".hdg";
			selectedFile=new File(path);
		}
		try {
			FileOutputStream fileOut=new FileOutputStream(selectedFile);
			ObjectOutputStream objOut=new ObjectOutputStream(fileOut);
			objOut.writeObject(info);
			objOut.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Fehler beim Speichern!");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Laedt ein Turnier von der Festplatte
	 * @param file Datei, in der das Turnier gespeichert wurde
	 * @return Turnierkonstellation mit allen Daten, Spielern und bereits gespielten Spielen
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static BuzzardTournamentInfo load(File file) throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			BuzzardTournamentInfo info = (BuzzardTournamentInfo) ois.readObject();
			info.setDeserialized(true);
			return info;
		} catch (IOException e) {
			System.err.println("Exception occured while trying to load serialized tournament file. Message=" + e.getMessage());
			throw e;
		} catch (ClassNotFoundException e) {
			System.err.println("Exception occured while trying to deserialize tournament file. Message=" + e.getMessage());
			throw e;
		}
	}
}
