package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * FileFilter fuer HolsDerGeier-Savegames
 * @author Gruppe 2
 *
 */
public class BuzzardSavegameFileFilter extends FileFilter {

	/**
	 * Vorgeschlagene Dateiendung fuer Savegames
	 */
	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(".hdg");
	}

	/**
	 * Beschreibungstext
	 */
	@Override
	public String getDescription() {
		return "HolsDerGeier-Savegame (*.hdg)";
	}

}
