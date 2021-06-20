package fr.leguiodan.numlie.utilities.Database;

import fr.leguiodan.numlie.utilities.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private final DbCredentials dbCredentials;
	private Connection connection;

	public DbConnection(DbCredentials dbCredentials)
	{
		this.dbCredentials = dbCredentials;
		this.connect();
	}

	private void connect()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(this.dbCredentials.toURI(), this.dbCredentials.getUser(), this.dbCredentials.getPass());

			Logger.logSuccess("Successfully connected to the Database !");
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
