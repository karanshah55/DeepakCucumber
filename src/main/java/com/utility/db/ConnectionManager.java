/*
 *
 */
package com.utility.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.utility.selenium.BaseTestScript;

/**
 * The Class ConnectionManager.
 */
public class ConnectionManager
{

	/** The DB URL variable. */
	private static String dburl;

	/** The Constant ORACLE_CONNECTION_STRING. */
	private static final String ORACLE_CONNECTION_STRING="oracle.jdbc.driver.OracleDriver";

	/** The Constant ORACLE_CONNECTION_URL. */
	private static final String ORACLE_CONNECTION_URL="jdbc:oracle:thin:@";

	/** The Constant SQL_CONNECTION_STRING. */
	private static final String SQL_CONNECTION_STRING="com.microsoft.sqlserver.jdbc.SQLServerDrive";

	/** The Constant SQL_CONNECTION_URL. */
	private static final String SQL_CONNECTION_URL="jdbc:sqlserver://";

	/** The logger. */
	static Logger logger = Logger.getLogger(ConnectionManager.class);

	/**
	 * Gets the DB connection.
	 * @author GS-1629
	 * @return the connection
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException
	{
		dburl = ORACLE_CONNECTION_URL + BaseTestScript.DBDOMAIN + ":" + BaseTestScript.DBPORT+":xe";
		Connection con = null;
		dburl = dburl + ";databaseName=" + BaseTestScript.DBNAME + ";user=" + BaseTestScript.DBUSERNAME + ";password=" + BaseTestScript.DBPASSWORD;

		// load the Driver Class
		Class.forName(ORACLE_CONNECTION_STRING);
		// create the connection now
		con = DriverManager.getConnection(dburl);
		return con;
	}

	/**
	 * Close DB Connection.
	 *
	 * @author GS-1629
	 * @param con the con
	 * @return void
	 */
	protected static void closeDbConnection(Connection con)
	{
		try
		{
			if (con != null) {
				con.close();
			}
		}
		catch (SQLException se)
		{
			logger.error("", se);
		}

	}

	/**
	 * Creates the DB.
	 * @author GS-1629
	 * @param dbName the db name
	 * @return void
	 */
	public static synchronized void createDB(String dbName)
	{
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(SQL_CONNECTION_STRING);

			// STEP 3: Open a connection
			logger.info("Connecting to database...");
			conn = DriverManager.getConnection(dburl, BaseTestScript.DBUSERNAME, BaseTestScript.DBPASSWORD);

			// STEP 4: Execute a query
			logger.info("Creating database...");
			stmt = conn.createStatement();

			stmt.executeUpdate(dbName);
			logger.info("Database created successfully...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			logger.info(se.toString());
		} catch (Exception e) {
			// Handle errors for Class.forName
			logger.info(e.toString());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se2) {
				// nothing we can do
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				logger.info(se.getMessage());
			} // end finally try
		} // end try
	}
}
