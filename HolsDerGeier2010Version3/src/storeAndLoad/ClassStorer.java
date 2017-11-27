package storeAndLoad;

import java.io.*;


public class ClassStorer {
	
	public static void storeClass(File input, String outputPath){
		storeClass(input.getPath(),outputPath);
	}
	
	public static void storeClass(String inputFile, String outputPath){
		byte[] data = new byte[4096];
		try{
			/*Name der Datei, die kopiert werden soll*/
			String filename = inputFile.substring(inputFile.lastIndexOf(System.getProperty("file.separator"))+1);
			/*File bestehend aus Pfad + Dateiname, unter dem die zu kopierende Datei gespeichert werden soll*/
			File storePath = new File(outputPath + System.getProperty("file.separator")+filename);
			System.out.println("Trying to store " + inputFile + " to " + 
					storePath.getAbsolutePath());
			System.out.println("Filename of class to be stored is: " + filename);
			FileInputStream in = new FileInputStream(inputFile);
			FileOutputStream out = new FileOutputStream(storePath,true);
			while(in.read(data)>0){
				out.write(data);
			}
			in.close();
			out.close();
			System.out.println("ClassStorer: Storing process completed successfully.");
		}catch(FileNotFoundException e){
			System.err.println("Datei konnte nicht gefunden werden");
			e.printStackTrace();
		}catch(IOException ioe){
			System.err.println("Upps, IOException");
			ioe.printStackTrace();
		}
			
	}

}
