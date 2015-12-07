package facebook.lab3;
import javax.jws.WebService;

@WebService(targetNamespace = "http://superbizint.org/wsdl")
public interface UserInterface {
	 public int signUp(String userObject);

	    public String signIn(String mul1, String mul2);
}
