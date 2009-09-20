import java.sql.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Hashtable;

public class MysqlConnection
{
	private static MysqlConnection instance = null;
	private static Connection conn = null;

	private MysqlConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/hnizdil_preflop", "hnizdil", "");
		}
		catch (Exception e) {
		}
	}

	public static MysqlConnection getInstance()
	{
		if (instance == null) instance = new MysqlConnection();
		return instance;
	}

	public static void saveResults(ArrayList<int[]> results)
	{
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(
				"INSERT DELAYED INTO `preflop_ec_runs` (`ec`, `players`, `rounds`, `wins`, `ties`, `losses`) " +
				"VALUES (?, ?, ?, ?, ?, ?)"
			);

			for (int[] result : results) {
				// Save ec, players, runs, wins, ties and losses
				for (int i = 0; i < result.length; i++) stmt.setInt(i+1, result[i]);
				stmt.executeUpdate();
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	protected void finalize()
	{
		try {
			conn.close();
		}
		catch (SQLException e) {
		}
	}
}
