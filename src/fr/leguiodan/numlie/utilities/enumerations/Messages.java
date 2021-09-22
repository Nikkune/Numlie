package fr.leguiodan.numlie.utilities.enumerations;

public enum Messages {
    Database_Connected("databaseOk"),
    Account_Created("accountCreate"),
    Account_Updated("accountUpdate"),
    Cash_Created("cashCreate"),
    Level_Saved("statsYamlOk"),
    Congratulations("congratulations"),
    Shame("shame"),
    Level_Up("levelPlus"),
    Status_Up("statusPlus"),
    Xp_Up("xpPlus"),
    Xp_Down("xpMinus"),
    Backup_Error("backupErr"),
    Backup_Success("backupOk"),
    UI_Level("UILevel"),
    UI_Playtime("UIPlaytime"),
    UI_Change_Lang("UIChangeLang"),
    UI_Change_Title("UIChangeTitle"),
    UI_Title("UITitle"),
    UI_Chat("UIChat"),
    UI_Get_Link_Key("UIGetLinkKey"),
    Chat_Selector("ChatSelector"),
    Party_No("PartyNo"),
    ;
    final String key;

    Messages(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
