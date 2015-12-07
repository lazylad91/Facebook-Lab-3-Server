package facebook.lab3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.*;
import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sql.MysqlConnection;

@Stateless
@WebService(portName = "UserInfoPort", serviceName = "UserInfoService", targetNamespace = "http://superbiz2.org/wsdl", endpointInterface = "facebook.lab3.UserInfoInterface")
public class UserInfoImpl implements UserInfoInterface {

	@Override
	public String saveEdu(String userId, String from, String to, String name, String empOrEdu) {
		int resultCode;
		Connection conn = MysqlConnection.getConnection();
		String query = "insert into fb_info (user_id,from_start,to_end,name,type) values(?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, from);
			preparedStatement.setString(3, to);
			preparedStatement.setString(4, name);
			preparedStatement.setString(5, empOrEdu);
			resultCode = preparedStatement.executeUpdate();
			if (resultCode == 0) {
				return "501";
			}
			
        String query2 = "select * from fb_info where user_id=?";
        PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
        preparedStatement2.setInt(1, Integer.parseInt(userId));
        ResultSet rs = preparedStatement2.executeQuery();
        JSONArray arr = new JSONArray();
        while(rs.next()){
        	JSONObject obj = new JSONObject();
        	obj.put("user_id", rs.getString("user_id"));
			obj.put("from_start", rs.getString("from_start"));
			obj.put("to_end", rs.getString("to_end"));
			obj.put("name", rs.getString("name"));
			obj.put("type", rs.getString("type"));
			arr.put(obj);
        }
        conn.close();
        return arr.toString();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "501";
			}
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
	}

	@Override
	public String saveEvent(String userId, String event, String yearOfEvent) {
		int resultCode;
		Connection conn = MysqlConnection.getConnection();
		String query = "insert into fb_event (user_id,event_name,event_year) values(?,?,?)";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, event);
			preparedStatement.setString(3, yearOfEvent);
			resultCode = preparedStatement.executeUpdate();
			if (resultCode == 0) {
				return "501";
			}
			
        String query2 = "select * from fb_event where user_id=?";
        PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
        preparedStatement2.setInt(1, Integer.parseInt(userId));
        ResultSet rs = preparedStatement2.executeQuery();
        JSONArray arr = new JSONArray();
        while(rs.next()){
        	JSONObject obj = new JSONObject();
        	obj.put("user_id", rs.getString("user_id"));
			obj.put("event_name", rs.getString("event_name"));
			obj.put("event_year", rs.getString("event_year"));
			arr.put(obj);
        }
        conn.close();
        return arr.toString();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "501";
			}
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
	}

	@Override
	public String getEvent(String userId) {
		Connection conn = MysqlConnection.getConnection();
		try{
		 String query2 = "select * from fb_event where user_id=?";
	        PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
	        preparedStatement2.setInt(1, Integer.parseInt(userId));
	        ResultSet rs = preparedStatement2.executeQuery();
	        JSONArray arr = new JSONArray();
	        while(rs.next()){
	        	JSONObject obj = new JSONObject();
	        	obj.put("user_id", rs.getString("user_id"));
				obj.put("event_name", rs.getString("event_name"));
				obj.put("event_year", rs.getString("event_year"));
				arr.put(obj);
	        }
	        conn.close();
	        return arr.toString();
			} catch (SQLException ex) {
				ex.printStackTrace();
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return "501";
				}
				return "501";
			} catch (JSONException e) {
				e.printStackTrace();
				return "501";
			}
	}

	@Override
	public String getEdu(String userId) {
		Connection conn = MysqlConnection.getConnection();
		try{
		String query2 = "select * from fb_info where user_id=?";
        PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
        preparedStatement2.setInt(1, Integer.parseInt(userId));
        ResultSet rs = preparedStatement2.executeQuery();
        JSONArray arr = new JSONArray();
        while(rs.next()){
        	JSONObject obj = new JSONObject();
        	obj.put("user_id", rs.getString("user_id"));
			obj.put("from_start", rs.getString("from_start"));
			obj.put("to_end", rs.getString("to_end"));
			obj.put("name", rs.getString("name"));
			obj.put("type", rs.getString("type"));
			arr.put(obj);
        }
        conn.close();
        return arr.toString();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "501";
			}
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
	}

}
