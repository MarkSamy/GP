package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import model.Files;

public class Database {

	private Statement stmt = null;
	private Connection c = null;
	private static final int FLUSH_AMOUNT = 100000;

	public Database() throws ClassNotFoundException, SQLException {
		c = null;
		stmt = null;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:test.db");
		c.setAutoCommit(false);
		stmt = c.createStatement();
	}

	public void createTables() throws SQLException {
		String sql = "CREATE TABLE nodes "
				+ "( ID TEXT PRIMARY KEY NOT NULL, Lat  REAL  NOT NULL , Lon  REAL  NOT NULL )";
		stmt.executeUpdate(sql);
		stmt.close();
		sql = "CREATE TABLE edges ( ID1 TEXT NOT NULL , ID2   TEXT   NOT NULL)";
		stmt.executeUpdate(sql);
		stmt.close();
		c.commit();
	}

	public void loadEdges() throws SQLException, IOException {
		String query = "insert into edges values (?,?);";
		PreparedStatement ps = c.prepareStatement(query);
		BufferedReader br = new BufferedReader(new FileReader(Files.SORTED_EDGES));
		String line = br.readLine();
		int batchsize = 1000000;
		int k = 1;
		while (line != null) {
			String[] matches = line.split(";");
			ps.setString(1, matches[0]);
			ps.setString(2, matches[1]);
			ps.addBatch();
			k++;
			if (k % batchsize == 0) {
				ps.executeBatch();
				c.commit();
				k = 0;
			}
			line = br.readLine();
		}
		br.close();
		ps.executeBatch();
		c.commit();
		ps.close();
	}

	public void loadNodes() throws SQLException, IOException {
		String query = "insert into nodes values (?,?,?);";
		PreparedStatement ps = c.prepareStatement(query);
		BufferedReader br = new BufferedReader(new FileReader(Files.NODES_LON_LAT));
		String line = br.readLine();
		int batchsize = 1000000;
		int k = 1;
		while (line != null) {
			String[] matches = line.split(";");
			ps.setString(1, matches[0]);
			ps.setDouble(2, Double.parseDouble(matches[1]));
			ps.setDouble(3, Double.parseDouble(matches[2]));
			ps.addBatch();
			k++;
			if (k % batchsize == 0) {
				ps.executeBatch();
				c.commit();
				k = 0;
			}
			line = br.readLine();
		}
		br.close();
		ps.executeBatch();
		c.commit();
		ps.close();
	}

	public void join() throws SQLException, IOException {
		c.setAutoCommit(false);
		String sql = "SELECT e.id1,n1.lat,n1.lon,e.id2,n2.lat,n2.lon FROM Edges e INNER JOIN Nodes n1 ON e.ID1 = n1.id "
				+ " INNER JOIN Nodes n2 " + " ON e.ID2 = n2.id; ";
		ResultSet rs = stmt.executeQuery(sql);
		c.commit();
		PrintWriter joinWriter = new PrintWriter(new FileWriter(Files.JOIN_FILE));
		int counter = 0;
		while (rs.next()) {
			String id1 = rs.getString(1);
			Double id1Lat = rs.getDouble(2);
			Double id1Lon = rs.getDouble(3);
			String id2 = rs.getString(4);
			Double id2Lat = rs.getDouble(5);
			Double id2Lon = rs.getDouble(6);
			joinWriter.println(id1 + ";" + id1Lat + ";" + id1Lon + ";" + id2 + ";" + id2Lat + ";" + id2Lon);
			counter++;
			if (counter % FLUSH_AMOUNT == 0) {
				counter = 0;
				joinWriter.flush();
			}
		}
		stmt.close();
		joinWriter.close();
		c.close();
	}
	
	public void loadWeightedEdges() throws SQLException, IOException{
		String sql = "CREATE TABLE weightedEdges " + "(ID1 TEXT NOT NULL," + " ID2 TEXT NOT NULL, " + " weight REAL NOT NULL)";
		stmt.executeUpdate(sql);
		stmt.close();
		String query = "insert into weightedEdges values (?,?,?);";
		PreparedStatement ps = c.prepareStatement(query);
		BufferedReader br = new BufferedReader(new FileReader(Files.WEIGHTED_EDGES_FILE_SORTED));
		String line = br.readLine();
		int batchsize = 1000000;
		int k = 1;
		while (line != null) {
			String[] matches = line.split(";");
			ps.setString(1, matches[0]);
			ps.setString(2, matches[1]);
			ps.setDouble(3, Double.parseDouble(matches[2]));
			ps.addBatch();
			k++;
			if (k % batchsize == 0) {
				ps.executeBatch();
				c.commit();
				k = 0;
			}
			line = br.readLine();
		}
		br.close();
		ps.executeBatch();
		c.commit();
		ps.close();
	}
	
	public Double getWeights(String id1, String id2) throws SQLException {
		c.setAutoCommit(false);
		String sql = "select weight from weightedEdges where ID1 = '" + id1 + "' and ID2 = '" + id2 + "'  ";
		ResultSet rs = stmt.executeQuery(sql);
		c.commit();
		while (rs.next()) {
			return rs.getDouble("Weight");
		}
		stmt.close();
		return null;
	}

	public ArrayList<String> selectWithID(String id1) throws SQLException {
		String sql = "select ID2 from weightedEdges where ID1 = '" + id1 + "'";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> res = new ArrayList<String>();
		while (rs.next()) {
			String dest = rs.getString("ID2");
			res.add(dest);
		}
		stmt.close();
//		c.close();
		return res;
	}

}
