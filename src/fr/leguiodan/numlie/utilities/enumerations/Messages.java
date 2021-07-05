package fr.leguiodan.numlie.utilities.enumerations;

public enum Messages {
	Database_Connected("databaseOk"),
	Account_Created("accountCreate"),
	Account_Updated("accountUpdate"),
	Cash_Created("cashCreate"),
	Level_Saved("statsYamlOk"),
	Level_Not_Saved("statsYamlNo"),
	Congratulations("congratulations"),
	Shame("shame"),
	Level_Up("levelPlus"),
	Status_Up("statusPlus"),
	Xp_Up("xpPlus"),
	Xp_Down("xpMinus"),
	Backup_Error("backupErr"),
	Backup_Success("backupOk"),
	Delete_Error("deleteErr"),
	Delete_Success("deleteOk"),
	UI_Level("UILevel"),
	UI_Money("UIMoney"),
	UI_Playtime("UIPlaytime"),
	Join_Instance("instanceJoin"),
	Already_In_Instance("instanceHas"),
	Wrong_Instance_Code("instanceCodeErr"),
	;
	final String key;

	Messages(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
