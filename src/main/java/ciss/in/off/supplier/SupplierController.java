package ciss.in.off.supplier;

//import java.io.ByteArrayOutputStream;

import java.util.Map;

import org.apache.camel.CamelContext;
//import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
//import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//import ciss.in.jena.RDFStream;
//import ciss.in.models.UserInfo;

@RestController
//@RequestMapping("/supplier")
public class SupplierController extends WebMvcConfigurerAdapter {
	
	@Autowired
	CamelContext camelContext;
	
	@Autowired
	ProducerTemplate producerTemplate;
	
	static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
	
	@RequestMapping(value="/supplier/data", method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> sendMsgToTopic(@RequestBody Map<String, Object> newMsg) {

		System.out.println("newMsg " + newMsg.toString());
/*		String personURI = "http://www.ilariahome.com";
		String firstName = "Ilaria";
		String familyName = "C";
		String title = "Dr";
		String nickName = "ila";
		String homePage = "http://www.semanticreatures.com";
		String fullName = firstName + " " + familyName;
		String schoolHomepage = "http://wwww.school.com";*/
		
/*		newMsg.setPersonURI(personURI);
		newMsg.setFamily_name(familyName);
		newMsg.setFullName(fullName);
		newMsg.setGivenname(firstName);
		newMsg.setHomepage(homePage);
		newMsg.setNickName(nickName);
		newMsg.setPersonURI(personURI);
		newMsg.setSchoolHomepage(schoolHomepage);
		newMsg.setTitle(title);*/
		
		
/*    	Endpoint controller1 = camelContext.getEndpoint("direct:supplier1");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller1);

    	producerTemplate.sendBodyAndHeader(newMsg, KafkaConstants.PARTITION_KEY, 1);
 
    	Endpoint controller2 = camelContext.getEndpoint("direct:supplier2");
    	producerTemplate = camelContext.createProducerTemplate();
    	producerTemplate.setDefaultEndpoint(controller2);

    	RDFStream rdf = new RDFStream();
    	ByteArrayOutputStream out = (ByteArrayOutputStream) rdf.createRDF(newMsg);
    	
    	producerTemplate.sendBodyAndHeader(out, KafkaConstants.PARTITION_KEY, 1);

    	
    	Endpoint controller3 = camelContext.getEndpoint("direct:start-supplier");
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
    	producerTemplate.sendBody(queryString);
*/    	
		return newMsg;
    }
}