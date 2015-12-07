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

import org.json.JSONException;
import org.json.JSONObject;

import sql.MysqlConnection;

@Stateless
@WebService(portName = "UserPort", serviceName = "UserService", targetNamespace = "http://superbiz1.org/wsdl", endpointInterface = "facebook.lab3.UserInterface")
public class UserImpl implements UserInterface {
   
	public int signUp(String userObject)  {
		Connection conn = MysqlConnection.getConnection();
		 int res =0;
		System.out.println("userObject" + userObject);
		String firstName="";
		String lastName="";
		String email="";
		int phoneNo=0;
		String password="";
		String dob="";
		try {
			JSONObject json = new JSONObject(userObject);
			if (json.has("firstName")) {
				firstName=json.get("firstName").toString();
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			if (json.has("lastName")) {
				lastName=json.get("lastName").toString();
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			if (json.has("email")) {
				email=json.get("email").toString();
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			if (json.has("phoneNo")) {
				phoneNo=(int) json.get("phoneNo");
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			if (json.has("password")) {
				password=json.get("password").toString();
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			if (json.has("dob")) {
				dob=json.get("dob").toString();
			}
			else{
				//Server side validation failing code
				return 401 ;
			}
			 String query = " insert into fb_user (first_name, last_name, email, password, phone_no,dob)"
				        + " values (?, ?, ?, ?, ?,?)";
			 SimpleDateFormat from = new SimpleDateFormat("MM-dd-yyyy");
			 SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
			 Date date = from.parse(dob); 
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, email);
			preparedStatement.setInt(4, phoneNo);
			preparedStatement.setString (5, password);
			preparedStatement.setDate(6, new java.sql.Date(date.getTime()));
			res = preparedStatement.executeUpdate();
			System.out.println("res"+res);
			conn.close();
		} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 401;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 401;
			}
			e.printStackTrace();
			return 401;
		}
		return res;
	}

	public String signIn(String mul1, String mul2) {
		String query = "select * from fb_user where email=? and password =?";
		String json ="";
		Connection conn = MysqlConnection.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, mul1);
			preparedStatement.setString(2, mul2);
			ResultSet rs = preparedStatement.executeQuery();
			JSONObject obj = new JSONObject();
		rs.last();
			if(rs.getRow()==0){
				System.out.println("no of rows fetched"+rs.getRow());
				return "401";
			}
			else{
			
				obj.put("user_id", rs.getString("user_id"));
				obj.put("first_name", rs.getString("first_name"));
				obj.put("last_name", rs.getString("last_name"));
				obj.put("email", rs.getString("email"));
				obj.put("password", rs.getString("password"));
				obj.put("phone_no", rs.getString("phone_no"));
				obj.put("dob", rs.getString("dob"));
			
			}
			System.out.println("returning obj"+obj.toString());
			return obj.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "401";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "401";
		}
	}

}
