package facebook.lab3;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sql.MysqlConnection;

@WebService(targetNamespace = "http://superbizin5s.org/wsdl")
public interface UserSearchInterface {

	    public String searchUser(String searchText);

		
	
}
