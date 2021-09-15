package fr.leguiodan.numlie.utilities.enumerations;

public enum LoggerType {
    NORMAL(ColorsCodes.ANSI_WHITE),
    WARNING(ColorsCodes.ANSI_YELLOW),
    SUCCESS(ColorsCodes.ANSI_GREEN),
    ERROR(ColorsCodes.ANSI_RED);
    ColorsCodes colors;

    LoggerType(ColorsCodes colors) {
        this.colors = colors;
    }

    public ColorsCodes getColors() {
        return colors;
    }
}
