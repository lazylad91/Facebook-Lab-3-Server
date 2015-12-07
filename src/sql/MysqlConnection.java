package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class MysqlConnection {
	
	public static Connection  getConnection(){
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Connection conn = null;
	try {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","root");
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return conn;
	}
	
	public static void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
