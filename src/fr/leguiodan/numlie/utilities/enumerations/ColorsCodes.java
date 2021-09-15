package fr.leguiodan.numlie.utilities.enumerations;

public enum ColorsCodes {
    ANSI_RESET("\u001B[0m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_WHITE("\u001B[37m");

    private final String colorCode;

    ColorsCodes(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return this.colorCode;
    }
}
