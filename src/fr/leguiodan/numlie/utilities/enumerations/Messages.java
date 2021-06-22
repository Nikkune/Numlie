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
