package fr.leguiodan.numlie.utilities;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.ColorsCodes;
import fr.leguiodan.numlie.utilities.enumerations.LoggerType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Logger {
    static Date cal = new GregorianCalendar().getTime();
    static String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(cal);
    static File loggerFile = new File(Main.INSTANCE.getDataFolder(), "/logger.txt");
    static FileWriter loggerWriter;

    Logger() {
        try {
            if (loggerFile.createNewFile()) {
                System.out.println("File created: " + loggerFile.getName());
                loggerWriter = new FileWriter(loggerFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void log(LoggerType type, String message) {
        System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + type.getColors() + message + ColorsCodes.ANSI_RESET.getColorCode());
        try {
            loggerWriter.write(time + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
