package fr.leguiodan.numlie.utilities;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.ColorsCodes;
import fr.leguiodan.numlie.utilities.enumerations.LoggerType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Logger {
    static Date cal = new GregorianCalendar().getTime();
    static String time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cal);
    static File loggerFile = new File(Main.INSTANCE.getDataFolder(), "/logger.yml");
    static YamlConfiguration loggerYaml = YamlConfiguration.loadConfiguration(loggerFile);

    public static void init() {
        try {
            if (loggerFile.createNewFile()) {
                System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_YELLOW.getColorCode() + "Logger created: " + loggerFile.getName() + ColorsCodes.ANSI_RESET.getColorCode());
            } else {
                System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_YELLOW.getColorCode() + "Logger already exist !" + ColorsCodes.ANSI_RESET.getColorCode());
            }
        } catch (IOException e) {
            System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_RED.getColorCode() + "Erreur de creation du logger" + ColorsCodes.ANSI_RESET.getColorCode());
            e.printStackTrace();
        }
    }

    public static void log(LoggerType type, String message) {
        System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + type.getColors().getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
        loggerYaml.set(time + " - " + message, "");
        try {
            loggerYaml.save(loggerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
