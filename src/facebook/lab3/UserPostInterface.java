package facebook.lab3;
import javax.jws.WebService;

@WebService(targetNamespace = "http://superbizin4s.org/wsdl")
public interface UserPostInterface {
	 public String savePost(String userId,String firstName,String post);

	    public String getPost(String userId);
	    
}
