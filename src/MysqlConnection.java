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

	public static void saveResults(ArrayList<Hashtable<Integer,int[]>> results)
	{
		PreparedStatement stmt;

		/*
		stmt = conn.prepareStatement(
			"INSERT INTO `preflop_ec_runs` (`ec`, `players`, `runs`, `wins`, `ties`, `losses`) " +
			"VALUES (?, ?, ?, ?, ?)"
		);
		*/

		for (int i = 0; i < results.size(); i++) {
			Hashtable<Integer,int[]> ht = results.get(i);
			Enumeration e = ht.keys();
			while (e.hasMoreElements()) {
				Integer key = (Integer)e.nextElement();
				System.out.print(i + " - " + key + ":\t");
				for (int el : ht.get(key)) {
					System.out.print(el + "\t");
				}
				System.out.println();
			}
		}
	}
}
