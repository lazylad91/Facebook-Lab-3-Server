package facebook.lab3;
import javax.jws.WebService;

@WebService(targetNamespace = "http://superbizin3.org/wsdl")
public interface UserInfoInterface {
	 public String saveEdu(String userId,String from,String to,String name,String empOrEdu);

	    public String saveEvent(String userId,String event, String yearOfEvent);
	    
	    public String getEvent(String userId);
	    
	    public String getEdu(String userId);
}
