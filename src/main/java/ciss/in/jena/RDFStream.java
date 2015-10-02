package ciss.in.jena;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;

import ciss.in.models.User;
 
public class RDFStream extends Object {
 
	public OutputStream createRDF(User userNow) {
		
		String personURI = userNow.getPersonURI();
		String firstName = userNow.getGivenname();
		String familyName = userNow.getFamily_name();
		String title = userNow.getTitle();
		String nickName = userNow.getNickName();
		String homePage = userNow.getHomepage();
		String fullName = userNow.getFullName();
		String schoolHomepage = userNow.getSchoolHomepage();
	 
		 // create an empty model
		 Model model = ModelFactory.createDefaultModel();
		 String nsFoaf = "http://xmlns.com/foaf/0.1/";               // FOAF namespace, will be needed later for RQL queries
		 model.setNsPrefix("foaf", nsFoaf);                        // add FOAF namespace prefix to model	 
		 // create the resource
		 // and add the properties cascading styleResource
		 model.createResource(personURI, FOAF.Person)
		 	.addProperty(FOAF.name, fullName)
			.addProperty(FOAF.title, title)
			.addProperty(FOAF.givenname, firstName)
			.addProperty(FOAF.family_name, familyName)
			.addProperty(FOAF.nick, nickName)
			.addProperty(FOAF.homepage, homePage)
			.addProperty(FOAF.homepage, schoolHomepage);
	 
		 // write the model in JSONLD
		 //model.write(System.out, "JSONLD");
		 OutputStream out = new ByteArrayOutputStream();
		 RDFDataMgr.write(out, model, Lang.RDFXML);
		 return out;
	}
}