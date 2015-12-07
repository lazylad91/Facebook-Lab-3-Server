package facebook.lab3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sql.MysqlConnection;

@Stateless
@WebService(portName = "UserInfoPort", serviceName = "UserFriendService", targetNamespace = "http://superbiz9.org/wsdl", endpointInterface = "facebook.lab3.UserFriendInterface")
public class UserFriendImpl implements UserFriendInterface {

	@Override
	public String checkAddFriend(String logInUserId, String profUserId) {
		// Case 1 
		if(logInUserId.equals(profUserId)){
			 JSONObject obj = new JSONObject();
			 try {
				obj.put("same", "true");
				 obj.put("pending", "false");
				 obj.put("friends", "false");
				 obj.put("add", "false");
				 return obj.toString();
			} catch (JSONException e) {
				e.printStackTrace();
				return "501";
			}
		 }
		// Case 2
		Connection conn = MysqlConnection.getConnection();
		PreparedStatement preparedStatement;
		try {
		String query1 ="select * from fb_friend where (user_id=? and friend_user_id=?) or (user_id=? and friend_user_id=?) " ;
		preparedStatement = conn.prepareStatement(query1);
		preparedStatement.setString(1, logInUserId);
		preparedStatement.setString(2, profUserId);
		preparedStatement.setString(3, profUserId);
		preparedStatement.setString(4, logInUserId);
		ResultSet rs = preparedStatement.executeQuery();
		rs.last();
		if(rs.getRow()==2){
			JSONObject obj = new JSONObject();
			obj.put("same", "false");
			 obj.put("pending", "false");
			 obj.put("friends", "true");
			 obj.put("add", "false");
			 return obj.toString();
		}
		}
		catch(SQLException e){
			e.printStackTrace();
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
		
		// case 3
		PreparedStatement preparedStatement1;
		try {
		String query2 ="select * from fb_pending where pending_user_id=? and pending_friend_id=?";
		preparedStatement1 = conn.prepareStatement(query2);
		preparedStatement1.setString(1, logInUserId);
		preparedStatement1.setString(2, profUserId);
		ResultSet rs = preparedStatement1.executeQuery();
		rs.last();
		if(rs.getRow()>0){
			JSONObject obj = new JSONObject();
			obj.put("same", "false");
			 obj.put("pending", "true");
			 obj.put("friends", "false");
			 obj.put("add", "false");
			 return obj.toString();
		}
		else{
			JSONObject obj = new JSONObject();
			obj.put("same", "false");
			 obj.put("pending", "false");
			 obj.put("friends", "false");
			 obj.put("add", "true");
			 return obj.toString();
		}
		}
		catch(SQLException e){
			e.printStackTrace();
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
		 
		 
		
	}

	@Override
	public String sendFriendRequest(String logInUserId, String profUserId) {
		int rs;
	String query1 = "insert into fb_pending (pending_user_id,pending_friend_id) values (?,?)";
	Connection conn = MysqlConnection.getConnection();
	try{
	PreparedStatement preparedStatement;
	preparedStatement = conn.prepareStatement(query1);
	preparedStatement.setString(1, logInUserId);
	preparedStatement.setString(2, profUserId);
	rs = preparedStatement.executeUpdate();
	if(rs==1){
		JSONObject obj = new JSONObject();
		obj.put("same", "false");
		 obj.put("pending", "true");
		 obj.put("friends", "false");
		 obj.put("add", "false");
		 conn.close();
		 return obj.toString();
	}
	}
	catch(SQLException e){
		e.printStackTrace();
		return "501";
	} catch (JSONException e) {
		e.printStackTrace();
		return "501";
	}
		return null;
	}

	@Override
	public String getPending(String userId) {
		String query1 = "select first_name,user_id from fb_user where user_id in (select pending_user_id from fb_pending where pending_friend_id= ?)" ;
		Connection conn = MysqlConnection.getConnection();
		try{
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(query1);
		preparedStatement.setString(1, userId);
		ResultSet rs = preparedStatement.executeQuery();
		JSONArray arr = new JSONArray();
        while(rs.next()){
        	JSONObject obj = new JSONObject();
        	obj.put("first_name", rs.getString("first_name"));
			obj.put("user_id", rs.getString("user_id"));
			arr.put(obj);
        }
        conn.close();
			 return arr.toString();
		
		}
		catch(SQLException e){
			e.printStackTrace();
			return "501";
		} catch (JSONException e) {
			e.printStackTrace();
			return "501";
		}
	}

	@Override
	public String acceptReq(String userId, String friendId) {
		int rs;
		String query = "insert into fb_friend (user_id,friend_user_id) values (?,?),(?,?) ";
		Connection conn = MysqlConnection.getConnection();
		try{
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, friendId);
			preparedStatement.setString(3, friendId);
			preparedStatement.setString(4, userId);
			 rs = preparedStatement.executeUpdate();
			if(rs>0){
				String query2 = "delete from fb_pending where pending_friend_id = ? and pending_user_id = ?";
				PreparedStatement preparedStatement1;
				preparedStatement1 = conn.prepareStatement(query2);
				preparedStatement1.setString(1, userId);
				preparedStatement1.setString(2, friendId);
				 rs = preparedStatement.executeUpdate();
				 return "200";
			}
	        conn.close();
			
			
			}
			catch(SQLException e){
				e.printStackTrace();
				return "501";
			} 
		return null;
	}

	@Override
	public String getFriend(String userId, String friendId) {
	String query = "select user_id,first_name from fb_user where user_id in (select friend_user_id from fb_friend where user_id = ?)";
	Connection conn = MysqlConnection.getConnection();
	try{
	PreparedStatement preparedStatement;
	preparedStatement = conn.prepareStatement(query);
	preparedStatement.setString(1, userId);
	ResultSet rs = preparedStatement.executeQuery();
	JSONArray arr = new JSONArray();
    while(rs.next()){
    	JSONObject obj = new JSONObject();
    	obj.put("first_name", rs.getString("first_name"));
		obj.put("user_id", rs.getString("user_id"));
		arr.put(obj);
    }
    conn.close();
		 return arr.toString();
	
	}
	catch(SQLException e){
		e.printStackTrace();
		return "501";
	} catch (JSONException e) {
		e.printStackTrace();
		return "501";
	}
	}
	
}
