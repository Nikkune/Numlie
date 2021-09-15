package fr.leguiodan.numlie.utilities.enumerations;

public enum Files {
    PLAYERS("players"),
    STATS("stats"),
    CONFIG("config"),
    MESSAGES("messages");
    String fileName;

    Files(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
