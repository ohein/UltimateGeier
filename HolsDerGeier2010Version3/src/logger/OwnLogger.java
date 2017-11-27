package logger;

import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class OwnLogger {
	private static OwnLogger instance = new OwnLogger();
	private static Logger fileLogger;
	private static Logger consoleLogger;
	private static Date d = new Date();

	private OwnLogger() {
		consoleLogger = Logger.getLogger("consoleLogger");
		fileLogger  = Logger.getLogger("fileLogger");
		consoleLogger.setLevel(Level.WARN);
		fileLogger.setLevel(Level.WARN);
	}
	
	public static void setLogLevel(int warn){
		switch (warn){
		case 1: consoleLogger.setLevel(Level.FATAL);
				fileLogger.setLevel(Level.FATAL);
				break;
		case 2: consoleLogger.setLevel(Level.ERROR);
				fileLogger.setLevel(Level.ERROR);
					break;
		case 3: consoleLogger.setLevel(Level.WARN);
				fileLogger.setLevel(Level.WARN);
				break;
		case 4: consoleLogger.setLevel(Level.INFO);
				fileLogger.setLevel(Level.INFO);
				break;
		case 5: consoleLogger.setLevel(Level.DEBUG);
				fileLogger.setLevel(Level.DEBUG);
				break;
		}
	}
	
	public static Logger getSpielerLogger(){
		return fileLogger;
	}
	
	public static OwnLogger getInstance() {
		return instance;
	}

	public static void logAufConsole(String str, int warn) {
		if (consoleLogger.getAllAppenders().hasMoreElements()==false){
			SimpleLayout layout = new SimpleLayout();
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			consoleLogger.addAppender(consoleAppender);
		}
		log(consoleLogger, str, warn);
	}

	public static void logAufConsole(String str) {
		logAufConsole(str, 3);
	}

	public static void logInDatei(String str, int warn) {
		if (fileLogger.getAllAppenders().hasMoreElements()==false){
		try {
			SimpleLayout layout = new SimpleLayout();
			FileAppender fileAppender = new FileAppender(layout,
					"C:/HDG_Logs/HolsDerGeier.log", true);
			fileLogger.addAppender(fileAppender);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		}
		log(fileLogger, str, warn);
	}

	public static void logInDatei(String str) {
		logInDatei(str, 3);
	}
	
	private static void log(Logger log, String str, int warn){
		switch (warn) {
		case 1:
			log.fatal(d + ": " + str);
			break;
		case 2:
			log.error(d + ": " + str);
			break;
		case 3:
			log.warn(d + ": " + str);
			break;
		case 4:
			log.info(d + ": " + str);
			break;
		case 5:
			log.debug(d + ": " + str);
			break;
		}
	}

}
