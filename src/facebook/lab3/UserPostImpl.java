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
@WebService(portName = "UserInfoPort", serviceName = "UserPostService", targetNamespace = "http://superbiz3.org/wsdl", endpointInterface = "facebook.lab3.UserPostInterface")
public class UserPostImpl implements UserPostInterface {

	@Override
	public String savePost(String userId, String firstName, String post) {

		int resultCode;
		Connection conn = MysqlConnection.getConnection();
		String query = "insert into fb_post (user_id,user_name,post_desc) values(?,?,?)";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, post);
			resultCode = preparedStatement.executeUpdate();
			if (resultCode == 0) {
				return "501";
			}

			String query2 = "select * from fb_post where user_id in (select ? as user_id from fb_friend UNION select friend_user_id from fb_friend where user_id = ? ) order by post_time DESC";
			PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
			preparedStatement2.setString(1, userId);
			preparedStatement2.setString(2, userId);
			ResultSet rs = preparedStatement2.executeQuery();
			JSONArray arr = new JSONArray();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("post_id", rs.getString("post_id"));
				obj.put("user_id", rs.getString("user_id"));
				obj.put("user_name", rs.getString("user_name"));
				obj.put("post_desc", rs.getString("post_desc"));
				obj.put("post_time", rs.getString("post_time"));
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
	public String getPost(String userId) {
		Connection conn = MysqlConnection.getConnection();
		try{
		String query2 = "select * from fb_post where user_id in (select ? as user_id from fb_friend UNION select friend_user_id from fb_friend where user_id = ? ) order by post_time DESC";
		PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
		preparedStatement2.setString(1, userId);
		preparedStatement2.setString(2, userId);
		ResultSet rs = preparedStatement2.executeQuery();
		JSONArray arr = new JSONArray();
		while (rs.next()) {
			JSONObject obj = new JSONObject();
			obj.put("post_id", rs.getString("post_id"));
			obj.put("user_id", rs.getString("user_id"));
			obj.put("user_name", rs.getString("user_name"));
			obj.put("post_desc", rs.getString("post_desc"));
			obj.put("post_time", rs.getString("post_time"));
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
