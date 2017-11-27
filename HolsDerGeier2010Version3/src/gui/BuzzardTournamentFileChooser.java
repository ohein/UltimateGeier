package gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import storeAndLoad.TournamentSavingUtil;
import controller.BuzzardTournamentInfo;

/**
 * Auswahldialog zum Laden und Speichern von HolsDerGeier-Savegames auf der Festplatte.
 * Die De-/Serialisierung erfolgt mittels TournamentSavingUtil.
 * Die Dateiauswahl erfolgt durch die Elternklasse JFileChooser.
 * @author Gruppe 2
 *
 */
public class BuzzardTournamentFileChooser extends JFileChooser {

	public BuzzardTournamentFileChooser() {
		this.setMultiSelectionEnabled(false);
		this.setFileFilter(new BuzzardSavegameFileFilter());
	}
	
	/**
	 * Speichert das uebergebene Turnier mit Dateiauswahl und TournamentSavingUtil auf die Festplatte
	 * @param info das zu speichernde Turnier
	 * @throws IOException
	 */
	public void saveTournament(BuzzardTournamentInfo info) throws IOException{
		int selected = this.showSaveDialog(null);
		if (selected==JFileChooser.APPROVE_OPTION){
			System.out.println("Speicherort gewählt");
			File selectedFile=this.getSelectedFile();
			TournamentSavingUtil.save(selectedFile, info);
		}

	}
	
	/**
	 * Laedt ein Turnier mit Dateiauswahl und TournamentSavingUtil von der Festplatte
	 * @return Turnierkonstellation, oder null, wenn abgebrochen wurde
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public BuzzardTournamentInfo loadTournament() throws IOException, ClassNotFoundException {
		int selected = this.showOpenDialog(null);
		if (selected == JFileChooser.APPROVE_OPTION) {
			System.out.println("Hols der Geier Datei ausgewählt");
			File selectedFile = this.getSelectedFile();
			if (selectedFile.canRead() && selectedFile.exists()) {
				BuzzardTournamentInfo info = TournamentSavingUtil.load(selectedFile);
				return info;
			}
		}
		return null;
	}
}
