package fr.leguiodan.numlie.utilities.database;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private final DbCredentials dbCredentials;
	private final Main main;
	private Connection connection;

	public DbConnection(DbCredentials dbCredentials, Main main)
	{
		this.dbCredentials = dbCredentials;
		this.main = main;
		this.connect();
	}

	private void connect()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(this.dbCredentials.toURI(), this.dbCredentials.getUser(), this.dbCredentials.getPass());

			final String lang = main.filesManager.getLanguage();
			Logger.logSuccess(main.filesManager.getMessage(Messages.Database_Connected, lang));
		} catch (SQLException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void close() throws SQLException
	{
		if (this.connection != null)
		{
			if (!this.connection.isClosed())
			{
				this.connection.close();
			}
		}
	}

	public Connection getConnection() throws SQLException
	{
		if (this.connection != null)
		{
			if (!this.connection.isClosed())
			{
				return this.connection;
			}
		}
		connect();
		return this.connection;
	}
}
