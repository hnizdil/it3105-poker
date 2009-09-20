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
			conn = DriverManager.getConnection(
				"jdbc:mysql://mysql.stud.ntnu.no/hnizdil_preflop",
				"hnizdil", ""
			);
		}
		catch (Exception e) {
			System.out.println(e);
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
			int i = 0;

			String sql = "" +
				"INSERT DELAYED INTO `preflop_ec_runs` " +
				"(`ec`, `players`, `rounds`, `wins`, `ties`, `losses`) VALUES ";

			for (int[] r : results) {
				sql += "("+r[0]+","+r[1]+","+r[2]+","+r[3]+","+r[4]+","+r[5]+")";
				if (++i < results.size()) sql += ",";
			}

			conn.createStatement().executeUpdate(sql);
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
			System.out.println(e);
		}
	}
}
