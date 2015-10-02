package ciss.in.controllers;

//import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.camel.CamelContext;
//import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
//import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//import ciss.in.jena.RDFStream;
import ciss.in.models.User;
import ciss.in.repositories.UserRepository;
import ciss.in.service.UserService;

//@RestController
@Controller
public class UserController extends WebMvcConfigurerAdapter {
	
	@Autowired
	CamelContext camelContext;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProducerTemplate producerTemplate;
	
	static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
	@RequestMapping(value="/user/data", method=RequestMethod.POST)
    public String sendMsgToTopic(HttpServletRequest httpServletRequest, @ModelAttribute(value="usermsg") @Valid User user, BindingResult bindingResultUser, Model model) {

/*    	if (bindingResultUser.hasErrors()) { 	
        	model.addAttribute("WelcomeMessage","Enter valid User ID aand password");      	
        	return "/user/data";
        }*/
    	    	
		HttpSession session = httpServletRequest.getSession();
    	String username = (String) session.getAttribute("username");
    	ciss.in.models.User userNow = userRepository.findByUsername(username);
    	
    	userNow.setPersonURI(user.getPersonURI());
    	userNow.setGivenname(user.getGivenname());
    	userNow.setFamily_name(user.getFamily_name());
    	userNow.setTitle(user.getTitle());
    	userNow.setNickName(user.getNickName());
    	userNow.setHomepage(user.getHomepage());

		String fullName = user.getGivenname() + " " + user.getFamily_name();
		userNow.setFullName(fullName);
    	userNow.setSchoolHomepage(user.getSchoolHomepage());
    	    	
		UserService userservice = new UserService(userRepository);
		userservice.update(userNow);
 
/*    	Endpoint controller2 = camelContext.getEndpoint("direct:channel2");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller2);

    	RDFStream rdf = new RDFStream();
    	ByteArrayOutputStream out = (ByteArrayOutputStream) rdf.createRDF(userNow);
    	
    	producerTemplate.sendBodyAndHeader(out, KafkaConstants.PARTITION_KEY, 1);*/

    	
/*    	Endpoint controller3 = camelContext.getEndpoint("direct:start-user");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller3);
        
    	String queryString =
 				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
    			"SELECT ?nick WHERE { " +
    		  	"	?nick a foaf:Person . " +
    			"}";
    	PREFIX foaf: <http://xmlns.com/foaf/0.1/>

    		SELECT ?nick
    		WHERE {
    		  ?nick a foaf:Person .
    		}
    	
        String queryString = 
                //"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
        		//"INSERT DATA { " +
        		out.toString();// +
        		//" }";
    	producerTemplate.sendBody(queryString);*/
    	
		return "/user/data";
    }
}