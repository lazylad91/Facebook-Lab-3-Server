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
@WebService(portName = "UserInfoPort", serviceName = "UserSearchService", targetNamespace = "http://superbiz6.org/wsdl", endpointInterface = "facebook.lab3.UserSearchInterface")
public class UserSearchImpl implements UserSearchInterface {

		@Override
		public String searchUser(String searchText) {
			Connection conn = MysqlConnection.getConnection();
			try{
			String query2 = "select * from fb_user where first_name like ?";
			PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
			preparedStatement2.setString(1, searchText+"%");
			ResultSet rs = preparedStatement2.executeQuery();
			JSONArray arr = new JSONArray();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("user_id", rs.getString("user_id"));
				obj.put("first_name", rs.getString("first_name"));
				obj.put("last_name", rs.getString("last_name"));
				obj.put("email", rs.getString("email"));
				obj.put("password", rs.getString("password"));
				obj.put("phone_no", rs.getString("phone_no"));
				obj.put("dob", rs.getDate("dob"));
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
