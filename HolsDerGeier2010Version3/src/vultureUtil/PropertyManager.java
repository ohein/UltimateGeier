package vultureUtil;

import java.io.*;
import java.util.Properties;

public class PropertyManager {
	public static void storeKeyVal(String key, String val){
		try{
			File f = new File("src/data/properties.txt");
			FileInputStream in = new FileInputStream(f);
			Properties prop = new Properties();
			prop.load(in);
			prop.setProperty(key, val);
			FileOutputStream out = new FileOutputStream(f);
			prop.store(out,"Beat the Buzzard");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key){
		try{
			File f = new File("src/data/properties.txt");
			FileInputStream in = new FileInputStream(f);
			Properties prop = new Properties();
			prop.load(in);
			in.close();
			return prop.getProperty(key);
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException ie){
			ie.printStackTrace();
		}
		return null;
	}
	

}
