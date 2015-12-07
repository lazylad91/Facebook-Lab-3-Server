package facebook.lab3;
import javax.jws.WebService;

@WebService(targetNamespace = "http://superbizin7s.org/wsdl")
public interface UserFriendInterface {
	 public String checkAddFriend(String logInUserId,String profUserId);
	 public String sendFriendRequest(String logInUserId,String profUserId);
	 public String getPending(String userId);
	 public String acceptReq(String userId,String friendId);
	 public String getFriend(String userId,String friendId);
}
