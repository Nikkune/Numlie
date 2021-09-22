package fr.leguiodan.numlie.utilities.enumerations;

public enum Messages {
    Database_Connected("databaseOk"),
    Database_Disconnected("databaseDisconnected"),

    Account_Created("accountCreate"),
    Account_Updated("accountUpdate"),

    Cash_Created("cashCreate"),

    Level_Saved("statsYamlOk"),
    Backup_Error("backupErr"),
    Backup_Success("backupOk"),

    Congratulations("congratulations"),
    Shame("shame"),
    Level_Up("levelPlus"),
    Status_Up("statusPlus"),
    Xp_Up("xpPlus"),
    Xp_Down("xpMinus"),
    Chat_Selector("chatSelector"),
    Player_Offline("playerOffline"),
    Command_Error("commandError"),
    Successful_Add("successfulAdd"),
    Successful_Add_1("successfulAdd1"),
    Successful_Remove("successfulRemove"),
    Successful_Remove_1("successfulRemove"),
    Successful_Set("successfulSet"),
    Successful_Set_1("successfulSet"),

    Party_No("PartyNo"),
    Party_Invited("partyInvited"),
    Party_Invite("partyInvite"),
    Party_Created("partyCreate"),
    Party_Dissolved("partyDissolved"),
    Party_Joined("partyJoined"),
    Party_Joined_1("partyJoined1"),
    Party_Leave("partyLeave"),
    Party_Left("partyLeft"),

    UI_Level("UILevel"),
    UI_Playtime("UIPlaytime"),
    UI_Change_Lang("UIChangeLang"),
    UI_Change_Title("UIChangeTitle"),
    UI_Title("UITitle"),
    UI_Chat("UIChat"),
    UI_Get_Link_Key("UIGetLinkKey"),
    ;
    final String key;

    Messages(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
